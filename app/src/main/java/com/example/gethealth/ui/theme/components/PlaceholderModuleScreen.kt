package com.example.gethealth.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Shared "Coming Soon" layout used by all three module placeholder screens
 * (Meal Planner, Fitness, Wellness).
 *
 * Why we need it: The three module screens currently look identical apart
 * from their title and message. Instead of copy-pasting the same layout
 * three times (which the task description explicitly asks us to avoid),
 * we put the shared layout here once. Each module screen (see
 * ui/screens/mealplanner, ui/screens/fitness, ui/screens/wellness) just
 * calls this with its own title/message.
 *
 * When a teammate later builds the real Meal Planner feature, they simply
 * replace the body of MealPlannerScreen.kt — this shared component is
 * untouched and keeps working for Fitness and Wellness.
 */
@Composable
fun PlaceholderModuleScreen(
    title: String,
    message: String
) {
    Scaffold(
        topBar = { GetHealthTopBar(title = title) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Coming Soon",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
