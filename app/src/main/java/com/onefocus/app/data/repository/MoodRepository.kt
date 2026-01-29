package com.onefocus.app.data.repository

import com.onefocus.app.data.local.dao.MoodDao
import com.onefocus.app.data.model.MoodEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoodRepository @Inject constructor(
    private val moodDao: MoodDao
) {
    fun getAllMoodEntries(): Flow<List<MoodEntry>> = moodDao.getAllMoodEntries()

    fun getMoodEntriesForHabit(habitId: String): Flow<List<MoodEntry>> =
        moodDao.getMoodEntriesForHabit(habitId)

    suspend fun insertMoodEntry(moodEntry: MoodEntry) {
        moodDao.insert(moodEntry)
    }

    suspend fun deleteMoodEntry(moodEntry: MoodEntry) {
        moodDao.delete(moodEntry)
    }
}
