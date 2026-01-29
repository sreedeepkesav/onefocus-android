package com.onefocus.app.data.local.dao

import androidx.room.*
import com.onefocus.app.data.model.Journey
import kotlinx.coroutines.flow.Flow

@Dao
interface JourneyDao {
    @Query("SELECT * FROM journey LIMIT 1")
    fun getJourney(): Flow<Journey?>

    @Query("SELECT * FROM journey LIMIT 1")
    suspend fun getJourneyOnce(): Journey?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(journey: Journey)

    @Update
    suspend fun update(journey: Journey)

    @Query("UPDATE journey SET completedDays = :completedDays WHERE id = 'main_journey'")
    suspend fun updateCompletedDays(completedDays: List<String>)
}
