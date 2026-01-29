package com.onefocus.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "repeating_logs")
data class RepeatingLog(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val habitId: String,
    val dateKey: String,  // "2024-01-26" 
    val count: Int = 1
)
