package com.onefocus.app.data.local.dao

import androidx.room.*
import com.onefocus.app.data.model.FailureAnalysis
import kotlinx.coroutines.flow.Flow

@Dao
interface FailureAnalysisDao {
    @Insert
    suspend fun insert(analysis: FailureAnalysis)
    
    @Query("SELECT * FROM failure_analyses ORDER BY createdAt DESC")
    fun getAllAnalyses(): Flow<List<FailureAnalysis>>
    
    @Query("SELECT * FROM failure_analyses WHERE journeyId = :journeyId")
    suspend fun getAnalysisForJourney(journeyId: String): FailureAnalysis?
}
