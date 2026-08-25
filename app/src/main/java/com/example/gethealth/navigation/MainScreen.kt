package com.example.gethealth.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gethealth.ui.screens.dashboard.DashboardScreen
import com.example.gethealth.ui.screens.fitness.FitnessScreen
import com.example.gethealth.ui.screens.mealplanner.MealPlannerScreen
import com.example.gethealth.ui.screens.wellness.WellnessScreen

/**
 * The "main app area" screen: Dashboard + Meal Planner + Fitness + Wellness,
 * always shown together with the bottom navigation bar.
 *
 * A small design decision worth explaining: the task description asks for a
 * single top-level NavHost, but the bottom nav bar must NOT appear on
 * Login/Register. The simplest way to achieve that with Navigation Compose
 * is to give the "main area" its own small NavHost (nested inside this
 * screen), while Login/Register/Main stay as three destinations of the
 * OUTER NavHost in AppNavigation.kt. This keeps the auth area completely
 * separate from the bottom-nav area without any manual "show/hide bottom
 * bar" logic scattered across screens.
 *
 * `userName` and `onLogout` come from AppNavigation.kt (the outer NavHost).
 */
@Composable
fun MainScreen(
    userName: String,
    onLogout: () -> Unit
) {
    // This is a SEPARATE NavController from the one in AppNavigation.kt.
    // It only controls navigation between the 4 tabs below.
    val innerNavController: NavHostController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController = innerNavController) }
    ) { innerPadding ->
        NavHost(
            navController = innerNavController,
            startDestination = MainRoutes.DASHBOARD,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainRoutes.DASHBOARD) {
                DashboardScreen(
                    userName = userName,
                    onNavigateToMealPlanner = { innerNavController.navigate(MainRoutes.MEAL_PLANNER) },
                    onNavigateToFitness = { innerNavController.navigate(MainRoutes.FITNESS) },
                    onNavigateToWellness = { innerNavController.navigate(MainRoutes.WELLNESS) },
                    onLogout = onLogout
                )
            }
            composable(MainRoutes.MEAL_PLANNER) { MealPlannerScreen() }
            composable(MainRoutes.FITNESS) { FitnessScreen() }
            composable(MainRoutes.WELLNESS) { WellnessScreen() }
        }
    }
}
