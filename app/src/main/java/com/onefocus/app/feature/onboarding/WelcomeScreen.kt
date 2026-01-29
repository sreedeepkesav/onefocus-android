package com.onefocus.app.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.onefocus.app.core.design.*

@Composable
fun WelcomeScreen(
    onBeginJourney: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Surface,
                        Purple80.copy(alpha = 0.05f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Icon
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Purple80, Cyan)
                        ),
                        shape = RoundedCornerShape(32.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "◯",
                    style = MaterialTheme.typography.displayMedium,
                    color = Surface
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "OneFocus",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "One habit. 66 days. Life changed.",
                style = MaterialTheme.typography.bodyLarge,
                color = OnSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Science Cards
            ScienceCard(
                icon = "📊",
                iconBackground = Purple40,
                title = "66 days to form a habit",
                description = "Research shows it takes 66 days on average to make a behavior automatic.",
                stat = "66 days"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ScienceCard(
                icon = "🎯",
                iconBackground = Yellow.copy(alpha = 0.15f),
                title = "Trigger-based success",
                description = "Habits anchored to existing routines are 3x more likely to stick.",
                stat = "3x success"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ScienceCard(
                icon = "✨",
                iconBackground = GreenContainer,
                title = "One focus works",
                description = "Building one habit at a time increases your completion rate by 80%.",
                stat = "80% boost"
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Begin Button
            Button(
                onClick = onBeginJourney,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple80,
                    contentColor = Purple40
                )
            ) {
                Text(
                    text = "Begin Your Journey",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun ScienceCard(
    icon: String,
    iconBackground: androidx.compose.ui.graphics.Color,
    title: String,
    description: String,
    stat: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceContainer
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBackground, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stat,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Purple80
                )
            }
        }
    }
}
