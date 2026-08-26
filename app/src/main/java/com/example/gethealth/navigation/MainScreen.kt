package com.example.gethealth.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gethealth.model.Recipe
import com.example.gethealth.ui.screens.dashboard.DashboardScreen
import com.example.gethealth.ui.screens.fitness.FitnessScreen
import com.example.gethealth.ui.screens.mealplanner.MealPlannerScreen
import com.example.gethealth.ui.screens.mealplanner.SavedRecipesScreen
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
 * OUTER NavHost in AppNavigation.kt.
 *
 * Saved-recipe state lives HERE (not inside MealPlannerScreen) because both
 * MealPlannerScreen (to show/toggle the bookmark icon) and
 * SavedRecipesScreen (to display the full saved list) need to see the same
 * data. Lifting it up to their shared parent is the simplest way to keep
 * them in sync without introducing a ViewModel yet. When Supabase is wired
 * in, this in-memory list will be replaced by real reads/writes.
 */
@Composable
fun MainScreen(
    userName: String,
    onLogout: () -> Unit
) {
    val innerNavController: NavHostController = rememberNavController()

    var savedRecipes by remember { mutableStateOf<List<Recipe>>(emptyList()) }

    fun toggleSaveRecipe(recipe: Recipe) {
        savedRecipes = if (savedRecipes.any { it.id == recipe.id }) {
            savedRecipes.filterNot { it.id == recipe.id }
        } else {
            savedRecipes + recipe
        }
    }

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
            composable(MainRoutes.MEAL_PLANNER) {
                MealPlannerScreen(
                    savedRecipeIds = savedRecipes.map { it.id }.toSet(),
                    onToggleSave = { recipe -> toggleSaveRecipe(recipe) },
                    onViewSavedRecipes = { innerNavController.navigate(MainRoutes.SAVED_RECIPES) }
                )
            }
            composable(MainRoutes.SAVED_RECIPES) {
                SavedRecipesScreen(
                    savedRecipes = savedRecipes,
                    onToggleSave = { recipe -> toggleSaveRecipe(recipe) },
                    onNavigateBack = { innerNavController.popBackStack() }
                )
            }
            composable(MainRoutes.FITNESS) { FitnessScreen() }
            composable(MainRoutes.WELLNESS) { WellnessScreen() }
        }
    }
}
