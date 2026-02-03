package com.onefocus.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "failure_analyses")
data class FailureAnalysis(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val journeyId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val whatWorked: String,
    val whatDidnt: String,
    val nextTimeChanges: String,
    val completionRate: Float,
    val bestWeek: Int,
    val worstWeek: Int,
    val completedDaysSnapshot: String  // JSON serialized list
)
