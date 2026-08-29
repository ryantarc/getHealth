package com.example.gethealth.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
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
import com.example.gethealth.ui.util.WindowWidthSize
import com.example.gethealth.ui.util.rememberWindowWidthSize

/**
 * The "main app area" screen: Dashboard + Meal Planner + Fitness + Wellness.
 *
 * Responsive navigation: on a COMPACT (phone-width) screen this shows the
 * usual bottom navigation bar. On an EXPANDED (tablet-width) screen it
 * switches to a NavigationRail down the left edge instead — matching the
 * "Tablet Screen Design" slide in the deck. Both nav styles drive the same
 * NavHost/routes, so no screen needs to know or care which one is showing.
 *
 * Saved-recipe state is hoisted here (see previous comment) so both
 * MealPlannerScreen and SavedRecipesScreen share it.
 */
@Composable
fun MainScreen(
    userName: String,
    onLogout: () -> Unit
) {
    val innerNavController: NavHostController = rememberNavController()
    val windowWidthSize = rememberWindowWidthSize()

    var savedRecipes by remember { mutableStateOf<List<Recipe>>(emptyList()) }

    fun toggleSaveRecipe(recipe: Recipe) {
        savedRecipes = if (savedRecipes.any { it.id == recipe.id }) {
            savedRecipes.filterNot { it.id == recipe.id }
        } else {
            savedRecipes + recipe
        }
    }

    val content = remember {
        movableContentOf { modifier: Modifier ->
            NavHost(
                navController = innerNavController,
                startDestination = MainRoutes.DASHBOARD,
                modifier = modifier
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

    if (windowWidthSize == WindowWidthSize.EXPANDED) {
        // Tablet layout: a NavigationRail beside the content, no bottom bar.
        Row(modifier = Modifier.fillMaxSize()) {
            AppNavRail(navController = innerNavController)
            content(Modifier.weight(1f))
        }
    } else {
        // Phone layout: the familiar bottom navigation bar.
        Scaffold(
            bottomBar = { BottomNavBar(navController = innerNavController) }
        ) { innerPadding ->
            content(Modifier.padding(innerPadding))
        }
    }
}
