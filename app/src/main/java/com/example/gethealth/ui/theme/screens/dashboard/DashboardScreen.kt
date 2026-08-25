package com.example.gethealth.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gethealth.ui.theme.BadgeGreenBg
import com.example.gethealth.ui.theme.BadgeGreenFg
import com.example.gethealth.ui.theme.BadgePeachBg
import com.example.gethealth.ui.theme.BadgePeachFg
import com.example.gethealth.ui.theme.BadgePurpleBg
import com.example.gethealth.ui.theme.BadgePurpleFg

/**
 * The main Dashboard screen (the "Home" tab).
 *
 * Styled to match the GetHealth design deck: a greeting header, then one
 * card per module with a soft colored circular icon badge — green for Meal
 * Planner, purple for Wellness/Mood, peach for Fitness — mirroring the
 * "Screen Design" slides.
 */
@Composable
fun DashboardScreen(
    userName: String,
    onNavigateToMealPlanner: () -> Unit,
    onNavigateToFitness: () -> Unit,
    onNavigateToWellness: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Good day,",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = userName.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.size(4.dp))

        Text(
            text = "Three tools to support your health and well-being. Choose where to start.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ModuleCard(
            title = "Meal Planner",
            description = "Plan and view your meals",
            icon = Icons.Filled.Restaurant,
            badgeBg = BadgeGreenBg,
            badgeFg = BadgeGreenFg,
            onClick = onNavigateToMealPlanner
        )

        ModuleCard(
            title = "Wellness",
            description = "Track your mood and wellbeing",
            icon = Icons.Filled.SelfImprovement,
            badgeBg = BadgePurpleBg,
            badgeFg = BadgePurpleFg,
            onClick = onNavigateToWellness
        )

        ModuleCard(
            title = "Fitness",
            description = "Track your workouts",
            icon = Icons.Filled.FitnessCenter,
            badgeBg = BadgePeachBg,
            badgeFg = BadgePeachFg,
            onClick = onNavigateToFitness
        )

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}

/**
 * A single tappable card representing one module, styled with a soft
 * colored circular icon badge on the left (matching the design deck).
 */
@Composable
private fun ModuleCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    badgeBg: Color,
    badgeFg: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(color = badgeBg, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = badgeFg)
            }

            Spacer(modifier = Modifier.size(14.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
