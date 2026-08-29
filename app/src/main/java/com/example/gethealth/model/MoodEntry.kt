package com.example.gethealth.model

import kotlinx.serialization.Serializable

@Serializable
data class MoodEntry(
    val id: Long? = null,
    val email: String, // To associate with the user
    val mood: String,
    val note: String? = "",
    val date: String, // Store as YYYY-MM-DD
)
