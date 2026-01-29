package com.onefocus.app.feature.mood

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.onefocus.app.core.design.*

@Composable
fun MoodAfterScreen(
    onContinue: () -> Unit,
    viewModel: MoodViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 60.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "How do you feel now?",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Notice any changes after your practice",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Mood selector
            MoodSelector(
                selectedMood = state.selectedMood,
                onMoodSelected = { viewModel.selectMood(it) }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Continue button
            Button(
                onClick = { viewModel.saveMood(isBefore = false, onComplete = onContinue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = state.selectedMood != null && !state.isSaving,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple80,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Continue",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun MoodSelector(
    selectedMood: Int?,
    onMoodSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val moods = listOf(
        1 to "😢",
        2 to "😕",
        3 to "😐",
        4 to "🙂",
        5 to "😄"
    )

    val moodLabels = listOf(
        "Terrible",
        "Bad",
        "Okay",
        "Good",
        "Excellent"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            moods.forEach { (value, emoji) ->
                MoodButton(
                    emoji = emoji,
                    isSelected = selectedMood == value,
                    onClick = { onMoodSelected(value) },
                    contentDescription = "Mood ${moodLabels[value - 1]}"
                )
            }
        }

        // Show selected mood label
        if (selectedMood != null) {
            Text(
                text = moodLabels[selectedMood - 1],
                style = MaterialTheme.typography.titleMedium,
                color = Purple80,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun MoodButton(
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) Purple80.copy(alpha = 0.2f)
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .semantics {
                stateDescription = if (isSelected) "$contentDescription, selected" else contentDescription
            }
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
