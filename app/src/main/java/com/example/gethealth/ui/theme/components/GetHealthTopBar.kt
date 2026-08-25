package com.example.gethealth.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

/**
 * A reusable top app bar (the title bar shown at the top of most screens).
 *
 * What it is: A small wrapper around Material 3's CenterAlignedTopAppBar,
 *             styled with the app's forest-green brand color to match the
 *             design deck's dark header bars.
 *
 * Why we need it: So every screen's top bar looks consistent, and we can
 *             change the styling for the whole app in one place.
 *
 * Where it's used: DashboardScreen and the module placeholder screens
 *             (Meal Planner, Fitness, Wellness).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetHealthTopBar(title: String) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
