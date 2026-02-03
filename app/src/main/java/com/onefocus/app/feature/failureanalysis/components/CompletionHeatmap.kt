package com.onefocus.app.feature.failureanalysis.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.onefocus.app.core.design.Green
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CompletionHeatmap(
    completedDays: List<String>,
    totalDays: Int = 66,
    startDate: Long,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "66-Day Journey Heatmap",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.height(300.dp)
        ) {
            items(totalDays) { dayIndex ->
                val dayNumber = dayIndex + 1
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dayDate = Date(startDate + (dayIndex * 86400000L))
                val dateString = dateFormat.format(dayDate)
                
                val color = when {
                    completedDays.contains(dateString) -> Green
                    dayNumber <= totalDays -> Color.Gray.copy(alpha = 0.3f)
                    else -> Color.DarkGray.copy(alpha = 0.2f)
                }
                
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(color, RoundedCornerShape(4.dp))
                )
            }
        }
        
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            LegendItem(color = Green, label = "Completed")
            LegendItem(color = Color.Gray.copy(alpha = 0.3f), label = "Missed")
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color, RoundedCornerShape(4.dp))
        )
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}
