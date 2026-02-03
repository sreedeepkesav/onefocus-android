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
class ReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationHelper: NotificationHelper,
    private val habitRepository: HabitRepository,
    private val journeyRepository: JourneyRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val journey = journeyRepository.getJourney().first() ?: return Result.success()
            
            // Don't send reminder if already completed today
            if (journey.isCompletedToday()) {
                return Result.success()
            }

            val habit = habitRepository.getPrimaryHabit().first() ?: return Result.success()
            
            // Check current streak for streak protection
            if (journey.currentStreak > 0) {
                notificationHelper.showStreakProtectionNotification(
                    habitName = habit.name,
                    streakDays = journey.currentStreak
                )
            } else {
                notificationHelper.showDailyReminder(habit.name)
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
