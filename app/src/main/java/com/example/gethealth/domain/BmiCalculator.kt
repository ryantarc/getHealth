package com.example.gethealth.domain

import kotlin.math.roundToInt

enum class BmiCategory(val label: String) {
    UNDERWEIGHT("Underweight"),
    NORMAL("Normal"),
    OVERWEIGHT("Overweight"),
    OBESE("Obese")
}

enum class IntensityLevel(
    val label: String,
    val volumeMultiplier: Double,
    val exercisesPerCategory: Int
) {
    LIGHT("Light / Beginner", volumeMultiplier = 0.8, exercisesPerCategory = 3),
    STANDARD("Standard", volumeMultiplier = 1.0, exercisesPerCategory = 4),
    MODERATE("Moderate", volumeMultiplier = 1.15, exercisesPerCategory = 4),
    HIGHER("Higher Intensity", volumeMultiplier = 1.3, exercisesPerCategory = 4)
}

data class BmiResult(
    val bmi: Double,
    val category: BmiCategory,
    val intensity: IntensityLevel
)

object BmiCalculator {

    // Sanity bounds — outside these, the input is almost certainly a typo,
    // not a real human measurement. Prevents nonsense results like a BMI
    // of 40000 from a stray extra digit.
    private const val MIN_HEIGHT_CM = 50.0
    private const val MAX_HEIGHT_CM = 250.0
    private const val MIN_WEIGHT_KG = 20.0
    private const val MAX_WEIGHT_KG = 300.0

    sealed class ValidationResult {
        data class Success(val result: BmiResult) : ValidationResult()
        data class Error(val message: String) : ValidationResult()
    }

    /**
     * Validates raw text input BEFORE doing any math, so a non-numeric
     * string (e.g. "abc", empty text, or a stray letter) never reaches the
     * BMI formula at all — it's caught here with a clear error message.
     */
    fun validateAndCalculate(heightText: String, weightText: String): ValidationResult {
        if (heightText.isBlank() || weightText.isBlank()) {
            return ValidationResult.Error("Please fill in both height and weight.")
        }

        val height = heightText.toDoubleOrNull()
            ?: return ValidationResult.Error("Height must be a number (e.g. 170).")
        val weight = weightText.toDoubleOrNull()
            ?: return ValidationResult.Error("Weight must be a number (e.g. 65).")

        if (height < MIN_HEIGHT_CM || height > MAX_HEIGHT_CM) {
            return ValidationResult.Error("Height must be between $MIN_HEIGHT_CM and $MAX_HEIGHT_CM cm.")
        }
        if (weight < MIN_WEIGHT_KG || weight > MAX_WEIGHT_KG) {
            return ValidationResult.Error("Weight must be between $MIN_WEIGHT_KG and $MAX_WEIGHT_KG kg.")
        }

        return ValidationResult.Success(calculate(heightCm = height, weightKg = weight))
    }

    fun calculate(heightCm: Double, weightKg: Double): BmiResult {
        require(heightCm > 0) { "Height must be greater than zero." }
        require(weightKg > 0) { "Weight must be greater than zero." }

        val heightM = heightCm / 100.0
        val rawBmi = weightKg / (heightM * heightM)
        val bmi = (rawBmi * 10).roundToInt() / 10.0

        val category = when {
            bmi < 18.5 -> BmiCategory.UNDERWEIGHT
            bmi < 25.0 -> BmiCategory.NORMAL
            bmi < 30.0 -> BmiCategory.OVERWEIGHT
            else -> BmiCategory.OBESE
        }

        val intensity = when (category) {
            BmiCategory.UNDERWEIGHT -> IntensityLevel.LIGHT
            BmiCategory.NORMAL -> IntensityLevel.STANDARD
            BmiCategory.OVERWEIGHT -> IntensityLevel.MODERATE
            BmiCategory.OBESE -> IntensityLevel.LIGHT
        }

        return BmiResult(bmi = bmi, category = category, intensity = intensity)
    }
}