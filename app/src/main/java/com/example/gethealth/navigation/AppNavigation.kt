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
import com.example.gethealth.data.UserRepository
import com.example.gethealth.ui.theme.screens.auth.LoginScreen
import com.example.gethealth.ui.theme.screens.auth.RegisterScreen

/**
 * The app's top-level (outer) navigation graph.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Restore the session data SYNCHRONOUSLY on startup
    // This ensures that when MainScreen loads, the email is already available.
    val savedName = remember { 
        val name = SessionManager.getSavedUserName(context)
        val email = SessionManager.getSavedUserEmail(context)
        
        // Restore the email to the singleton repository immediately
        UserRepository.currentUserEmail.value = email
        
        name
    }

    val startDestination = if (savedName != null) {
        RootRoutes.mainRoute(savedName)
    } else {
        RootRoutes.LOGIN
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(RootRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { user ->
                    // Save session and update repository state
                    SessionManager.saveSession(context, user.name, user.email)
                    UserRepository.currentUserEmail.value = user.email
                    
                    navController.navigate(RootRoutes.mainRoute(user.name)) {
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
                    // Auto-login and save session
                    SessionManager.saveSession(context, user.name, user.email)
                    UserRepository.currentUserEmail.value = user.email
                    
                    navController.navigate(RootRoutes.mainRoute(user.name)) {
                        popUpTo(RootRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = RootRoutes.MAIN_WITH_ARG,
            arguments = listOf(navArgument(RootRoutes.USER_NAME_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString(RootRoutes.USER_NAME_ARG) ?: "User"

            MainScreen(
                userName = userName,
                onLogout = {
                    // Clear session and reset repository state
                    SessionManager.clearSession(context)
                    UserRepository.currentUserEmail.value = null

                    navController.navigate(RootRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
