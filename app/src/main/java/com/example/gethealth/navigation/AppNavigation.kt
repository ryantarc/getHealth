package com.example.gethealth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gethealth.ui.screens.auth.LoginScreen
import com.example.gethealth.ui.screens.auth.RegisterScreen

/**
 * The app's top-level (outer) navigation graph.
 *
 * What it is: A NavHost is the container that swaps one screen for another
 * as the user navigates. It needs a NavController (which remembers where
 * the user currently is and the "back stack" of previous screens) and a
 * `startDestination` (the first screen shown).
 *
 * The app has two areas:
 *   AUTH AREA:  Login, Register            (no bottom nav bar)
 *   MAIN AREA:  Dashboard/Meals/Fitness/Wellness (has a bottom nav bar,
 *               all handled together inside MainScreen.kt)
 *
 * This file only knows about 3 destinations: "login", "register" and
 * "main/{userName}". Everything inside the main area is handled by its own
 * nested NavHost in MainScreen.kt — see the comment there for why.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RootRoutes.LOGIN
    ) {
        composable(RootRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { userName ->
                    navController.navigate(RootRoutes.mainRoute(userName)) {
                        // Remove Login from the back stack so the user
                        // can't press "back" and return to it after
                        // logging in.
                        popUpTo(RootRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(RootRoutes.REGISTER)
                }
            )
        }

        composable(RootRoutes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    // After registering, send the user back to Login so
                    // they can sign in with their new (fake) account.
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // The main area receives the logged-in user's name as a navigation
        // argument. This demonstrates simple data-passing between screens:
        // LoginScreen -> AppNavigation -> MainScreen -> DashboardScreen.
        composable(
            route = RootRoutes.MAIN_WITH_ARG,
            arguments = listOf(navArgument(RootRoutes.USER_NAME_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString(RootRoutes.USER_NAME_ARG) ?: "User"

            MainScreen(
                userName = userName,
                onLogout = {
                    navController.navigate(RootRoutes.LOGIN) {
                        // Clear the whole back stack so logging out fully
                        // resets the app back to a clean Login screen.
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
