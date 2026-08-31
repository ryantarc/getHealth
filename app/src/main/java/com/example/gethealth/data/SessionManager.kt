package com.example.gethealth.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Helper class to handle saving and loading the user's login session.
 * 
 * What it is: A wrapper around SharedPreferences (Android's simple key-value storage).
 * Why we need it: To remember who is logged in even after the app is closed.
 */
object SessionManager {
    private const val PREF_NAME = "gethealth_prefs"
    private const val KEY_USER_NAME = "logged_in_user_name"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Saves the user's name and marks them as logged in.
     */
    fun saveSession(context: Context, userName: String) {
        val editor = getPrefs(context).edit()
        editor.putString(KEY_USER_NAME, userName)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.apply()
    }

    /**
     * Loads the saved user name. Returns null if no one is logged in.
     */
    fun getSavedUserName(context: Context): String? {
        return getPrefs(context).getString(KEY_USER_NAME, null)
    }

    /**
     * Checks if a user is currently logged in.
     */
    fun isLoggedIn(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * Clears the saved session (logs the user out).
     */
    fun clearSession(context: Context) {
        val editor = getPrefs(context).edit()
        editor.clear()
        editor.apply()
    }
}
