package com.onefocus.app.feature.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onefocus.app.feature.analytics.components.CalendarHeatmap
import com.onefocus.app.feature.analytics.components.MoodCorrelationCard
import com.onefocus.app.feature.analytics.components.StatsCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Insights") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Stats Card
            StatsCard(
                completionRate = state.completionRate,
                bestStreak = state.bestStreak,
                currentStreak = state.currentStreak,
                totalFocusTime = state.totalFocusTime
            )

            // Calendar Heatmap
            CalendarHeatmap(days = state.heatmapDays)

            // Mood Correlation Card (only if mood data exists)
            state.moodCorrelation?.let { moodCorrelation ->
                MoodCorrelationCard(moodCorrelation = moodCorrelation)
            }
        }
    }
}
