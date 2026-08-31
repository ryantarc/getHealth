package com.example.gethealth.ui.screens.mealplanner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.rememberCoroutineScope
import com.example.gethealth.data.MealPlanAiRepository
import kotlinx.coroutines.launch
import com.example.gethealth.model.Recipe
import com.example.gethealth.ui.components.GetHealthTextField
import com.example.gethealth.ui.components.RecipeCard

/**
 * The AI Meal Planner screen — matches the "AI Meal Planner page" slide in
 * the design deck.
 *
 * Responsive layout: the ingredients form and the generated recipe results
 * all live inside one LazyVerticalGrid using GridCells.Adaptive. The form
 * fields span the full grid width on every screen size (using
 * GridItemSpan), while the recipe result cards reflow into multiple
 * columns automatically on a wide (tablet) screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlannerScreen(
    savedRecipeIds: Set<String>,
    onToggleSave: (Recipe) -> Unit,
    onViewSavedRecipes: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var ingredients by remember { mutableStateOf("") }
    var generatedRecipes by remember { mutableStateOf<List<Recipe>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 320.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
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
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
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

            item(span = { GridItemSpan(maxLineSpan) }) {
                Button(
                    onClick = {
                        if (ingredients.isNotBlank()) {
                            scope.launch {
                                isLoading = true
                                errorMessage = null
                                val result = MealPlanAiRepository.generateMealPlan(ingredients)
                                
                                if (result.isSuccess) {
                                    generatedRecipes = result.getOrNull() ?: emptyList()
                                } else {
                                    errorMessage = "Couldn't generate a recipe — check your connection and try again"
                                }
                                
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(if (isLoading) "Generating..." else "Generate Meal Plan")
                }
            }

            // Display an error message if the AI generation fails
            errorMessage?.let {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            if (generatedRecipes.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
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
