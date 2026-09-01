package com.example.gethealth.ui.theme.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
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
 * Responsive layout: module cards sit in a LazyVerticalGrid using
 * GridCells.Adaptive. On a narrow (phone) screen this naturally lays out
 * as a single column, exactly like before. On a wide (tablet) screen it
 * automatically reflows into two or three columns — no manual breakpoint
 * logic needed, Compose recalculates the column count from the available
 * width on every recomposition (e.g. on rotation).
 */
@Composable
fun DashboardScreen(
    userName: String,
    onNavigateToMealPlanner: () -> Unit,
    onNavigateToFitness: () -> Unit,
    onNavigateToWellness: () -> Unit,
    onLogout: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 260.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header spans the full width regardless of how many columns
        // the grid decided to use for the cards below it.
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                Text(
                    text = "Welcome,",
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
            }
        }

        item {
            ModuleCard(
                title = "Meal Planner",
                description = "Plan and view your meals",
                icon = Icons.Filled.Restaurant,
                badgeBg = BadgeGreenBg,
                badgeFg = BadgeGreenFg,
                onClick = onNavigateToMealPlanner
            )
        }

        item {
            ModuleCard(
                title = "Wellness",
                description = "Track your mood and wellbeing",
                icon = Icons.Filled.SelfImprovement,
                badgeBg = BadgePurpleBg,
                badgeFg = BadgePurpleFg,
                onClick = onNavigateToWellness
            )
        }

        item {
            ModuleCard(
                title = "Fitness",
                description = "Track your workouts",
                icon = Icons.Filled.FitnessCenter,
                badgeBg = BadgePeachBg,
                badgeFg = BadgePeachFg,
                onClick = onNavigateToFitness
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
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
