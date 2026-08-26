package com.example.gethealth.model

/**
 * Represents one recipe suggestion, whether freshly AI-generated or
 * previously saved by the user.
 *
 * This is fake/placeholder data for now — later, `Recipe` instances will
 * come from the AI API response (see MealPlanAiRepository) and from
 * Supabase (see MealPlanRepository), but every screen that displays a
 * recipe card only needs to know about this shape, not where it came from.
 */
data class Recipe(
    val id: String,
    val title: String,
    val description: String,
    val minutes: Int,
    val calories: Int,
    val proteinG: Int,
    val tags: List<String>
)

/**
 * A couple of fake recipes so the Meal Planner UI has something to show
 * before the AI/Supabase integration exists. Delete once real data flows
 * in — nothing else depends on this list existing.
 */
val fakeGeneratedRecipes = listOf(
    Recipe(
        id = "1",
        title = "Garlic Chicken Stir-Fry",
        description = "Quick protein-packed wok dish with crisp vegetables and a savory soy glaze.",
        minutes = 25,
        calories = 380,
        proteinG = 35,
        tags = listOf("High Protein", "Quick", "Low Carb")
    )
)
