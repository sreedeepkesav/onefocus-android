package com.onefocus.app.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onefocus.app.data.model.Habit
import com.onefocus.app.data.model.Journey
import com.onefocus.app.data.model.enums.HabitType
import com.onefocus.app.data.model.enums.TriggerType
import com.onefocus.app.data.repository.HabitRepository
import com.onefocus.app.data.repository.JourneyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingState(
    val habitType: HabitType? = null,
    val habitName: String = "",
    val triggerType: TriggerType = TriggerType.ANCHOR,
    val triggerText: String = "",
    val startValue: Int? = null,
    val targetValue: Int? = null,
    val dailyTarget: Int? = null,
    val timedMinutes: Int? = null,
    val isCreating: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val journeyRepository: JourneyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    fun setHabitType(type: HabitType) {
        _state.update { it.copy(habitType = type) }
    }

    fun setHabitName(name: String) {
        _state.update { it.copy(habitName = name) }
    }

    fun setTrigger(triggerType: TriggerType, triggerText: String) {
        _state.update {
            it.copy(
                triggerType = triggerType,
                triggerText = triggerText
            )
        }
    }

    fun setMetrics(
        startValue: Int? = null,
        targetValue: Int? = null,
        dailyTarget: Int? = null,
        timedMinutes: Int? = null
    ) {
        _state.update {
            it.copy(
                startValue = startValue,
                targetValue = targetValue,
                dailyTarget = dailyTarget,
                timedMinutes = timedMinutes
            )
        }
    }

    fun createHabitAndJourney(onComplete: () -> Unit) {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.habitType == null || currentState.habitName.isBlank()) {
                _state.update { it.copy(error = "Missing required fields") }
                return@launch
            }

            _state.update { it.copy(isCreating = true, error = null) }

            try {
                // Create the habit
                val habit = Habit(
                    name = currentState.habitName,
                    type = currentState.habitType,
                    trigger = currentState.triggerText,
                    triggerType = currentState.triggerType,
                    startValue = currentState.startValue,
                    targetValue = currentState.targetValue,
                    dailyTarget = currentState.dailyTarget ?: if (currentState.habitType == HabitType.REPEATING) 8 else null,
                    timedMinutes = currentState.timedMinutes,
                    isSecondary = false
                )

                habitRepository.insertHabit(habit)

                // Create the journey
                journeyRepository.createJourney()

                _state.update { it.copy(isCreating = false) }
                onComplete()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isCreating = false,
                        error = e.message ?: "Failed to create habit"
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
