package com.example.gethealth.data

import com.example.gethealth.BuildConfig
import com.example.gethealth.model.Recipe
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.UUID

/**
 * Repository responsible for generating healthy meal plans using Grok AI (xAI).
 * 
 * What it is: A singleton object that acts as the data source for AI features.
 * Why we need it: It handles the direct HTTP communication with Grok's API
 *                and parses the response into our Recipe models.
 */
object MealPlanAiRepository {

    // Setup the HTTP client with JSON support
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Builds a prompt for Grok, sends it, and parses the resulting JSON into a Recipe list.
     * 
     * @param ingredients A comma-separated list of ingredients the user has.
     * @return A Result containing a list of recipes (usually one) on success, or an error.
     */
    suspend fun generateMealPlan(ingredients: String): Result<List<Recipe>> {
        return try {
            val apiKey = BuildConfig.GROK_API_KEY.trim()
            
            // AUTOMATIC FIX: If the key starts with 'gsk_', it's a GROQ key, not GROK.
            // We adjust the endpoint and model accordingly.
            val isGroq = apiKey.startsWith("gsk_")
            val url = if (isGroq) {
                "https://api.groq.com/openai/v1/chat/completions"
            } else {
                "https://api.x.ai/v1/chat/completions"
            }
            
            val modelName = if (isGroq) "llama-3.1-70b-versatile" else "grok-beta"

            // Strict instructions to ensure the AI returns ONLY valid JSON matching our model.
            val systemInstruction = "You are a professional nutritionist and chef. " +
                    "Generate ONE realistic healthy recipe based on the ingredients provided. " +
                    "Respond ONLY with a raw JSON object. Do not include markdown code blocks. " +
                    "JSON Fields: title (string), description (string), minutes (int), calories (int), proteinG (int), tags (list of strings)."

            val requestBody = GrokRequest(
                model = modelName,
                messages = listOf(
                    Message(role = "system", content = systemInstruction),
                    Message(role = "user", content = "Ingredients: $ingredients")
                ),
                temperature = 0.7
            )

            // Make the request and get the raw response first
            val httpResponse = client.post(url) {
                header("Authorization", "Bearer ${apiKey.trim()}")
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            val responseBody = httpResponse.body<String>()
            println("DEBUG_AI: Status: ${httpResponse.status}, Body: $responseBody")

            if (httpResponse.status.value !in 200..299) {
                throw Exception("AI API Error: ${httpResponse.status}. Body: $responseBody")
            }

            val response: GrokResponse = json.decodeFromString(responseBody)
            var responseText = response.choices.firstOrNull()?.message?.content ?: ""
            
            // Cleanup markdown code blocks if the AI included them (e.g. ```json ... ```)
            responseText = responseText.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            if (responseText.isBlank()) throw Exception("AI returned empty text")
            
            // Parse the JSON string into our temporary AI response model
            val aiRecipe = json.decodeFromString<AiRecipeResponse>(responseText)
            
            // Convert to our app's official Recipe model and add a unique ID
            val recipe = Recipe(
                id = UUID.randomUUID().toString(),
                title = aiRecipe.title,
                description = aiRecipe.description,
                minutes = aiRecipe.minutes,
                calories = aiRecipe.calories,
                proteinG = aiRecipe.proteinG,
                tags = aiRecipe.tags
            )
            
            Result.success(listOf(recipe))
        } catch (e: Exception) {
            // Log the error and return a failure so the app doesn't crash
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

// Support models for the Grok (OpenAI-compatible) API structure

@Serializable
private data class GrokRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double
)

@Serializable
private data class Message(
    val role: String,
    val content: String
)

@Serializable
private data class GrokResponse(
    val choices: List<Choice>
)

@Serializable
private data class Choice(
    val message: Message
)

/**
 * A temporary internal data class used only for parsing the AI's specific JSON structure.
 */
@Serializable
private data class AiRecipeResponse(
    val title: String,
    val description: String,
    val minutes: Int,
    val calories: Int,
    val proteinG: Int,
    val tags: List<String>
)
