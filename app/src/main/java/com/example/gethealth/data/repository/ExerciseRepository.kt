package com.example.gethealth.data.repository

import com.example.gethealth.data.model.Exercise
import com.example.gethealth.data.model.ExerciseDto
import com.example.gethealth.data.model.toDomain
import com.example.gethealth.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from

class ExerciseRepository {

    private val table = SupabaseClientProvider.client.from("exercises")

    suspend fun getAllExercises(): List<Exercise> {
        return table.select()
            .decodeList<ExerciseDto>()
            .map { it.toDomain() }
    }
}