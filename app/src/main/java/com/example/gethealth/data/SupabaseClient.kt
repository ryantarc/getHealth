package com.example.gethealth.data

import com.example.gethealth.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

/**
 * Singleton instance of the Supabase client.
 *
 * This object initializes the connection to Supabase using the URL and API Key
 * defined in local.properties (and exposed via BuildConfig).
 */
object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Auth)
        install(Postgrest)
    }
}
