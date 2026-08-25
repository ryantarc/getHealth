package com.example.gethealth.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Describes one item in the bottom navigation bar: its route (used for
 * navigation), the label shown to the user, and the icon.
 *
 * What it is: A small data class + a fixed list of the 4 tabs we have.
 *
 * Why we need it: Instead of hardcoding 4 separate NavigationBarItem blocks
 * with repeated logic inside BottomNavBar.kt, we describe each tab once as
 * data, then loop over the list to build the UI. This makes it very easy to
 * add a 5th tab later — just add one line here.
 */
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(MainRoutes.DASHBOARD, "Home", Icons.Filled.Home),
    BottomNavItem(MainRoutes.MEAL_PLANNER, "Meals", Icons.Filled.Restaurant),
    BottomNavItem(MainRoutes.FITNESS, "Fitness", Icons.Filled.FitnessCenter),
    BottomNavItem(MainRoutes.WELLNESS, "Wellness", Icons.Filled.SelfImprovement)
)
