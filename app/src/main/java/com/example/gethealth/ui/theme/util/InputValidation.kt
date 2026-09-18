package com.example.gethealth.ui.util

import android.util.Patterns

/**
 * Utility functions for validating user input.
 */

/**
 * Performs a cheap local check to see if the input string likely contains
 * actual ingredients (letters) rather than just numbers or gibberish.
 * This saves unnecessary API calls for obviously invalid input.
 */
fun looksLikeValidInput(text: String): Boolean {
    val trimmed = text.trim()
    if (trimmed.isBlank()) return false
    if (trimmed.length < 2) return false
    
    // Check if at least half of the characters are letters.
    // This filters out things like "123456" or "!!!"
    val letterCount = trimmed.count { it.isLetter() }
    return letterCount >= trimmed.length / 2
}

/**
 * Checks if a string is a valid email format and specifically ends with @gmail.com.
 */
fun isValidEmail(email: String): Boolean {
    return email.isNotBlank() && 
           Patterns.EMAIL_ADDRESS.matcher(email).matches() && 
           email.lowercase().endsWith("@gmail.com")
}
