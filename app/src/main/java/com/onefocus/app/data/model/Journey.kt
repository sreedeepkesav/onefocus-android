package com.onefocus.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.onefocus.app.data.model.enums.JourneyStatus
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Entity(tableName = "journey")
data class Journey(
    @PrimaryKey val id: String = "main_journey",
    val startDate: Long = System.currentTimeMillis(),
    val completedDays: List<String> = emptyList(),
    val flexDaysUsed: Int = 0,
    val totalFocusTimeSeconds: Int = 0,
    val status: JourneyStatus = JourneyStatus.ACTIVE
) {
    val currentDay: Int
        get() {
            val start = LocalDate.ofEpochDay(startDate / 86400000)
            val today = LocalDate.now()
            return minOf(maxOf(ChronoUnit.DAYS.between(start, today).toInt() + 1, 1), 66)
        }

    val currentPhase: JourneyPhase
        get() = when {
            currentDay <= 21 -> JourneyPhase.FOUNDATION
            currentDay <= 42 -> JourneyPhase.STRENGTHENING
            else -> JourneyPhase.CEMENTING
        }

    val currentStreak: Int
        get() {
            var streak = 0
            var checkDate = LocalDate.now()
            while (streak < currentDay) {
                val dateStr = checkDate.toString()
                if (completedDays.any { it.startsWith(dateStr) }) {
                    streak++
                    checkDate = checkDate.minusDays(1)
                } else {
                    break
                }
            }
            return streak
        }

    val flexDaysRemaining: Int
        get() = 3 - flexDaysUsed

    val progress: Float
        get() = currentDay / 66f

    fun isCompletedToday(habitIndex: Int = 0): Boolean {
        val todayKey = todayKey(habitIndex)
        return completedDays.contains(todayKey)
    }

    private fun todayKey(habitIndex: Int): String {
        val today = LocalDate.now().toString()
        return if (habitIndex == 0) today else "${today}_2"
    }
}

enum class JourneyPhase(val label: String) {
    FOUNDATION("Building Foundation"),
    STRENGTHENING("Strengthening"),
    CEMENTING("Cementing Habit")
}
