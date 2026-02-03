package com.onefocus.app.data.repository

import com.onefocus.app.data.local.dao.FailureAnalysisDao
import com.onefocus.app.data.model.FailureAnalysis
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FailureAnalysisRepository @Inject constructor(
    private val failureAnalysisDao: FailureAnalysisDao
) {
    fun getAllAnalyses(): Flow<List<FailureAnalysis>> = 
        failureAnalysisDao.getAllAnalyses()
    
    suspend fun getAnalysisForJourney(journeyId: String): FailureAnalysis? =
        failureAnalysisDao.getAnalysisForJourney(journeyId)
    
    suspend fun saveAnalysis(analysis: FailureAnalysis) {
        failureAnalysisDao.insert(analysis)
    }
}
