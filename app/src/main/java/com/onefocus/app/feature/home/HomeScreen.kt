package com.onefocus.app.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onefocus.app.core.design.Purple80
import com.onefocus.app.feature.home.components.HabitCard
import com.onefocus.app.feature.home.components.JourneyCard

@Composable
fun HomeScreen(
    onNavigateToMood: () -> Unit,
    onNavigateToAddSecondHabit: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToReflection: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Purple80
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 60.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header with action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "OneFocus",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = onNavigateToAnalytics) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = "View Analytics",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Journey card
                state.journey?.let { journey ->
                    JourneyCard(journey = journey)
                }

                // Reflection prompt card (when due)
                if (state.shouldShowReflectionPrompt) {
                    ReflectionPromptCard(
                        weekNumber = state.journey?.currentDay?.div(7) ?: 0,
                        onClick = onNavigateToReflection
                    )
                }

                // Habit card
                state.primaryHabit?.let { habit ->
                    HabitCard(
                        habit = habit,
                        isCompleted = state.isCompletedToday
                    )
                }

                // Second habit card (if exists)
                state.secondaryHabit?.let { habit ->
                    HabitCard(
                        habit = habit,
                        isCompleted = state.journey?.isCompletedToday(1) ?: false
                    )
                }

                // Add second habit card (if unlocked and not yet added)
                if (state.canAddSecondHabit) {
                    SecondHabitCard(onClick = onNavigateToAddSecondHabit)
                }
            }

            // Floating Action Button
            if (!state.isCompletedToday) {
                FloatingActionButton(
                    onClick = onNavigateToMood,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp)
                        .semantics {
                            stateDescription = "Start focus mode to complete today's habit"
                        },
                    containerColor = Purple80,
                    contentColor = MaterialTheme.colorScheme.surface
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Complete habit",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ReflectionPromptCard(
    weekNumber: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Purple80.copy(alpha = 0.15f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "✨ Week $weekNumber Reflection",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Purple80
                )
                Text(
                    text = "Take 2 minutes to reflect on your progress",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Icon(
                imageVector = Icons.Default.BarChart,
                contentDescription = "Start reflection",
                tint = Purple80,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun SecondHabitCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = Purple80
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎯",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Add Your Second Habit",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Unlocked at Day 21!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
