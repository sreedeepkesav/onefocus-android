package com.onefocus.app.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "onefocus_preferences")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val NOTIFICATION_HOUR = intPreferencesKey("notification_hour")
        val NOTIFICATION_MINUTE = intPreferencesKey("notification_minute")
        val MILESTONE_NOTIFICATIONS = booleanPreferencesKey("milestone_notifications")
        val STREAK_PROTECTION_NOTIFICATIONS = booleanPreferencesKey("streak_protection_notifications")
        val WIDGET_ENABLED = booleanPreferencesKey("widget_enabled")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    val notificationsEnabled: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[NOTIFICATIONS_ENABLED] ?: true }

    val notificationHour: Flow<Int> = dataStore.data
        .map { preferences -> preferences[NOTIFICATION_HOUR] ?: 20 }

    val notificationMinute: Flow<Int> = dataStore.data
        .map { preferences -> preferences[NOTIFICATION_MINUTE] ?: 0 }

    val milestoneNotifications: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[MILESTONE_NOTIFICATIONS] ?: true }

    val streakProtectionNotifications: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[STREAK_PROTECTION_NOTIFICATIONS] ?: true }

    val widgetEnabled: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[WIDGET_ENABLED] ?: false }

    val themeMode: Flow<String> = dataStore.data
        .map { preferences -> preferences[THEME_MODE] ?: "system" }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setNotificationTime(hour: Int, minute: Int) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_HOUR] = hour
            preferences[NOTIFICATION_MINUTE] = minute
        }
    }

    suspend fun setMilestoneNotifications(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[MILESTONE_NOTIFICATIONS] = enabled
        }
    }

    suspend fun setStreakProtectionNotifications(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[STREAK_PROTECTION_NOTIFICATIONS] = enabled
        }
    }

    suspend fun setWidgetEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[WIDGET_ENABLED] = enabled
        }
    }

    suspend fun setThemeMode(mode: String) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode
        }
    }
}
