package com.onefocus.app.feature.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onefocus.app.data.model.Journey
import com.onefocus.app.data.model.MoodEntry
import com.onefocus.app.data.repository.JourneyRepository
import com.onefocus.app.data.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DayData(
    val date: LocalDate,
    val isCompleted: Boolean,
    val isToday: Boolean,
    val isInFuture: Boolean
)

data class MoodCorrelation(
    val averageBeforeMood: Float,
    val averageAfterMood: Float,
    val improvement: Float
)

data class AnalyticsState(
    val journey: Journey? = null,
    val completionRate: Float = 0f,
    val bestStreak: Int = 0,
    val currentStreak: Int = 0,
    val totalFocusTime: String = "0h 0m",
    val heatmapDays: List<DayData> = emptyList(),
    val moodCorrelation: MoodCorrelation? = null
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val journeyRepository: JourneyRepository,
    private val moodRepository: MoodRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AnalyticsState())
    val state: StateFlow<AnalyticsState> = _state.asStateFlow()

    init {
        loadAnalytics()
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            combine(
                journeyRepository.getJourney(),
                moodRepository.getAllMoodEntries()
            ) { journey, moodEntries ->
                journey to moodEntries
            }.collect { (journey, moodEntries) ->
                journey?.let {
                    _state.value = AnalyticsState(
                        journey = it,
                        completionRate = calculateCompletionRate(it),
                        bestStreak = calculateBestStreak(it),
                        currentStreak = it.currentStreak,
                        totalFocusTime = formatFocusTime(it.totalFocusTimeSeconds),
                        heatmapDays = generateHeatmapDays(it),
                        moodCorrelation = calculateMoodCorrelation(moodEntries)
                    )
                }
            }
        }
    }

    private fun calculateCompletionRate(journey: Journey): Float {
        val currentDay = journey.currentDay
        if (currentDay == 0) return 0f
        return (journey.completedDays.size.toFloat() / currentDay) * 100f
    }

    private fun calculateBestStreak(journey: Journey): Int {
        var bestStreak = 0
        var currentStreak = 0
        
        val startDate = LocalDate.ofEpochDay(journey.startDate / 86400000)
        val today = LocalDate.now()
        
        var checkDate = startDate
        while (checkDate <= today && checkDate <= startDate.plusDays(65)) {
            val dateStr = checkDate.toString()
            if (journey.completedDays.any { it.startsWith(dateStr) }) {
                currentStreak++
                bestStreak = maxOf(bestStreak, currentStreak)
            } else {
                currentStreak = 0
            }
            checkDate = checkDate.plusDays(1)
        }
        
        return bestStreak
    }

    private fun formatFocusTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        return if (hours > 0) {
            "${hours}h ${minutes}m"
        } else {
            "${minutes}m"
        }
    }

    private fun generateHeatmapDays(journey: Journey): List<DayData> {
        val days = mutableListOf<DayData>()
        val startDate = LocalDate.ofEpochDay(journey.startDate / 86400000)
        val today = LocalDate.now()
        
        for (i in 0 until 66) {
            val date = startDate.plusDays(i.toLong())
            val dateStr = date.toString()
            days.add(
                DayData(
                    date = date,
                    isCompleted = journey.completedDays.any { it.startsWith(dateStr) },
                    isToday = date == today,
                    isInFuture = date > today
                )
            )
        }
        
        return days
    }

    private fun calculateMoodCorrelation(moodEntries: List<MoodEntry>): MoodCorrelation? {
        if (moodEntries.isEmpty()) return null
        
        val beforeMoods = moodEntries.filter { !it.isAfter }.map { it.moodLevel }
        val afterMoods = moodEntries.filter { it.isAfter }.map { it.moodLevel }
        
        if (beforeMoods.isEmpty() || afterMoods.isEmpty()) return null
        
        val avgBefore = beforeMoods.average().toFloat()
        val avgAfter = afterMoods.average().toFloat()
        val improvement = ((avgAfter - avgBefore) / avgBefore) * 100f
        
        return MoodCorrelation(
            averageBeforeMood = avgBefore,
            averageAfterMood = avgAfter,
            improvement = improvement
        )
    }
}
