package com.example.gethealth.data

import androidx.compose.runtime.mutableStateOf
import com.example.gethealth.model.User
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

/**
 * Repository for user-related data using a custom Supabase table.
 *
 * This version uses the 'Users' table instead of Supabase Auth.
 */
object UserRepository {

    /** 
     * The email of the currently logged-in user. 
     * We use mutableStateOf so that the UI (like MainScreen) automatically
     * updates whenever this value is restored or changed.
     */
    var currentUserEmail = mutableStateOf<String?>(null)

    /**
     * Logs a user in by checking the 'Users' table for matching credentials.
     * Returns the User object if successful, or null if login fails.
     */
    suspend fun login(email: String, password: String): User? {
        return try {
            val user = SupabaseClient.client.from("Users")
                .select(columns = Columns.ALL) {
                    filter {
                        eq("email", email)
                        eq("password", password)
                    }
                }
                .decodeSingleOrNull<User>()
            
            if (user != null) {
                currentUserEmail.value = user.email
            }
            user
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Registers a new user by inserting a row into the 'Users' table.
     */
    suspend fun register(name: String, email: String, password: String): User {
        // We use a map to avoid sending a null 'id' to an auto-incrementing column
        val data = mapOf(
            "name" to name,
            "email" to email,
            "password" to password
        )
        
        val registeredUser = SupabaseClient.client.from("Users")
            .insert(data) {
                select()
            }
            .decodeSingle<User>()
            
        currentUserEmail.value = registeredUser.email
        return registeredUser
    }
}
