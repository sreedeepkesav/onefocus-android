package com.onefocus.app.core.navigation

sealed class Destination(val route: String) {
    // Onboarding
    data object Welcome : Destination("welcome")
    data object HabitType : Destination("habit_type")
    data object HabitName : Destination("habit_name")
    data object TriggerSetup : Destination("trigger_setup")
    data object TriggerRepeating : Destination("trigger_repeating")
    data object Ready : Destination("ready")
    
    // Main App
    data object Home : Destination("home")
    data object MoodBefore : Destination("mood_before")
    data object MoodAfter : Destination("mood_after")
    data object AddSecondHabit : Destination("add_second_habit")
    
    // Phase 3 - New Destinations
    data object Analytics : Destination("analytics")
    data object Settings : Destination("settings")
}
