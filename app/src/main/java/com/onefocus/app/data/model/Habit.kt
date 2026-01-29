package com.onefocus.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.onefocus.app.data.model.enums.HabitType
import com.onefocus.app.data.model.enums.TriggerType
import java.util.UUID

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: HabitType,
    val trigger: String,
    val triggerType: TriggerType = TriggerType.ANCHOR,
    val startValue: Int? = null,
    val targetValue: Int? = null,
    val currentValue: Int? = null,
    val dailyTarget: Int? = null,
    val timedMinutes: Int? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isSecondary: Boolean = false
) {
    val triggerDisplayText: String
        get() = when (triggerType) {
            TriggerType.THROUGHOUT -> "Throughout the day"
            TriggerType.CONTEXT -> trigger
            TriggerType.ANCHOR -> "After I $trigger"
        }
}
