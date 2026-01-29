package com.onefocus.app.data.repository

import com.onefocus.app.data.local.dao.JourneyDao
import com.onefocus.app.data.model.Journey
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JourneyRepository @Inject constructor(
    private val journeyDao: JourneyDao
) {
    fun getJourney(): Flow<Journey?> = journeyDao.getJourney()

    suspend fun createJourney() {
        val newJourney = Journey(
            startDate = System.currentTimeMillis()
        )
        journeyDao.insert(newJourney)
    }

    suspend fun updateJourney(journey: Journey) {
        journeyDao.update(journey)
    }

    suspend fun markDayComplete(habitIndex: Int = 0) {
        // This is simplified - in real implementation you'd get current journey first
        val today = LocalDate.now().toString()
        val dateKey = if (habitIndex == 0) today else "${today}_2"
        
        // Would need to fetch current journey, update completedDays, then save
        // For now this is a placeholder
    }

    suspend fun useFlexDay(journey: Journey) {
        val updated = journey.copy(flexDaysUsed = journey.flexDaysUsed + 1)
        journeyDao.update(updated)
    }

    suspend fun addFocusTime(journey: Journey, seconds: Int) {
        val updated = journey.copy(
            totalFocusTimeSeconds = journey.totalFocusTimeSeconds + seconds
        )
        journeyDao.update(updated)
    }
}
