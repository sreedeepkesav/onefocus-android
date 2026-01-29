package com.onefocus.app.feature.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onefocus.app.core.design.Purple80

@Composable
fun FocusScreen(
    onComplete: () -> Unit,
    viewModel: FocusViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.currentPhase) {
        if (state.currentPhase == BreathingPhase.COMPLETE) {
            onComplete()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Cycle counter
            Text(
                text = "Cycle ${state.cycleCount + 1} of ${state.totalCycles}",
                style = MaterialTheme.typography.labelLarge,
                color = Purple80,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Breathing circle
            BreathingCircle(
                phase = state.currentPhase,
                secondsRemaining = state.secondsRemaining
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Phase text
            Text(
                text = viewModel.getPhaseText(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Seconds remaining
            Text(
                text = "${state.secondsRemaining}",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = Purple80
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Skip button
            TextButton(
                onClick = { viewModel.skip() },
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "Skip",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
