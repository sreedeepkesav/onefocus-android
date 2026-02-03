package com.onefocus.app.feature.failureanalysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onefocus.app.data.model.FailureAnalysis
import com.onefocus.app.data.model.Journey
import com.onefocus.app.data.model.enums.JourneyStatus
import com.onefocus.app.data.repository.FailureAnalysisRepository
import com.onefocus.app.data.repository.JourneyRepository
import com.onefocus.app.feature.failureanalysis.components.WeekData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FailureAnalysisState(
    val journey: Journey? = null,
    val whatWorked: String = "",
    val whatDidnt: String = "",
    val nextTimeChanges: String = "",
    val weekData: List<WeekData> = emptyList(),
    val bestWeek: Int = 1,
    val worstWeek: Int = 1,
    val completionRate: Float = 0f,
    val isLoading: Boolean = false
)

@HiltViewModel
class FailureAnalysisViewModel @Inject constructor(
    private val journeyRepository: JourneyRepository,
    private val failureAnalysisRepository: FailureAnalysisRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(FailureAnalysisState())
    val state: StateFlow<FailureAnalysisState> = _state.asStateFlow()
    
    init {
        loadJourneyData()
    }
    
    private fun loadJourneyData() {
        viewModelScope.launch {
            journeyRepository.getMainJourney().collect { journey ->
                journey?.let {
                    val weekData = calculateWeeklyBreakdown(it)
                    val best = weekData.maxByOrNull { w -> w.completionRate }?.weekNumber ?: 1
                    val worst = weekData.minByOrNull { w -> w.completionRate }?.weekNumber ?: 1
                    val rate = it.completedDays.size.toFloat() / 66f
                    
                    _state.value = _state.value.copy(
                        journey = it,
                        weekData = weekData,
                        bestWeek = best,
                        worstWeek = worst,
                        completionRate = rate
                    )
                }
            }
        }
    }
    
    private fun calculateWeeklyBreakdown(journey: Journey): List<WeekData> {
        val weeks = mutableListOf<WeekData>()
        for (week in 1..9) {
            val startDay = (week - 1) * 7 + 1
            val endDay = minOf(week * 7, 66)
            val daysInWeek = journey.completedDays.filter { dateString ->
                val dayNumber = calculateDayNumber(journey.startDate, parseDate(dateString))
                dayNumber in startDay..endDay
            }.size
            weeks.add(WeekData(week, daysInWeek, endDay - startDay + 1))
        }
        return weeks
    }
    
    private fun calculateDayNumber(startDate: Long, date: Long): Int {
        return ((date - startDate) / 86400000).toInt() + 1
    }
    
    private fun parseDate(dateString: String): Long {
        return java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            .parse(dateString)?.time ?: 0L
    }
    
    fun updateWhatWorked(text: String) {
        _state.value = _state.value.copy(whatWorked = text)
    }
    
    fun updateWhatDidnt(text: String) {
        _state.value = _state.value.copy(whatDidnt = text)
    }
    
    fun updateNextTimeChanges(text: String) {
        _state.value = _state.value.copy(nextTimeChanges = text)
    }
    
    fun saveAnalysisAndReset(onComplete: () -> Unit) {
        viewModelScope.launch {
            val journey = _state.value.journey ?: return@launch
            
            // Save analysis
            val analysis = FailureAnalysis(
                journeyId = journey.id,
                whatWorked = _state.value.whatWorked,
                whatDidnt = _state.value.whatDidnt,
                nextTimeChanges = _state.value.nextTimeChanges,
                completionRate = _state.value.completionRate,
                bestWeek = _state.value.bestWeek,
                worstWeek = _state.value.worstWeek,
                completedDaysSnapshot = journey.completedDays.joinToString(",")
            )
            failureAnalysisRepository.saveAnalysis(analysis)
            
            // Mark journey as failed
            journeyRepository.updateJourney(
                journey.copy(status = JourneyStatus.FAILED)
            )
            
            // Create new journey
            val newJourney = Journey(
                startDate = System.currentTimeMillis(),
                completedDays = emptyList(),
                graceDaysUsed = 0,
                totalFocusTimeMinutes = 0,
                status = JourneyStatus.ACTIVE
            )
            journeyRepository.insertJourney(newJourney)
            
            onComplete()
        }
    }
}
