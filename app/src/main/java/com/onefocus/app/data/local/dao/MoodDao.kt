package com.onefocus.app.data.local.dao

import androidx.room.*
import com.onefocus.app.data.model.MoodEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    fun getAllMoodEntries(): Flow<List<MoodEntry>>

    @Query("SELECT * FROM mood_entries WHERE habitId = :habitId ORDER BY timestamp DESC")
    fun getMoodEntriesForHabit(habitId: String): Flow<List<MoodEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moodEntry: MoodEntry)

    @Delete
    suspend fun delete(moodEntry: MoodEntry)
}
