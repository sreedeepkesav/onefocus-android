package com.onefocus.app.data.repository

import com.onefocus.app.data.local.dao.HabitDao
import com.onefocus.app.data.local.dao.RepeatingLogDao
import com.onefocus.app.data.model.Habit
import com.onefocus.app.data.model.RepeatingLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val habitDao: HabitDao,
    private val repeatingLogDao: RepeatingLogDao
) {
    fun getPrimaryHabit(): Flow<Habit?> = habitDao.getPrimaryHabit()

    fun getSecondaryHabit(): Flow<Habit?> = habitDao.getSecondaryHabit()

    fun getAllHabits(): Flow<List<Habit>> = habitDao.getAllHabits()

    suspend fun insertHabit(habit: Habit) {
        habitDao.insert(habit)
    }

    suspend fun updateHabit(habit: Habit) {
        habitDao.update(habit)
    }

    suspend fun deleteHabit(habit: Habit) {
        habitDao.delete(habit)
        // Delete associated repeating logs
        repeatingLogDao.deleteAllForHabit(habit.id)
    }

    suspend fun deleteAllHabits() {
        habitDao.deleteAll()
    }

    // Repeating habit log methods
    fun getRepeatingLogForToday(habitId: String): Flow<RepeatingLog?> {
        val dateKey = LocalDate.now().toString()
        return repeatingLogDao.getLogForDate(habitId, dateKey)
    }

    fun getAllLogsForHabit(habitId: String): Flow<List<RepeatingLog>> {
        return repeatingLogDao.getAllLogsForHabit(habitId)
    }

    suspend fun logRepeatingHabit(habitId: String) {
        val dateKey = LocalDate.now().toString()
        val existingLog = repeatingLogDao.getLogForDate(habitId, dateKey)
        
        // This is a simplified version - in real implementation you'd collect the Flow first
        val newLog = RepeatingLog(
            habitId = habitId,
            dateKey = dateKey,
            count = 1 // Will be updated by incrementing
        )
        repeatingLogDao.insert(newLog)
    }

    suspend fun incrementRepeatingLog(log: RepeatingLog) {
        repeatingLogDao.update(log.copy(count = log.count + 1))
    }
}
