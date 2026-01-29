package com.onefocus.app.data.local

import androidx.room.TypeConverter
import com.onefocus.app.data.model.enums.HabitType
import com.onefocus.app.data.model.enums.TriggerType

class Converters {
    @TypeConverter
    fun fromHabitType(value: HabitType): String = value.name

    @TypeConverter
    fun toHabitType(value: String): HabitType = HabitType.valueOf(value)

    @TypeConverter
    fun fromTriggerType(value: TriggerType): String = value.name

    @TypeConverter
    fun toTriggerType(value: String): TriggerType = TriggerType.valueOf(value)

    @TypeConverter
    fun fromStringList(value: List<String>): String = value.joinToString(",")

    @TypeConverter
    fun toStringList(value: String): List<String> =
        if (value.isEmpty()) emptyList() else value.split(",")
}
