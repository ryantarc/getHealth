package com.example.gethealth.navigation

/**
 * All navigation route names used by the app, gathered in one place.
 *
 * What it is: Just a set of constant Strings. Navigation Compose identifies
 * screens by String routes (e.g. "login", "dashboard").
 *
 * Why we need it: If every screen typed route names like "dashboard" by
 * hand, a single typo (e.g. "Dashboard" vs "dashboard") would cause a crash
 * that's hard to spot. Keeping every route name here means we only type it
 * once, and auto-complete does the rest everywhere else.
 */

/** Top-level routes: the "auth area" plus the entry point into the main app. */
object RootRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MAIN = "main"

    /** Argument name used to pass the logged-in user's name into the main area. */
    const val USER_NAME_ARG = "userName"

    /** Route pattern including the argument placeholder, used by NavHost. */
    const val MAIN_WITH_ARG = "$MAIN/{$USER_NAME_ARG}"

    /** Builds a real, navigable route for a given user name. */
    fun mainRoute(userName: String) = "$MAIN/$userName"
}

/** Routes inside the "main app area" (shown together with the bottom nav bar). */
object MainRoutes {
    const val DASHBOARD = "dashboard"
    const val MEAL_PLANNER = "mealPlanner"
    const val FITNESS = "fitness"
    const val WELLNESS = "wellness"
}
