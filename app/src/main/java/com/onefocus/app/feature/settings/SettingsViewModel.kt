package com.onefocus.app.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onefocus.app.core.preferences.PreferencesManager
import com.onefocus.app.data.repository.JourneyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(
    val notificationsEnabled: Boolean = true,
    val notificationHour: Int = 20,
    val notificationMinute: Int = 0,
    val milestoneNotifications: Boolean = true,
    val streakProtectionNotifications: Boolean = true,
    val showResetDialog: Boolean = false,
    val showTimePickerDialog: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val journeyRepository: JourneyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            combine(
                preferencesManager.notificationsEnabled,
                preferencesManager.notificationHour,
                preferencesManager.notificationMinute,
                preferencesManager.milestoneNotifications,
                preferencesManager.streakProtectionNotifications
            ) { enabled, hour, minute, milestone, streakProtection ->
                SettingsState(
                    notificationsEnabled = enabled,
                    notificationHour = hour,
                    notificationMinute = minute,
                    milestoneNotifications = milestone,
                    streakProtectionNotifications = streakProtection
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setNotificationsEnabled(enabled)
        }
    }

    fun toggleMilestoneNotifications(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setMilestoneNotifications(enabled)
        }
    }

    fun toggleStreakProtectionNotifications(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setStreakProtectionNotifications(enabled)
        }
    }

    fun showTimePicker() {
        _state.update { it.copy(showTimePickerDialog = true) }
    }

    fun hideTimePicker() {
        _state.update { it.copy(showTimePickerDialog = false) }
    }

    fun updateNotificationTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            preferencesManager.setNotificationTime(hour, minute)
            hideTimePicker()
        }
    }

    fun showResetDialog() {
        _state.update { it.copy(showResetDialog = true) }
    }

    fun hideResetDialog() {
        _state.update { it.copy(showResetDialog = false) }
    }

    fun resetJourney() {
        viewModelScope.launch {
            journeyRepository.resetJourney()
            hideResetDialog()
        }
    }

    fun getFormattedTime(): String {
        val hour = _state.value.notificationHour
        val minute = _state.value.notificationMinute
        val period = if (hour >= 12) "PM" else "AM"
        val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        return String.format("%d:%02d %s", displayHour, minute, period)
    }
}
