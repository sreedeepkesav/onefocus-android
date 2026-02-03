package com.onefocus.app.core.widget

import android.content.Context
import com.onefocus.app.data.local.AppDatabase
import kotlinx.coroutines.flow.first

data class WidgetData(
    val habitName: String = "",
    val currentDay: Int = 1,
    val currentStreak: Int = 0,
    val isCompletedToday: Boolean = false,
    val progress: Float = 0f
)

class WidgetDataProvider(private val context: Context) {
    
    suspend fun getWidgetData(): WidgetData {
        val database = AppDatabase.getInstance(context)
        val journey = database.journeyDao().getJourney().first()
        val habit = database.habitDao().getPrimaryHabit().first()
        
        return if (journey != null && habit != null) {
            WidgetData(
                habitName = habit.name,
                currentDay = journey.currentDay,
                currentStreak = journey.currentStreak,
                isCompletedToday = journey.isCompletedToday(),
                progress = journey.progress
            )
        } else {
            WidgetData()
        }
    }
}
