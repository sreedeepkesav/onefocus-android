package com.onefocus.app.feature.failureanalysis.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.onefocus.app.core.design.Green
import com.onefocus.app.core.design.Yellow

data class WeekData(
    val weekNumber: Int,
    val completedDays: Int,
    val totalDays: Int = 7
) {
    val completionRate: Float get() = completedDays.toFloat() / totalDays
}

@Composable
fun WeekBreakdownCard(
    weekData: List<WeekData>,
    bestWeek: Int,
    worstWeek: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Week-by-Week Breakdown",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(250.dp)
            ) {
                items(weekData) { week ->
                    WeekRow(
                        week = week,
                        isBest = week.weekNumber == bestWeek,
                        isWorst = week.weekNumber == worstWeek
                    )
                }
            }
        }
    }
}

@Composable
private fun WeekRow(
    week: WeekData,
    isBest: Boolean,
    isWorst: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Week ${week.weekNumber}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.3f)
        )
        
        LinearProgressIndicator(
            progress = { week.completionRate },
            modifier = Modifier
                .weight(0.5f)
                .height(8.dp),
            color = when {
                isBest -> Green
                isWorst -> Yellow
                else -> MaterialTheme.colorScheme.primary
            }
        )
        
        Text(
            text = "${week.completedDays}/${week.totalDays}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(0.2f)
        )
    }
}
