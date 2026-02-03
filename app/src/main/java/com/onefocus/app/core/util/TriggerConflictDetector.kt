package com.onefocus.app.core.util

import com.onefocus.app.data.model.Habit
import com.onefocus.app.data.model.enums.TriggerType

object TriggerConflictDetector {
    
    fun hasConflict(existingHabit: Habit, newHabit: Habit): Boolean {
        // If both are "throughout the day", they conflict
        if (existingHabit.triggerType == TriggerType.THROUGHOUT && 
            newHabit.triggerType == TriggerType.THROUGHOUT) {
            return true
        }
        
        // If both have the same anchor trigger, they conflict
        if (existingHabit.triggerType == TriggerType.ANCHOR && 
            newHabit.triggerType == TriggerType.ANCHOR &&
            existingHabit.trigger.lowercase() == newHabit.trigger.lowercase()) {
            return true
        }
        
        // If both have the same context trigger, they might conflict
        if (existingHabit.triggerType == TriggerType.CONTEXT && 
            newHabit.triggerType == TriggerType.CONTEXT &&
            existingHabit.trigger.lowercase() == newHabit.trigger.lowercase()) {
            return true
        }
        
        return false
    }
    
    fun getConflictMessage(existingHabit: Habit): String {
        return when (existingHabit.triggerType) {
            TriggerType.THROUGHOUT -> "You already have a habit for 'Throughout the day'"
            TriggerType.ANCHOR -> "You already have a habit with the same anchor: ${existingHabit.trigger}"
            TriggerType.CONTEXT -> "You already have a habit in the same context: ${existingHabit.trigger}"
        }
    }
}
