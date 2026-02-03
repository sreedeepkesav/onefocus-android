package com.onefocus.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "reflections")
data class Reflection(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val createdAt: Long = System.currentTimeMillis(),
    val weekNumber: Int,
    val whatHelped: String,
    val whatHindered: String,
    val patternsNoticed: String
)
