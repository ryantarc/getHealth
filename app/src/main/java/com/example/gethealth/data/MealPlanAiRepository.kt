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

    private val json = Json { 
        ignoreUnknownKeys = true
        isLenient = true
    }

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

            val modelName = "openai/gpt-oss-20b"

            // Updated prompt with stricter requirements and instructions support
            val systemInstruction = """
                You are a nutrition assistant for a health app. A user has these ingredients available at home: "$ingredients".

                Requirements:
                - Use MOSTLY the listed ingredients. You may assume basic pantry staples (salt, pepper, oil, water) are available, but do not introduce other major ingredients not listed. // Ensures the user can actually make the recipe
                - The recipe must be realistic and actually cookable by a home cook with no special equipment. // Accessibility for all users
                - Keep it healthy: prioritize lean protein, vegetables, and balanced portions over fried or heavily processed preparations. // Core app value proposition
                - Instructions must be concise: 4-6 short numbered steps, each one sentence. // Readability on mobile devices
                - Calories and protein must be realistic estimates for a single serving, not placeholder round numbers. // Nutritional accuracy

                First, check if the input contains real food ingredients. 
                If the input has NO real ingredients (gibberish, random words, non-food items), respond with exactly this JSON and nothing else:
                {"error": "No valid ingredients found. Please list real food items."}

                Respond ONLY with valid JSON in exactly this format. 
                CRITICAL: Use ONLY single quotes or escaped quotes (\") inside strings to avoid breaking the JSON structure.
                {
                  "title": "string",
                  "description": "one sentence, under 20 words",
                  "minutes": number,
                  "calories": number,
                  "proteinG": number,
                  "tags": ["2 to 4 short tags like 'High Protein', 'Quick', 'Low Carb'"],
                  "instructions": ["step 1", "step 2", "step 3"]
                }
            """.trimIndent()

            val requestBody = GrokRequest(
                model = modelName,
                messages = listOf(
                    Message(role = "system", content = systemInstruction),
                    Message(role = "user", content = "Ingredients: $ingredients")
                ),
                temperature = 0.6, // Balanced creativity and reliability
                max_tokens = 800 // Ensure response isn't truncated mid-JSON
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
            val rawResponse = response.choices.firstOrNull()?.message?.content ?: ""
            
            // LAYER 3: Robust JSON Extraction
            // Find the first '{' and last '}' to ignore any conversational filler the AI might add
            val startIndex = rawResponse.indexOf('{')
            val endIndex = rawResponse.lastIndexOf('}')
            
            if (startIndex == -1 || endIndex == -1 || endIndex < startIndex) {
                throw Exception("AI output did not contain a valid JSON block")
            }
            
            val cleanedResponse = rawResponse.substring(startIndex, endIndex + 1).trim()

            if (cleanedResponse.isBlank()) throw Exception("AI returned empty JSON")

            // Parse the JSON string into our temporary AI response model
            val aiRecipe = json.decodeFromString<AiRecipeResponse>(cleanedResponse)
            
            // LAYER 2 VALIDATION: Check if the AI returned a semantic error
            if (aiRecipe.error != null) {
                return Result.failure(Exception(aiRecipe.error))
            }
            
            // Convert to our app's official Recipe model and add a unique ID
            val recipe = Recipe(
                id = UUID.randomUUID().toString(),
                title = aiRecipe.title ?: "Healthy Recipe",
                description = aiRecipe.description ?: "",
                minutes = aiRecipe.minutes ?: 20,
                calories = aiRecipe.calories ?: 300,
                proteinG = aiRecipe.proteinG ?: 20,
                tags = aiRecipe.tags ?: emptyList(),
                instructions = aiRecipe.instructions
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
    val temperature: Double,
    val max_tokens: Int? = null
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
 * Fields are optional to handle the error case safely.
 */
@Serializable
private data class AiRecipeResponse(
    val title: String? = null,
    val description: String? = null,
    val minutes: Int? = null,
    val calories: Int? = null,
    val proteinG: Int? = null,
    val tags: List<String>? = null,
    val instructions: List<String> = emptyList(),
    val error: String? = null
)
