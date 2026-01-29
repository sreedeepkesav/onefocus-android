package com.onefocus.app.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
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
import com.onefocus.app.data.model.Journey

@Composable
fun JourneyCard(
    journey: Journey,
    modifier: Modifier = Modifier
) {
    val progressDescription = "Journey progress: Day ${journey.currentDay} of 66, ${(journey.progress * 100).toInt()}% complete"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(20.dp)
            .semantics { contentDescription = progressDescription }
    ) {
        // Phase label
        Text(
            text = journey.currentPhase.label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Purple80,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.dp.value.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Day counter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Day ${journey.currentDay}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "/ 66",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress bar
        LinearProgressIndicator(
            progress = { journey.progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Purple80,
            trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Stats row
        StatsRow(
            streak = journey.currentStreak,
            flexDaysRemaining = journey.flexDaysRemaining
        )
    }
}

private val Int.sp: androidx.compose.ui.unit.TextUnit
    @Composable get() = with(androidx.compose.ui.platform.LocalDensity.current) {
        this@sp.toSp()
    }
