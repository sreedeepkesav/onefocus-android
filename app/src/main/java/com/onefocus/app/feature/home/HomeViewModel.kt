package com.onefocus.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onefocus.app.data.model.Habit
import com.onefocus.app.data.model.Journey
import com.onefocus.app.data.repository.HabitRepository
import com.onefocus.app.data.repository.JourneyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val journey: Journey? = null,
    val primaryHabit: Habit? = null,
    val isCompletedToday: Boolean = false,
    val isLoading: Boolean = true,
    val canAddSecondHabit: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val journeyRepository: JourneyRepository
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
                habitRepository.getPrimaryHabit()
            ) { journey, habit ->
                val isCompleted = journey?.isCompletedToday(0) ?: false
                val canAddSecond = (journey?.currentDay ?: 0) >= 21

                HomeState(
                    journey = journey,
                    primaryHabit = habit,
                    isCompletedToday = isCompleted,
                    isLoading = false,
                    canAddSecondHabit = canAddSecond
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
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
