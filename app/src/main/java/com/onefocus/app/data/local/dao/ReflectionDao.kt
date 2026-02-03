package com.onefocus.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.onefocus.app.data.model.Reflection
import kotlinx.coroutines.flow.Flow

@Dao
interface ReflectionDao {
    @Insert
    suspend fun insert(reflection: Reflection)

    @Query("SELECT * FROM reflections ORDER BY createdAt DESC")
    fun getAllReflections(): Flow<List<Reflection>>

    @Query("SELECT * FROM reflections WHERE weekNumber = :week LIMIT 1")
    suspend fun getReflectionForWeek(week: Int): Reflection?
}
