package com.onefocus.app.feature.secondhabit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onefocus.app.core.util.TriggerConflictDetector
import com.onefocus.app.data.model.Habit
import com.onefocus.app.data.model.enums.HabitType
import com.onefocus.app.data.model.enums.TriggerType
import com.onefocus.app.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SecondHabitState(
    val currentStep: Int = 1,
    val habitType: HabitType = HabitType.SIMPLE,
    val habitName: String = "",
    val triggerType: TriggerType = TriggerType.ANCHOR,
    val trigger: String = "",
    val startValue: Int? = null,
    val targetValue: Int? = null,
    val dailyTarget: Int? = null,
    val timedMinutes: Int? = null,
    val conflictError: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class AddSecondHabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SecondHabitState())
    val state: StateFlow<SecondHabitState> = _state.asStateFlow()

    fun setHabitType(type: HabitType) {
        _state.update { it.copy(habitType = type) }
    }

    fun setHabitName(name: String) {
        _state.update { it.copy(habitName = name) }
    }

    fun setTriggerType(type: TriggerType) {
        _state.update { it.copy(triggerType = type) }
    }

    fun setTrigger(trigger: String) {
        _state.update { it.copy(trigger = trigger, conflictError = null) }
    }

    fun setStartValue(value: Int?) {
        _state.update { it.copy(startValue = value) }
    }

    fun setTargetValue(value: Int?) {
        _state.update { it.copy(targetValue = value) }
    }

    fun setDailyTarget(value: Int?) {
        _state.update { it.copy(dailyTarget = value) }
    }

    fun setTimedMinutes(value: Int?) {
        _state.update { it.copy(timedMinutes = value) }
    }

    fun nextStep() {
        _state.update { it.copy(currentStep = it.currentStep + 1) }
    }

    fun previousStep() {
        _state.update { it.copy(currentStep = maxOf(1, it.currentStep - 1)) }
    }

    suspend fun checkTriggerConflict(): Boolean {
        val currentState = _state.value
        val primaryHabit = habitRepository.getPrimaryHabit().first() ?: return false

        val newHabit = Habit(
            name = currentState.habitName,
            type = currentState.habitType,
            trigger = currentState.trigger,
            triggerType = currentState.triggerType,
            isSecondary = true
        )

        if (TriggerConflictDetector.hasConflict(primaryHabit, newHabit)) {
            _state.update {
                it.copy(conflictError = TriggerConflictDetector.getConflictMessage(primaryHabit))
            }
            return true
        }

        return false
    }

    fun createSecondHabit(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            // Check for conflicts again before creating
            if (checkTriggerConflict()) {
                _state.update { it.copy(isLoading = false) }
                return@launch
            }

            val currentState = _state.value
            val habit = Habit(
                name = currentState.habitName,
                type = currentState.habitType,
                trigger = currentState.trigger,
                triggerType = currentState.triggerType,
                startValue = currentState.startValue,
                targetValue = currentState.targetValue,
                dailyTarget = currentState.dailyTarget,
                timedMinutes = currentState.timedMinutes,
                isSecondary = true
            )

            habitRepository.insertHabit(habit)
            _state.update { it.copy(isLoading = false) }
            onSuccess()
        }
    }

    fun clearConflictError() {
        _state.update { it.copy(conflictError = null) }
    }
}
