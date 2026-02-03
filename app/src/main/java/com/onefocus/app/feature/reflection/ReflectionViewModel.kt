package com.onefocus.app.feature.reflection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onefocus.app.data.model.Reflection
import com.onefocus.app.data.repository.JourneyRepository
import com.onefocus.app.data.repository.ReflectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReflectionState(
    val weekNumber: Int = 0,
    val whatHelped: String = "",
    val whatHindered: String = "",
    val patternsNoticed: String = "",
    val isSubmitting: Boolean = false,
    val isComplete: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ReflectionViewModel @Inject constructor(
    private val reflectionRepository: ReflectionRepository,
    private val journeyRepository: JourneyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ReflectionState())
    val state: StateFlow<ReflectionState> = _state.asStateFlow()

    init {
        loadWeekNumber()
    }

    private fun loadWeekNumber() {
        viewModelScope.launch {
            val journey = journeyRepository.getJourney().firstOrNull()
            journey?.let {
                val weekNumber = reflectionRepository.getCurrentWeekNumber(it)
                _state.value = _state.value.copy(weekNumber = weekNumber)
            }
        }
    }

    fun updateWhatHelped(text: String) {
        _state.value = _state.value.copy(whatHelped = text)
    }

    fun updateWhatHindered(text: String) {
        _state.value = _state.value.copy(whatHindered = text)
    }

    fun updatePatternsNoticed(text: String) {
        _state.value = _state.value.copy(patternsNoticed = text)
    }

    fun submitReflection() {
        viewModelScope.launch {
            val currentState = _state.value

            // Validate that at least one field is filled
            if (currentState.whatHelped.isBlank() &&
                currentState.whatHindered.isBlank() &&
                currentState.patternsNoticed.isBlank()
            ) {
                _state.value = currentState.copy(
                    errorMessage = "Please answer at least one question"
                )
                return@launch
            }

            _state.value = currentState.copy(isSubmitting = true, errorMessage = null)

            try {
                val reflection = Reflection(
                    weekNumber = currentState.weekNumber,
                    whatHelped = currentState.whatHelped,
                    whatHindered = currentState.whatHindered,
                    patternsNoticed = currentState.patternsNoticed
                )

                reflectionRepository.insertReflection(reflection)

                _state.value = currentState.copy(
                    isSubmitting = false,
                    isComplete = true
                )
            } catch (e: Exception) {
                _state.value = currentState.copy(
                    isSubmitting = false,
                    errorMessage = "Failed to save reflection: ${e.message}"
                )
            }
        }
    }

    fun skipReflection() {
        viewModelScope.launch {
            // Save an empty reflection to mark as skipped
            val reflection = Reflection(
                weekNumber = _state.value.weekNumber,
                whatHelped = "",
                whatHindered = "",
                patternsNoticed = ""
            )

            reflectionRepository.insertReflection(reflection)

            _state.value = _state.value.copy(isComplete = true)
        }
    }
}
