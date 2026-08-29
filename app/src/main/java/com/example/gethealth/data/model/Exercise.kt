package com.example.gethealth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDto(
    val id: Long = 0,
    val name: String,
    val description: String,
    val categories: List<String>,
    @SerialName("base_reps") val baseReps: Int? = null,
    @SerialName("base_duration_seconds") val baseDurationSeconds: Int? = null,
    val equipment: String = "None"
)

data class Exercise(
    val id: Long,
    val name: String,
    val description: String,
    val categories: List<Category>,
    val baseReps: Int?,
    val baseDurationSeconds: Int?,
    val equipment: String
)

fun ExerciseDto.toDomain(): Exercise = Exercise(
    id = id,
    name = name,
    description = description,
    categories = categories.mapNotNull { Category.fromDbValue(it) },
    baseReps = baseReps,
    baseDurationSeconds = baseDurationSeconds,
    equipment = equipment
)