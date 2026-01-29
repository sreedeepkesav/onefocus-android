package com.onefocus.app.feature.onboarding

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.onefocus.app.core.design.*
import com.onefocus.app.data.model.enums.TriggerType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriggerRepeatingScreen(
    habitName: String,
    onBackClick: () -> Unit,
    onContinue: (triggerType: TriggerType, triggerText: String) -> Unit
) {
    var selectedTriggerType by remember { mutableStateOf(TriggerType.THROUGHOUT) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface
                )
            )
        },
        containerColor = Surface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "STEP 2 OF 3",
                    style = MaterialTheme.typography.labelMedium,
                    color = Purple80,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "When will you $habitName?",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Optional - you can skip this step",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Trigger Type Options
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TriggerTypeOption(
                    type = TriggerType.THROUGHOUT,
                    isSelected = selectedTriggerType == TriggerType.THROUGHOUT,
                    isRecommended = true,
                    onClick = { selectedTriggerType = TriggerType.THROUGHOUT }
                )

                TriggerTypeOption(
                    type = TriggerType.CONTEXT,
                    isSelected = selectedTriggerType == TriggerType.CONTEXT,
                    isRecommended = false,
                    onClick = { selectedTriggerType = TriggerType.CONTEXT }
                )

                TriggerTypeOption(
                    type = TriggerType.ANCHOR,
                    isSelected = selectedTriggerType == TriggerType.ANCHOR,
                    isRecommended = false,
                    onClick = { selectedTriggerType = TriggerType.ANCHOR }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Continue Button
            Button(
                onClick = {
                    onContinue(selectedTriggerType, "")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple80,
                    contentColor = Purple40
                )
            ) {
                Text(
                    text = "Continue",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun TriggerTypeOption(
    type: TriggerType,
    isSelected: Boolean,
    isRecommended: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(
                if (isSelected) {
                    Modifier.border(2.dp, Purple80, RoundedCornerShape(16.dp))
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Purple80.copy(alpha = 0.08f) else SurfaceContainer
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Radio button
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) Purple80 else Outline,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Purple80, CircleShape)
                    )
                }
            }

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = type.label,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if (isRecommended) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = GreenContainer
                        ) {
                            Text(
                                text = "RECOMMENDED",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Green
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = type.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant
                )
            }
        }
    }
}
