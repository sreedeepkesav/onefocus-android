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
        val journey = journeyDao.getJourneyOnce() ?: return
        val today = LocalDate.now().toString()
        val dateKey = if (habitIndex == 0) today else "${today}_2"

        if (!journey.completedDays.contains(dateKey)) {
            val updatedDays = journey.completedDays + dateKey
            val updated = journey.copy(completedDays = updatedDays)
            journeyDao.update(updated)
        }
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

    suspend fun resetJourney() {
        val newJourney = Journey(
            startDate = System.currentTimeMillis(),
            completedDays = emptyList(),
            flexDaysUsed = 0,
            totalFocusTimeSeconds = 0
        )
        journeyDao.insert(newJourney)
    }
}
