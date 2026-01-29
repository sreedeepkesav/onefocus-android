package com.onefocus.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val mood: Int,  // 1-5
    val habitId: String? = null,
    val isBefore: Boolean = true
)
