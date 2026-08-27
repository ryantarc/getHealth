package com.example.gethealth.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * The tablet/wide-screen equivalent of BottomNavBar: a vertical rail of
 * tabs down the left edge, matching the "Tablet Screen Design" slide in
 * the design deck.
 *
 * Deliberately mirrors BottomNavBar's structure (same routes, same simple
 * navigate() calls, no popUpTo/saveState) so both nav styles behave
 * identically — the only difference is which one MainScreen chooses to
 * show, based on the available screen width.
 */
@Composable
fun AppNavRail(navController: NavHostController) {
    val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
    val currentRoute: String? = navBackStackEntry?.destination?.hierarchy?.firstOrNull()?.route

    NavigationRail {
        NavigationRailItem(
            selected = currentRoute == MainRoutes.DASHBOARD,
            onClick = { navController.navigate(MainRoutes.DASHBOARD) },
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home") },
            label = { Text(text = "Home") }
        )

        NavigationRailItem(
            selected = currentRoute == MainRoutes.MEAL_PLANNER,
            onClick = { navController.navigate(MainRoutes.MEAL_PLANNER) },
            icon = { Icon(imageVector = Icons.Filled.Restaurant, contentDescription = "Meals") },
            label = { Text(text = "Meals") }
        )

        NavigationRailItem(
            selected = currentRoute == MainRoutes.FITNESS,
            onClick = { navController.navigate(MainRoutes.FITNESS) },
            icon = { Icon(imageVector = Icons.Filled.FitnessCenter, contentDescription = "Fitness") },
            label = { Text(text = "Fitness") }
        )

        NavigationRailItem(
            selected = currentRoute == MainRoutes.WELLNESS,
            onClick = { navController.navigate(MainRoutes.WELLNESS) },
            icon = { Icon(imageVector = Icons.Filled.SelfImprovement, contentDescription = "Wellness") },
            label = { Text(text = "Wellness") }
        )
    }
}
