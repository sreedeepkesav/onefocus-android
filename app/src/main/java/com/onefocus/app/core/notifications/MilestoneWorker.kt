package com.onefocus.app.core.notifications

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.onefocus.app.data.repository.HabitRepository
import com.onefocus.app.data.repository.JourneyRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class MilestoneWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationHelper: NotificationHelper,
    private val habitRepository: HabitRepository,
    private val journeyRepository: JourneyRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val journey = journeyRepository.getJourney().first() ?: return Result.success()
            val habit = habitRepository.getPrimaryHabit().first() ?: return Result.success()

            val currentDay = journey.currentDay
            
            // Check if this is a milestone day
            if (currentDay in listOf(7, 14, 21, 28, 35, 42, 49, 56, 63, 66)) {
                notificationHelper.showMilestoneNotification(
                    day = currentDay,
                    habitName = habit.name
                )
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
