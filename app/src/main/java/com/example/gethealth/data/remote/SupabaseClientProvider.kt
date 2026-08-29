package com.example.gethealth.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClientProvider {

    val client by lazy {
        createSupabaseClient(
            supabaseUrl = com.example.gethealth.BuildConfig.SUPABASE_URL,
            supabaseKey = com.example.gethealth.BuildConfig.SUPABASE_ANON_KEY
        ) {
            install(Postgrest)
        }
    }
}