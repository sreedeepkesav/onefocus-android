package com.onefocus.app.feature.analytics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.onefocus.app.feature.analytics.DayData

@Composable
fun CalendarHeatmap(
    days: List<DayData>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "66-Day Journey",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(11),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.height(280.dp),
            userScrollEnabled = false
        ) {
            items(days) { day ->
                DayCell(day = day)
            }
        }
        
        HeatmapLegend()
    }
}

@Composable
fun DayCell(day: DayData) {
    val backgroundColor = when {
        day.isInFuture -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        day.isCompleted -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    
    val borderColor = if (day.isToday) {
        MaterialTheme.colorScheme.secondary
    } else {
        Color.Transparent
    }
    
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .then(
                if (day.isToday) {
                    Modifier.border(
                        width = 2.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(4.dp)
                    )
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    )
}

@Composable
fun HeatmapLegend() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp)
    ) {
        LegendItem(
            color = MaterialTheme.colorScheme.primary,
            label = "Completed"
        )
        LegendItem(
            color = MaterialTheme.colorScheme.surfaceVariant,
            label = "Missed"
        )
        LegendItem(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            label = "Future"
        )
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
