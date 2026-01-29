package com.onefocus.app.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.onefocus.app.core.design.*
import com.onefocus.app.data.model.enums.HabitType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriggerSetupScreen(
    habitName: String,
    habitType: HabitType,
    onBackClick: () -> Unit,
    onContinue: (trigger: String) -> Unit
) {
    var triggerText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

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
                    text = "Set your trigger",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "When will you do this?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Intention Builder
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(20.dp),
                color = SurfaceContainer
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = Purple40
                        ) {
                            Text(
                                text = "IMPLEMENTATION INTENTION",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Purple80
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // After I...
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "After I",
                            style = MaterialTheme.typography.bodyLarge,
                            color = OnSurfaceVariant
                        )
                        OutlinedTextField(
                            value = triggerText,
                            onValueChange = { triggerText = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("pour my coffee") },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Purple80,
                                unfocusedBorderColor = OutlineVariant,
                                focusedContainerColor = Surface,
                                unfocusedContainerColor = Surface
                            ),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.None,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                }
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // I will...
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "I will $habitName",
                            style = MaterialTheme.typography.titleLarge,
                            color = Purple80
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pro tip
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                color = GreenContainer
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "💡",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Choose a trigger you already do daily. The more automatic it is, the better.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Continue Button
            Button(
                onClick = {
                    if (triggerText.isNotBlank()) {
                        onContinue(triggerText)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                enabled = triggerText.isNotBlank(),
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
