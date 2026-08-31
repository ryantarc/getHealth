package com.example.gethealth.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gethealth.data.SessionManager
import com.example.gethealth.ui.screens.auth.LoginScreen
import com.example.gethealth.ui.screens.auth.RegisterScreen

/**
 * The app's top-level (outer) navigation graph.
 *
// ... (omitting comments)
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Check if there is a saved session on startup
    val savedUserName = remember { SessionManager.getSavedUserName(context) }
    val startDestination = if (savedUserName != null) {
        RootRoutes.mainRoute(savedUserName)
    } else {
        RootRoutes.LOGIN
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
// ...
        composable(RootRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { userName ->
                    // Save session when login succeeds
                    SessionManager.saveSession(context, userName)
                    
                    navController.navigate(RootRoutes.mainRoute(userName)) {
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
                onRegisterSuccess = { user ->
                    // Auto-login and save session after successful registration
                    SessionManager.saveSession(context, user.name)
                    
                    navController.navigate(RootRoutes.mainRoute(user.name)) {
                        popUpTo(RootRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // ...
        composable(
            route = RootRoutes.MAIN_WITH_ARG,
            arguments = listOf(navArgument(RootRoutes.USER_NAME_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString(RootRoutes.USER_NAME_ARG) ?: "User"

            MainScreen(
                userName = userName,
                onLogout = {
                    // Clear session when logging out
                    SessionManager.clearSession(context)

                    navController.navigate(RootRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
