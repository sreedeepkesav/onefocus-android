package com.onefocus.app.feature.focus

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.onefocus.app.core.design.Indigo
import com.onefocus.app.core.design.Purple80

@Composable
fun BreathingCircle(
    phase: BreathingPhase,
    secondsRemaining: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Check for Reduce Motion accessibility setting
    val settings = remember {
        android.provider.Settings.Global.getString(
            context.contentResolver,
            android.provider.Settings.Global.TRANSITION_ANIMATION_SCALE
        )?.toFloatOrNull() ?: 1f
    }
    val shouldReduceMotion = settings == 0f

    // Calculate target scale based on phase
    val targetScale = when (phase) {
        BreathingPhase.BREATHE_IN -> 1.3f
        BreathingPhase.HOLD -> 1.3f
        BreathingPhase.BREATHE_OUT -> 0.7f
        BreathingPhase.COMPLETE -> 1.0f
    }

    // Animate scale if motion is enabled
    val scale by animateFloatAsState(
        targetValue = if (shouldReduceMotion) 1.0f else targetScale,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "breathing_scale"
    )

    // Glow effect during hold phase
    val glowAlpha by animateFloatAsState(
        targetValue = if (phase == BreathingPhase.HOLD && !shouldReduceMotion) 0.6f else 0.3f,
        animationSpec = tween(durationMillis = 800),
        label = "glow_alpha"
    )

    val phaseDescription = when (phase) {
        BreathingPhase.BREATHE_IN -> "Breathing in, $secondsRemaining seconds remaining"
        BreathingPhase.HOLD -> "Holding breath, $secondsRemaining seconds remaining"
        BreathingPhase.BREATHE_OUT -> "Breathing out, $secondsRemaining seconds remaining"
        BreathingPhase.COMPLETE -> "Breathing exercise complete"
    }

    Box(
        modifier = modifier
            .size(280.dp)
            .semantics { contentDescription = phaseDescription },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(280.dp)
        ) {
            // Outer glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Purple80.copy(alpha = glowAlpha),
                        Color.Transparent
                    )
                ),
                radius = size.minDimension / 2 * scale * 1.2f
            )

            // Main breathing circle
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Purple80,
                        Indigo
                    ),
                    center = center
                ),
                radius = size.minDimension / 2 * scale
            )

            // Inner highlight
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.Transparent
                    ),
                    center = center
                ),
                radius = size.minDimension / 3 * scale
            )
        }
    }
}
