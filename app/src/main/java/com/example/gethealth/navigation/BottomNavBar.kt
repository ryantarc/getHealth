package com.example.gethealth.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * The bottom navigation bar shown across the "main app area"
 * (Dashboard, Meal Planner, Fitness, Wellness) but NOT on Login/Register.
 *
 * NOTE: This uses the classic Compose Material (Material 2) BottomNavigation
 * / BottomNavigationItem instead of Material3's NavigationBar /
 * NavigationBarItem. Both look and behave almost identically, but
 * BottomNavigation has existed since Compose's earliest Material releases,
 * so it doesn't depend on having a recent Material3/BOM version — it just
 * needs the "androidx.compose.material:material" dependency, which
 * material-icons-extended already pulls in transitively.
 */
@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
    val currentRoute: String? = navBackStackEntry?.destination?.hierarchy?.firstOrNull()?.route

    BottomNavigation {
        BottomNavigationItem(
            selected = currentRoute == MainRoutes.DASHBOARD,
            onClick = {
                navController.popBackStack(MainRoutes.DASHBOARD, false)
            },
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home") },
            label = { Text(text = "Home") }
        )

        BottomNavigationItem(
            selected = currentRoute == MainRoutes.MEAL_PLANNER,
            onClick = {
                navController.navigate(MainRoutes.MEAL_PLANNER) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(imageVector = Icons.Filled.Restaurant, contentDescription = "Meals") },
            label = { Text(text = "Meals") }
        )

        BottomNavigationItem(
            selected = currentRoute == MainRoutes.FITNESS,
            onClick = {
                navController.navigate(MainRoutes.FITNESS) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(imageVector = Icons.Filled.FitnessCenter, contentDescription = "Fitness") },
            label = { Text(text = "Fitness") }
        )

        BottomNavigationItem(
            selected = currentRoute == MainRoutes.WELLNESS,
            onClick = {
                navController.navigate(MainRoutes.WELLNESS) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(imageVector = Icons.Filled.SelfImprovement, contentDescription = "Wellness") },
            label = { Text(text = "Wellness") }
        )
    }
}
