package com.example.gethealth.navigation

import androidx.compose.material.Icon
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * A vertical side navigation bar shown instead of the bottom nav bar on
 * tablet-sized screens (see MainScreen.kt) — this matches the sidebar
 * layout in the "Tablet Screen Design" slide of the design deck.
 *
 * Mirrors BottomNavBar.kt exactly (same routes, same simple
 * navController.navigate(route) calls) so the two are easy to keep in
 * sync — this is deliberately NOT shared code with BottomNavBar because
 * NavigationRailItem and BottomNavigationItem have slightly different
 * parameters, and keeping them as two small, obvious files is simpler for
 * beginners than a shared abstraction over both.
 */
@Composable
fun NavRailBar(navController: NavHostController) {
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
