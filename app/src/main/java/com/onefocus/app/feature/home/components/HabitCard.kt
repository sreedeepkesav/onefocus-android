package com.onefocus.app.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.onefocus.app.core.design.Purple80
import com.onefocus.app.data.model.Habit
import com.onefocus.app.data.model.enums.HabitType

@Composable
fun HabitCard(
    habit: Habit,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    val typeIcon = when (habit.type) {
        HabitType.BINARY -> "✓"
        HabitType.REPEATING -> "🔄"
        HabitType.INCREMENT -> "📈"
        HabitType.REDUCTION -> "📉"
        HabitType.TIMED -> "⏱️"
    }

    val habitDescription = buildString {
        append("Habit: ${habit.name}. ")
        append("Trigger: ${habit.triggerDisplayText}. ")
        append("Type: ${habit.type.name}. ")
        append(if (isCompleted) "Completed today" else "Not yet completed")
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(20.dp)
            .semantics { contentDescription = habitDescription }
    ) {
        // Header with type icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today's Focus",
                style = MaterialTheme.typography.labelMedium,
                color = Purple80,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.dp.value.sp
            )
            Text(
                text = typeIcon,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Habit name
        Text(
            text = habit.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Trigger text
        Text(
            text = habit.triggerDisplayText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Completion status
        if (isCompleted) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "✓",
                    style = MaterialTheme.typography.titleMedium,
                    color = Purple80
                )
                Text(
                    text = "Completed today",
                    style = MaterialTheme.typography.bodySmall,
                    color = Purple80,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private val Float.sp: androidx.compose.ui.unit.TextUnit
    @Composable get() = with(androidx.compose.ui.platform.LocalDensity.current) {
        this@sp.toSp()
    }
