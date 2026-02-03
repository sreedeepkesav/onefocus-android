package com.onefocus.app.core.notifications

import android.content.Context
import androidx.work.*
import com.onefocus.app.core.preferences.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager
) {
    companion object {
        const val REMINDER_WORK_NAME = "daily_reminder_work"
    }

    suspend fun scheduleDailyReminder() {
        val notificationsEnabled = preferencesManager.notificationsEnabled.first()
        if (!notificationsEnabled) {
            cancelDailyReminder()
            return
        }

        val hour = preferencesManager.notificationHour.first()
        val minute = preferencesManager.notificationMinute.first()

        // Calculate initial delay to the next scheduled time
        val currentTime = System.currentTimeMillis()
        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, hour)
            set(java.util.Calendar.MINUTE, minute)
            set(java.util.Calendar.SECOND, 0)
            
            // If time has passed today, schedule for tomorrow
            if (timeInMillis <= currentTime) {
                add(java.util.Calendar.DAY_OF_YEAR, 1)
            }
        }
        
        val initialDelay = calendar.timeInMillis - currentTime

        val dailyWorkRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            dailyWorkRequest
        )
    }

    fun cancelDailyReminder() {
        WorkManager.getInstance(context).cancelUniqueWork(REMINDER_WORK_NAME)
    }

    suspend fun checkAndScheduleMilestoneNotification() {
        val milestoneEnabled = preferencesManager.milestoneNotifications.first()
        if (!milestoneEnabled) return

        val milestoneWorkRequest = OneTimeWorkRequestBuilder<MilestoneWorker>()
            .build()

        WorkManager.getInstance(context).enqueue(milestoneWorkRequest)
    }
}
