package com.example.gethealth.data

import com.example.gethealth.model.User
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

/**
 * Repository for user-related data using a custom Supabase table.
 *
 * This version uses the 'Users' table instead of Supabase Auth.
 */
object UserRepository {
    var currentUserEmail: String? = null

    /**
     * Logs a user in by checking the 'Users' table for matching credentials.
     */

    suspend fun login(email: String, password: String): Boolean {
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
                currentUserEmail = email
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Registers a new user by inserting a row into the 'Users' table.
     */
    suspend fun register(name: String, email: String, password: String): User {
        val newUser = User(name = name, email = email, password = password)
        
        return SupabaseClient.client.from("Users")
            .insert(newUser) {
                select()
            }
            .decodeSingle<User>()
    }
}
