package com.example.gethealth.data

import com.example.gethealth.model.Recipe
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.Serializable

/**
 * Repository responsible for managing saved recipes in Supabase.
 */
object MealPlanRepository {

    /**
     * Fetches all recipes saved by a specific user.
     */
    suspend fun getSavedRecipes(userEmail: String): List<Recipe> {
        return try {
            println("DEBUG_DB: Fetching recipes for $userEmail")
            
            val saved = SupabaseClient.client.from("SavedRecipes")
                .select(columns = Columns.ALL) {
                    filter {
                        eq("user_email", userEmail)
                    }
                }
                .decodeList<SavedRecipeEntry>()
            
            println("DEBUG_DB: Found ${saved.size} recipes in database")
            
            saved.map { entry ->
                Recipe(
                    id = entry.recipe_id,
                    title = entry.title,
                    description = entry.description,
                    minutes = entry.minutes,
                    calories = entry.calories,
                    proteinG = entry.protein_g,
                    tags = entry.tags,
                    instructions = entry.instructions
                )
            }
        } catch (e: Exception) {
            println("DEBUG_DB_ERROR: Failed to fetch recipes: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Saves a recipe to the 'SavedRecipes' table.
     */
    suspend fun saveRecipe(userEmail: String, recipe: Recipe) {
        try {
            println("DEBUG_DB: Saving recipe ${recipe.title} for $userEmail")
            
            val entry = SavedRecipeEntry(
                user_email = userEmail,
                recipe_id = recipe.id,
                title = recipe.title,
                description = recipe.description,
                minutes = recipe.minutes,
                calories = recipe.calories,
                protein_g = recipe.proteinG,
                tags = recipe.tags,
                instructions = recipe.instructions
            )
            
            SupabaseClient.client.from("SavedRecipes").insert(entry)
            println("DEBUG_DB: Recipe '${recipe.title}' saved successfully for $userEmail")
        } catch (e: Exception) {
            println("DEBUG_DB_ERROR: Failed to save recipe '${recipe.title}': ${e.localizedMessage}")
            e.printStackTrace()
        }
    }

    /**
     * Removes a recipe from the user's saved list.
     */
    suspend fun unsaveRecipe(userEmail: String, recipeId: String) {
        try {
            println("DEBUG_DB: Removing recipe $recipeId for $userEmail")
            
            SupabaseClient.client.from("SavedRecipes").delete {
                filter {
                    eq("user_email", userEmail)
                    eq("recipe_id", recipeId)
                }
            }
            println("DEBUG_DB: Recipe removed successfully")
        } catch (e: Exception) {
            println("DEBUG_DB_ERROR: Failed to remove recipe: ${e.message}")
            e.printStackTrace()
        }
    }
}

/**
 * Internal data class that matches the Supabase 'SavedRecipes' table columns.
 */
@Serializable
private data class SavedRecipeEntry(
    val id: Int? = null,
    val user_email: String,
    val recipe_id: String,
    val title: String,
    val description: String,
    val minutes: Int,
    val calories: Int,
    val protein_g: Int,
    val tags: List<String>,
    val instructions: List<String> = emptyList()
)
