package com.onefocus.app.feature.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onefocus.app.data.repository.JourneyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class BreathingPhase {
    BREATHE_IN,
    HOLD,
    BREATHE_OUT,
    COMPLETE
}

data class FocusState(
    val currentPhase: BreathingPhase = BreathingPhase.BREATHE_IN,
    val cycleCount: Int = 0,
    val totalCycles: Int = 3,
    val secondsRemaining: Int = 4,
    val isActive: Boolean = true
)

@HiltViewModel
class FocusViewModel @Inject constructor(
    private val journeyRepository: JourneyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FocusState())
    val state: StateFlow<FocusState> = _state.asStateFlow()

    init {
        startBreathingSession()
    }

    private fun startBreathingSession() {
        viewModelScope.launch {
            var currentState = _state.value

            while (currentState.cycleCount < currentState.totalCycles && currentState.isActive) {
                // Breathe In - 4 seconds
                currentState = currentState.copy(
                    currentPhase = BreathingPhase.BREATHE_IN,
                    secondsRemaining = 4
                )
                _state.value = currentState
                countdown(4)

                if (!_state.value.isActive) break

                // Hold - 7 seconds
                currentState = _state.value.copy(
                    currentPhase = BreathingPhase.HOLD,
                    secondsRemaining = 7
                )
                _state.value = currentState
                countdown(7)

                if (!_state.value.isActive) break

                // Breathe Out - 8 seconds
                currentState = _state.value.copy(
                    currentPhase = BreathingPhase.BREATHE_OUT,
                    secondsRemaining = 8
                )
                _state.value = currentState
                countdown(8)

                if (!_state.value.isActive) break

                // Increment cycle
                currentState = _state.value.copy(
                    cycleCount = currentState.cycleCount + 1
                )
                _state.value = currentState
            }

            // Mark as complete
            if (_state.value.isActive) {
                _state.value = _state.value.copy(
                    currentPhase = BreathingPhase.COMPLETE
                )
                // Add focus time (4+7+8 = 19 seconds per cycle * 3 cycles = 57 seconds)
                val journey = journeyRepository.getJourney().firstOrNull()
                journey?.let {
                    journeyRepository.addFocusTime(it, 57)
                }
            }
        }
    }

    private suspend fun countdown(seconds: Int) {
        for (i in seconds downTo 1) {
            if (!_state.value.isActive) break
            _state.value = _state.value.copy(secondsRemaining = i)
            delay(1000)
        }
    }

    fun skip() {
        _state.value = _state.value.copy(
            isActive = false,
            currentPhase = BreathingPhase.COMPLETE
        )
    }

    fun getPhaseText(): String {
        return when (_state.value.currentPhase) {
            BreathingPhase.BREATHE_IN -> "Breathe In"
            BreathingPhase.HOLD -> "Hold"
            BreathingPhase.BREATHE_OUT -> "Breathe Out"
            BreathingPhase.COMPLETE -> "Complete"
        }
    }
}
