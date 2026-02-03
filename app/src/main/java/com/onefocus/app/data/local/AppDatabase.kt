package com.onefocus.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.onefocus.app.data.local.dao.*
import com.onefocus.app.data.model.*

@Database(
    entities = [
        Habit::class,
        Journey::class,
        MoodEntry::class,
        RepeatingLog::class,
        Reflection::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun journeyDao(): JourneyDao
    abstract fun moodDao(): MoodDao
    abstract fun repeatingLogDao(): RepeatingLogDao
    abstract fun reflectionDao(): ReflectionDao
}
