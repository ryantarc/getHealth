package com.example.gethealth.model

import kotlinx.serialization.Serializable

/**
 * Represents one recipe suggestion, whether freshly AI-generated or
 * previously saved by the user.
 *
 * This is fake/placeholder data for now — later, `Recipe` instances will
 * come from the AI API response (see MealPlanAiRepository) and from
 * Supabase (see MealPlanRepository), but every screen that displays a
 * recipe card only needs to know about this shape, not where it came from.
 */
@Serializable
data class Recipe(
    val id: String,
    val title: String,
    val description: String,
    val minutes: Int,
    val calories: Int,
    val proteinG: Int,
    val tags: List<String>,
    val instructions: List<String> = emptyList()
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
        tags = listOf("High Protein", "Quick", "Low Carb"),
        instructions = listOf(
            "Slice chicken breast into thin strips and toss with minced garlic.",
            "Heat a wok or large skillet with a splash of oil and sear chicken until browned.",
            "Add chopped broccoli, bell peppers, and snap peas, stir-frying for 3-4 minutes.",
            "Pour in soy sauce and a touch of honey, tossing until well coated and hot."
        )
    )
)
