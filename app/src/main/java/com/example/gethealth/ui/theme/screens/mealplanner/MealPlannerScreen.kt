package com.example.gethealth.ui.screens.mealplanner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gethealth.model.Recipe
import com.example.gethealth.model.fakeGeneratedRecipes
import com.example.gethealth.ui.components.GetHealthTextField
import com.example.gethealth.ui.components.RecipeCard

/**
 * The AI Meal Planner screen — matches the "AI Meal Planner page" slide in
 * the design deck: an ingredients input box, a Generate button, and a list
 * of matching recipe results below it.
 *
 * State is intentionally simple for now (remember/mutableStateOf). The
 * "Generate" button currently just loads a fixed fake recipe after a short
 * delay-free placeholder — this is exactly where MealPlanAiRepository will
 * be plugged in later.
 *
 * `savedRecipeIds` / `onToggleSave` / `onViewSavedRecipes` are passed down
 * from MainScreen, which owns the actual list of saved recipes so that
 * MealPlannerScreen and SavedRecipesScreen both see the same saved state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlannerScreen(
    savedRecipeIds: Set<String>,
    onToggleSave: (Recipe) -> Unit,
    onViewSavedRecipes: () -> Unit
) {
    var ingredients by remember { mutableStateOf("") }
    var generatedRecipes by remember { mutableStateOf<List<Recipe>>(emptyList()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Meal Planner") },
                actions = {
                    TextButton(onClick = onViewSavedRecipes) {
                        Icon(
                            imageVector = Icons.Filled.Bookmark,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text("Saved ${savedRecipeIds.size}")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "What's in your kitchen?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "List your available ingredients and we'll find the best matching recipes for you.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                Column {
                    Text(
                        text = "Your Ingredients",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    GetHealthTextField(
                        value = ingredients,
                        onValueChange = { ingredients = it },
                        label = "e.g. chicken, rice, broccoli"
                    )
                    Text(
                        text = "Separate by commas, spaces, or new lines.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        // Placeholder "generation" — this is where
                        // MealPlanAiRepository.generateMealPlan(ingredients)
                        // will be called once the AI integration is wired up.
                        generatedRecipes = if (ingredients.isNotBlank()) fakeGeneratedRecipes else emptyList()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Generate Meal Plan")
                }
            }

            if (generatedRecipes.isNotEmpty()) {
                item {
                    Text(
                        text = "Found ${generatedRecipes.size} recipe matching your ingredients:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                items(generatedRecipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        isSaved = savedRecipeIds.contains(recipe.id),
                        onToggleSave = { onToggleSave(recipe) }
                    )
                }
            }
        }
    }
}
