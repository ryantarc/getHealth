package com.example.gethealth.model

import kotlinx.serialization.Serializable

/**
 * Simple data class representing a logged-in user.
 *
 * What it is:  A "data class" is a Kotlin class whose main job is to hold data.
 *              Kotlin automatically generates useful code for us (toString(),
 *              equals(), etc.) so we don't have to write it by hand.
 *
 * Why we need it: We don't have a real backend yet, but we still want a clear,
 *              typed way to represent "a user". When real authentication is
 *              added later, we mainly update this class and UserRepository,
 *              not every screen that uses it.
 */
@Serializable
data class User(
    val id: Int? = null,
    val name: String,
    val email: String,
    val password: String? = null
)
