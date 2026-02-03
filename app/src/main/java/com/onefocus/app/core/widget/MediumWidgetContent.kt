package com.onefocus.app.core.widget

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.onefocus.app.MainActivity

@Composable
fun MediumWidgetContent(data: WidgetData) {
    val backgroundColor = Color(0xFF1C1B1F)
    val primaryColor = Color(0xFFD0BCFF)
    val onSurfaceColor = Color(0xFFE6E1E5)
    val surfaceVariant = Color(0xFF49454F)
    
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(backgroundColor)
            .cornerRadius(16.dp)
            .padding(16.dp)
            .clickable(
                actionStartActivity(
                    Intent(
                        androidx.glance.appwidget.GlanceAppWidgetManager::class.java.`package`.name,
                        MainActivity::class.java
                    )
                )
            )
    ) {
        Row(
            modifier = GlanceModifier.fillMaxSize(),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left section - Habit info
            Column(
                modifier = GlanceModifier.defaultWeight(),
                verticalAlignment = Alignment.Vertical.CenterVertically
            ) {
                Text(
                    text = data.habitName,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorProvider(onSurfaceColor)
                    ),
                    maxLines = 2
                )
                
                Spacer(modifier = GlanceModifier.height(8.dp))
                
                Text(
                    text = if (data.isCompletedToday) "✓ Completed Today" else "Complete Today",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = ColorProvider(
                            if (data.isCompletedToday) Color(0xFF4CAF50) else Color(0xFFFF9800)
                        )
                    )
                )
            }
            
            Spacer(modifier = GlanceModifier.width(16.dp))
            
            // Right section - Stats
            Column(
                modifier = GlanceModifier.width(100.dp),
                horizontalAlignment = Alignment.End
            ) {
                StatBox(
                    label = "Day",
                    value = "${data.currentDay}/66",
                    primaryColor = primaryColor,
                    surfaceVariant = surfaceVariant,
                    onSurfaceColor = onSurfaceColor
                )
                
                Spacer(modifier = GlanceModifier.height(8.dp))
                
                StatBox(
                    label = "Streak",
                    value = "${data.currentStreak}",
                    primaryColor = primaryColor,
                    surfaceVariant = surfaceVariant,
                    onSurfaceColor = onSurfaceColor
                )
            }
        }
    }
}

@Composable
private fun StatBox(
    label: String,
    value: String,
    primaryColor: Color,
    surfaceVariant: Color,
    onSurfaceColor: Color
) {
    Box(
        modifier = GlanceModifier
            .fillMaxWidth()
            .background(surfaceVariant)
            .cornerRadius(8.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(primaryColor)
                )
            )
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 10.sp,
                    color = ColorProvider(onSurfaceColor.copy(alpha = 0.7f))
                )
            )
        }
    }
}
