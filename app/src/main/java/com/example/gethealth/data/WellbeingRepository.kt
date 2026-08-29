package com.example.gethealth.data

import android.util.Log
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import com.example.gethealth.model.MoodEntry
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import java.time.LocalDate

/**
 * Repository for Wellbeing data.
 * Includes a 'Draft' state to ensure mood entries persist during screen rotations
 * or tab switches without needing to edit the Main Navigation.
 */
object WellbeingRepository {
    private const val TABLE_MOODS = "moodentries"
    private const val TAG = "WellbeingRepository"

    // CENTRAL DRAFT STATE: Survives rotation and tab switches perfectly
    val draftMood = mutableStateOf<String?>(null)
    val draftNote = mutableStateOf("")
    val draftDateEpoch = mutableLongStateOf(LocalDate.now().toEpochDay())
    val draftMessage = mutableStateOf<String?>(null)

    fun resetDraft() {
        draftMood.value = null
        draftNote.value = ""
        draftDateEpoch.longValue = LocalDate.now().toEpochDay()
        draftMessage.value = null
    }

    suspend fun addMoodEntry(entry: MoodEntry) {
        try {
            SupabaseClient.client.from(TABLE_MOODS).insert(entry)
        } catch (e: Exception) {
            Log.e(TAG, "Error adding mood entry: ${e.message}")
            throw e
        }
    }

    suspend fun getMoodHistory(email: String): List<MoodEntry> {
        return try {
            val results = SupabaseClient.client.from(TABLE_MOODS)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("email", email)
                    }
                }
                .decodeList<MoodEntry>()
            
            // Sort by Date (desc) and then ID (desc) for most recent at top
            results.sortedWith(
                compareByDescending<MoodEntry> { it.date }
                    .thenByDescending { it.id ?: 0L },
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching mood history: ${e.message}")
            emptyList()
        }
    }

    suspend fun updateMoodEntry(entry: MoodEntry) {
        try {
            SupabaseClient.client.from(TABLE_MOODS).update(entry) {
                filter {
                    eq("id", entry.id ?: -1L)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating mood entry: ${e.message}")
            throw e
        }
    }

    suspend fun deleteMoodEntry(id: Long) {
        try {
            SupabaseClient.client.from(TABLE_MOODS).delete {
                filter {
                    eq("id", id)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting mood entry: ${e.message}")
            throw e
        }
    }
}
