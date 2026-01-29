package com.onefocus.app.data.local.dao

import androidx.room.*
import com.onefocus.app.data.model.RepeatingLog
import kotlinx.coroutines.flow.Flow

@Dao
interface RepeatingLogDao {
    @Query("SELECT * FROM repeating_logs WHERE habitId = :habitId AND dateKey = :dateKey")
    fun getLogForDate(habitId: String, dateKey: String): Flow<RepeatingLog?>

    @Query("SELECT * FROM repeating_logs WHERE habitId = :habitId ORDER BY dateKey DESC")
    fun getAllLogsForHabit(habitId: String): Flow<List<RepeatingLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: RepeatingLog)

    @Update
    suspend fun update(log: RepeatingLog)

    @Delete
    suspend fun delete(log: RepeatingLog)

    @Query("DELETE FROM repeating_logs WHERE habitId = :habitId")
    suspend fun deleteAllForHabit(habitId: String)
}
