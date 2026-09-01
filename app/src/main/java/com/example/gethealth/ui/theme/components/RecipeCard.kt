package com.example.gethealth.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gethealth.model.Recipe

/**
 * A card showing one recipe suggestion — title, description, quick stats
 * (time / calories / protein), tags, and a bookmark toggle to save/unsave
 * it. Styled to match the recipe cards in the design deck's Meal Planner
 * and Saved Recipes screens.
 *
 * This one component is reused on both MealPlannerScreen (freshly
 * generated recipes) and SavedRecipesScreen (previously saved ones) so the
 * two screens stay visually consistent and we don't duplicate this layout.
 */
@Composable
fun RecipeCard(
    recipe: Recipe,
    isSaved: Boolean,
    onToggleSave: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onToggleSave) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = if (isSaved) "Unsave recipe" else "Save recipe",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                text = recipe.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.size(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                StatItem(icon = Icons.Outlined.Schedule, text = "${recipe.minutes} min")
                Spacer(modifier = Modifier.size(16.dp))
                StatItem(icon = Icons.Filled.Whatshot, text = "${recipe.calories} kcal")
                Spacer(modifier = Modifier.size(16.dp))
                StatItem(icon = Icons.Outlined.Bolt, text = "${recipe.proteinG}g protein")
            }

            if (recipe.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.size(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recipe.tags) { tag -> TagChip(text = tag) }
                }
            }

            if (recipe.instructions.isNotEmpty()) {
                Spacer(modifier = Modifier.size(8.dp))
                
                TextButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.padding(start = 0.dp)
                ) {
                    Text(
                        text = if (expanded) "Hide instructions" else "View instructions",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                if (expanded) {
                    Column(
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        recipe.instructions.forEachIndexed { index, step ->
                            Text(
                                text = "${index + 1}. $step",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


