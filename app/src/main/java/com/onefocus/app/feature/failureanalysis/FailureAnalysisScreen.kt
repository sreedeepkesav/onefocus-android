package com.onefocus.app.feature.failureanalysis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onefocus.app.core.design.Yellow
import com.onefocus.app.feature.failureanalysis.components.CompletionHeatmap
import com.onefocus.app.feature.failureanalysis.components.WeekBreakdownCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FailureAnalysisScreen(
    viewModel: FailureAnalysisViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Learning from This Journey") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Yellow.copy(alpha = 0.2f)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Journey Complete",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "You completed ${state.journey?.completedDays?.size ?: 0}/66 days (${(state.completionRate * 100).toInt()}%)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            // Heatmap
            state.journey?.let { journey ->
                CompletionHeatmap(
                    completedDays = journey.completedDays,
                    startDate = journey.startDate,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Week breakdown
            if (state.weekData.isNotEmpty()) {
                WeekBreakdownCard(
                    weekData = state.weekData,
                    bestWeek = state.bestWeek,
                    worstWeek = state.worstWeek
                )
            }
            
            // Reflection questions
            Text(
                "Reflection",
                style = MaterialTheme.typography.titleMedium
            )
            
            OutlinedTextField(
                value = state.whatWorked,
                onValueChange = viewModel::updateWhatWorked,
                label = { Text("What worked well?") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            
            OutlinedTextField(
                value = state.whatDidnt,
                onValueChange = viewModel::updateWhatDidnt,
                label = { Text("What didn't work?") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            
            OutlinedTextField(
                value = state.nextTimeChanges,
                onValueChange = viewModel::updateNextTimeChanges,
                label = { Text("What will you change next time?") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            
            // Actions
            Button(
                onClick = { viewModel.saveAnalysisAndReset(onNavigateToHome) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Fresh Journey")
            }
        }
    }
}
