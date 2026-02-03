package com.onefocus.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onefocus.app.data.model.Habit
import com.onefocus.app.data.model.Journey
import com.onefocus.app.data.repository.HabitRepository
import com.onefocus.app.data.repository.JourneyRepository
import com.onefocus.app.data.repository.ReflectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val journey: Journey? = null,
    val primaryHabit: Habit? = null,
    val secondaryHabit: Habit? = null,
    val isCompletedToday: Boolean = false,
    val isLoading: Boolean = true,
    val canAddSecondHabit: Boolean = false,
    val isSecondHabitUnlocked: Boolean = false,
    val shouldShowReflectionPrompt: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val journeyRepository: JourneyRepository,
    private val reflectionRepository: ReflectionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                journeyRepository.getJourney(),
                habitRepository.getPrimaryHabit(),
                habitRepository.getSecondaryHabit()
            ) { journey, primaryHabit, secondaryHabit ->
                val isCompleted = journey?.isCompletedToday(0) ?: false
                val completedDays = journey?.completedDays?.size ?: 0
                val canAddSecond = completedDays >= 21 && secondaryHabit == null

                // Check if reflection is due
                val shouldShowReflection = reflectionRepository.isReflectionDue(journey) &&
                        !hasReflectionForCurrentWeek(journey)

                HomeState(
                    journey = journey,
                    primaryHabit = primaryHabit,
                    secondaryHabit = secondaryHabit,
                    isCompletedToday = isCompleted,
                    isLoading = false,
                    canAddSecondHabit = canAddSecond,
                    isSecondHabitUnlocked = completedDays >= 21,
                    shouldShowReflectionPrompt = shouldShowReflection
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    private suspend fun hasReflectionForCurrentWeek(journey: Journey?): Boolean {
        if (journey == null) return false
        val weekNumber = reflectionRepository.getCurrentWeekNumber(journey)
        return reflectionRepository.getReflectionForWeek(weekNumber) != null
    }

    fun quickComplete() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState.isCompletedToday) {
                return@launch
            }

            // Mark as complete in journey
            journeyRepository.markDayComplete(habitIndex = 0)
        }
    }
}
