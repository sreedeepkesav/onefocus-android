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
fun SmallWidgetContent(data: WidgetData) {
    val backgroundColor = Color(0xFF1C1B1F)
    val primaryColor = Color(0xFFD0BCFF)
    val onSurfaceColor = Color(0xFFE6E1E5)
    
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(backgroundColor)
            .cornerRadius(16.dp)
            .padding(12.dp)
            .clickable(
                actionStartActivity(
                    Intent(
                        androidx.glance.appwidget.GlanceAppWidgetManager::class.java.`package`.name,
                        MainActivity::class.java
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = data.habitName,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(onSurfaceColor)
                ),
                maxLines = 1
            )
            
            Spacer(modifier = GlanceModifier.height(8.dp))
            
            Text(
                text = "Day ${data.currentDay}/66",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(primaryColor)
                )
            )
            
            Spacer(modifier = GlanceModifier.height(4.dp))
            
            Text(
                text = if (data.isCompletedToday) "✓ Done" else "Pending",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = ColorProvider(
                        if (data.isCompletedToday) Color(0xFF4CAF50) else Color(0xFFFF9800)
                    )
                )
            )
            
            Spacer(modifier = GlanceModifier.height(8.dp))
            
            Text(
                text = "${data.currentStreak} day streak",
                style = TextStyle(
                    fontSize = 11.sp,
                    color = ColorProvider(onSurfaceColor.copy(alpha = 0.7f))
                )
            )
        }
    }
}
