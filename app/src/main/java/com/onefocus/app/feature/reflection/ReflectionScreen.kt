package com.onefocus.app.feature.reflection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onefocus.app.core.design.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReflectionScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReflectionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Navigate back when complete
    LaunchedEffect(state.isComplete) {
        if (state.isComplete) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Week ${state.weekNumber} Reflection") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Take 2 minutes to reflect",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Research shows that reflection strengthens habit formation by helping you identify what works.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Progress indicator
            LinearProgressIndicator(
                progress = {
                    val filled = listOf(
                        state.whatHelped.isNotBlank(),
                        state.whatHindered.isNotBlank(),
                        state.patternsNoticed.isNotBlank()
                    ).count { it }
                    filled / 3f
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .semantics {
                        contentDescription = "Progress: ${
                            listOf(
                                state.whatHelped.isNotBlank(),
                                state.whatHindered.isNotBlank(),
                                state.patternsNoticed.isNotBlank()
                            ).count { it }
                        } of 3 questions answered"
                    },
                color = Purple80,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            // Question 1
            ReflectionQuestion(
                number = 1,
                question = "What helped you succeed this week?",
                value = state.whatHelped,
                onValueChange = viewModel::updateWhatHelped,
                placeholder = "E.g., Setting out my workout clothes the night before..."
            )

            // Question 2
            ReflectionQuestion(
                number = 2,
                question = "What made it hard to show up?",
                value = state.whatHindered,
                onValueChange = viewModel::updateWhatHindered,
                placeholder = "E.g., Late meetings made me too tired..."
            )

            // Question 3
            ReflectionQuestion(
                number = 3,
                question = "Did you notice any patterns?",
                value = state.patternsNoticed,
                onValueChange = viewModel::updatePatternsNoticed,
                placeholder = "E.g., I do better when I exercise in the morning..."
            )

            // Error message
            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Submit button
                Button(
                    onClick = { viewModel.submitReflection() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !state.isSubmitting,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple80,
                        contentColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    if (state.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.surface
                        )
                    } else {
                        Text(
                            text = "Save Reflection",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Skip button
                TextButton(
                    onClick = { viewModel.skipReflection() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isSubmitting
                ) {
                    Text(
                        text = "Skip for now",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ReflectionQuestion(
    number: Int,
    question: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Question number badge
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Purple80.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Purple80
                )
            }

            Text(
                text = question,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Question $number: $question"
                },
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            },
            minLines = 3,
            maxLines = 6,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Purple80,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}
