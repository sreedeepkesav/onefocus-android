package com.onefocus.app.data.local.dao

import androidx.room.*
import com.onefocus.app.data.model.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE isSecondary = 0 LIMIT 1")
    fun getPrimaryHabit(): Flow<Habit?>

    @Query("SELECT * FROM habits WHERE isSecondary = 1 LIMIT 1")
    fun getSecondaryHabit(): Flow<Habit?>

    @Query("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Query("DELETE FROM habits")
    suspend fun deleteAll()
}
