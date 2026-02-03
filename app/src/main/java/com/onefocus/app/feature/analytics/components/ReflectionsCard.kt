package com.onefocus.app.feature.analytics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.onefocus.app.core.design.Purple80
import com.onefocus.app.data.model.Reflection
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReflectionsCard(
    reflections: List<Reflection>,
    modifier: Modifier = Modifier
) {
    if (reflections.isEmpty()) return

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Purple80.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✨",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Column {
                    Text(
                        text = "Weekly Reflections",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${reflections.size} reflection${if (reflections.size > 1) "s" else ""} recorded",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Divider(color = MaterialTheme.colorScheme.outlineVariant)

            // Reflections list
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                reflections.forEach { reflection ->
                    ReflectionItem(reflection = reflection)
                }
            }
        }
    }
}

@Composable
private fun ReflectionItem(
    reflection: Reflection,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(reflection.createdAt))

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Week badge and date
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Purple80.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "Week ${reflection.weekNumber}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Purple80,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Reflection content
        if (reflection.whatHelped.isNotBlank()) {
            ReflectionAnswer(
                question = "What helped",
                answer = reflection.whatHelped
            )
        }
        if (reflection.whatHindered.isNotBlank()) {
            ReflectionAnswer(
                question = "What hindered",
                answer = reflection.whatHindered
            )
        }
        if (reflection.patternsNoticed.isNotBlank()) {
            ReflectionAnswer(
                question = "Patterns noticed",
                answer = reflection.patternsNoticed
            )
        }
    }
}

@Composable
private fun ReflectionAnswer(
    question: String,
    answer: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = answer,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
