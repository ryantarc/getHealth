package com.example.gethealth.ui.screens.fitness

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gethealth.data.model.Category
import com.example.gethealth.data.repository.ExerciseRepository
import com.example.gethealth.domain.BmiCalculator
import com.example.gethealth.domain.BmiResult
import com.example.gethealth.domain.GeneratedWorkout
import com.example.gethealth.domain.WorkoutGenerator
import kotlinx.coroutines.launch

/**
 * NOTE ON STATE PERSISTENCE ACROSS LAYOUT CHANGES:
 * This ViewModel is created via Compose's `viewModel()` function, which
 * ties its lifetime to the Activity (through the ViewModelStore), NOT to
 * any individual Composable or screen size. That means rotating the
 * device, resizing a multi-window app, or going from phone-width to
 * tablet-width layout does NOT recreate this ViewModel — every property
 * below (heightText, weightText, selectedCategories, bmiResult,
 * generatedWorkout) survives all of that automatically. Nothing extra
 * needs to be done for "don't lose my input" — this is already handled by
 * how FitnessNavGraph.kt creates ONE shared instance.
 */
class FitnessViewModel(
    private val repository: ExerciseRepository = ExerciseRepository()
) : ViewModel() {

    var heightText by mutableStateOf("")
        private set
    var weightText by mutableStateOf("")
        private set
    var selectedCategories by mutableStateOf<Set<Category>>(emptySet())
        private set
    var bmiResult by mutableStateOf<BmiResult?>(null)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoadingWorkout by mutableStateOf(false)
        private set
    var generatedWorkout by mutableStateOf<GeneratedWorkout?>(null)
        private set

    /**
     * Filters keystrokes so only digits and a single decimal point can ever
     * land in heightText. This is a first line of defense — it stops
     * obviously-wrong characters (letters, symbols) before they're even
     * stored, on top of the full validation done in calculateBmi().
     */
    fun onHeightChanged(value: String) {
        heightText = sanitizeNumericInput(value, heightText)
        errorMessage = null
    }

    fun onWeightChanged(value: String) {
        weightText = sanitizeNumericInput(value, weightText)
        errorMessage = null
    }

    private fun sanitizeNumericInput(newValue: String, previousValue: String): String {
        // Allow empty (so the user can clear the field and retype).
        if (newValue.isEmpty()) return newValue
        // Allow only digits and at most one decimal point.
        val isValidPattern = newValue.matches(Regex("^\\d*\\.?\\d*$"))
        return if (isValidPattern) newValue else previousValue
    }

    fun setError(message: String) {
        errorMessage = message
    }

    fun toggleCategory(category: Category) {
        selectedCategories = if (category in selectedCategories) {
            selectedCategories - category
        } else {
            selectedCategories + category
        }
    }

    /**
     * Runs full validation (not just "is it a number", but also realistic
     * range checks) via BmiCalculator.validateAndCalculate. Any failure —
     * empty field, non-numeric text, or an out-of-range value — produces a
     * specific error message shown to the user instead of crashing or
     * silently producing a wrong BMI.
     */
    fun calculateBmi() {
        when (val result = BmiCalculator.validateAndCalculate(heightText, weightText)) {
            is BmiCalculator.ValidationResult.Success -> {
                errorMessage = null
                bmiResult = result.result
            }
            is BmiCalculator.ValidationResult.Error -> {
                errorMessage = result.message
                bmiResult = null
            }
        }
    }

    fun generateWorkout(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val bmi = bmiResult
        if (bmi == null) {
            errorMessage = "Calculate your BMI first."
            return
        }
        if (selectedCategories.isEmpty()) {
            errorMessage = "Select at least one target area."
            return
        }

        viewModelScope.launch {
            isLoadingWorkout = true
            try {
                val allExercises = repository.getAllExercises()
                val workout = WorkoutGenerator.generate(
                    allExercises = allExercises,
                    selectedCategories = selectedCategories.toList(),
                    intensity = bmi.intensity
                )
                generatedWorkout = workout
                isLoadingWorkout = false
                onSuccess()
            } catch (e: Exception) {
                isLoadingWorkout = false
                onError(e.message ?: "Could not load exercises. Check your connection.")
            }
        }
    }

    fun regenerateWorkout(onError: (String) -> Unit) {
        generateWorkout(onSuccess = {}, onError = onError)
    }
}