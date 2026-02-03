package com.onefocus.app.data.repository

import com.onefocus.app.data.local.dao.ReflectionDao
import com.onefocus.app.data.model.Journey
import com.onefocus.app.data.model.Reflection
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReflectionRepository @Inject constructor(
    private val reflectionDao: ReflectionDao
) {
    fun getAllReflections(): Flow<List<Reflection>> = reflectionDao.getAllReflections()

    suspend fun getReflectionForWeek(week: Int): Reflection? {
        return reflectionDao.getReflectionForWeek(week)
    }

    suspend fun insertReflection(reflection: Reflection) {
        reflectionDao.insert(reflection)
    }

    /**
     * Check if reflection is due based on journey day
     * Reflections trigger on Day 7, 14, 21, 28, 35, 42, 49, 56, 63
     */
    fun isReflectionDue(journey: Journey?): Boolean {
        if (journey == null) return false
        val currentDay = journey.currentDay
        val weekNumber = currentDay / 7

        // Only show on exact week boundaries (7, 14, 21, etc.)
        return currentDay > 0 && currentDay % 7 == 0 && currentDay <= 66
    }

    /**
     * Get the current week number based on journey day
     */
    fun getCurrentWeekNumber(journey: Journey?): Int {
        if (journey == null) return 0
        return journey.currentDay / 7
    }
}
