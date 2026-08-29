package com.example.gethealth.domain

import com.example.gethealth.data.model.Category
import com.example.gethealth.data.model.Exercise
import kotlin.math.roundToInt

data class WorkoutItem(
    val exercise: Exercise,
    val prescription: String
)

data class GeneratedWorkout(
    val intensity: IntensityLevel,
    val selectedCategories: List<Category>,
    val items: List<WorkoutItem>
)

object WorkoutGenerator {

    fun generate(
        allExercises: List<Exercise>,
        selectedCategories: List<Category>,
        intensity: IntensityLevel
    ): GeneratedWorkout {
        require(selectedCategories.isNotEmpty()) { "Pick at least one target area." }

        val perCategory = when (selectedCategories.size) {
            1 -> intensity.exercisesPerCategory
            2 -> (intensity.exercisesPerCategory - 1).coerceAtLeast(2)
            else -> (intensity.exercisesPerCategory - 2).coerceAtLeast(2)
        }

        val picked = LinkedHashSet<Exercise>()

        for (category in selectedCategories) {
            val candidates = allExercises.filter { category in it.categories }.shuffled()
            picked.addAll(candidates.take(perCategory))
        }

        val items = picked.map { exercise ->
            WorkoutItem(
                exercise = exercise,
                prescription = buildPrescription(exercise, intensity)
            )
        }

        return GeneratedWorkout(
            intensity = intensity,
            selectedCategories = selectedCategories,
            items = items
        )
    }

    private fun buildPrescription(exercise: Exercise, intensity: IntensityLevel): String {
        exercise.baseReps?.let { baseReps ->
            val reps = (baseReps * intensity.volumeMultiplier).roundToInt().coerceAtLeast(1)
            return "$reps reps"
        }
        exercise.baseDurationSeconds?.let { baseSeconds ->
            val seconds = (baseSeconds * intensity.volumeMultiplier).roundToInt().coerceAtLeast(10)
            return if (seconds >= 60) {
                val minutes = seconds / 60.0
                "%.1f min".format(minutes)
            } else {
                "$seconds sec"
            }
        }
        return "As able"
    }
}