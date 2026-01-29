package com.onefocus.app.feature.mood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onefocus.app.data.model.MoodEntry
import com.onefocus.app.data.repository.HabitRepository
import com.onefocus.app.data.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MoodState(
    val selectedMood: Int? = null,
    val notes: String = "",
    val habitId: String? = null,
    val isSaving: Boolean = false
)

@HiltViewModel
class MoodViewModel @Inject constructor(
    private val moodRepository: MoodRepository,
    private val habitRepository: HabitRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MoodState())
    val state: StateFlow<MoodState> = _state.asStateFlow()

    init {
        loadHabitId()
    }

    private fun loadHabitId() {
        viewModelScope.launch {
            habitRepository.getPrimaryHabit()
                .filterNotNull()
                .firstOrNull()
                ?.let { habit ->
                    _state.value = _state.value.copy(habitId = habit.id)
                }
        }
    }

    fun selectMood(mood: Int) {
        _state.value = _state.value.copy(selectedMood = mood)
    }

    fun updateNotes(notes: String) {
        _state.value = _state.value.copy(notes = notes)
    }

    fun saveMood(isBefore: Boolean, onComplete: () -> Unit) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState.selectedMood == null) {
                return@launch
            }

            _state.value = currentState.copy(isSaving = true)

            try {
                val moodEntry = MoodEntry(
                    mood = currentState.selectedMood,
                    habitId = currentState.habitId,
                    isBefore = isBefore
                )

                moodRepository.insertMoodEntry(moodEntry)
                _state.value = _state.value.copy(isSaving = false)
                onComplete()
            } catch (e: Exception) {
                _state.value = _state.value.copy(isSaving = false)
            }
        }
    }
}
