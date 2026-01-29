package com.onefocus.app.feature.onboarding

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
fun HabitNameScreen(
    habitType: HabitType,
    onBackClick: () -> Unit,
    onContinue: (habitName: String) -> Unit
) {
    var habitName by remember { mutableStateOf("") }
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
                    text = habitType.icon,
                    style = MaterialTheme.typography.displayMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "What's your habit?",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Give it a short, clear name",
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Habit Name Input
            OutlinedTextField(
                value = habitName,
                onValueChange = { habitName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                placeholder = { Text(getPlaceholder(habitType)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Purple80,
                    unfocusedBorderColor = OutlineVariant,
                    focusedContainerColor = SurfaceContainerHigh,
                    unfocusedContainerColor = SurfaceContainerHigh
                ),
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        if (habitName.isNotBlank()) {
                            onContinue(habitName)
                        }
                    }
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            // Continue Button
            Button(
                onClick = {
                    if (habitName.isNotBlank()) {
                        onContinue(habitName)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                enabled = habitName.isNotBlank(),
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

private fun getPlaceholder(habitType: HabitType): String {
    return when (habitType) {
        HabitType.BINARY -> "e.g. Meditate"
        HabitType.TIMED -> "e.g. Read"
        HabitType.INCREMENT -> "e.g. Do pushups"
        HabitType.REDUCTION -> "e.g. Cut down smoking"
        HabitType.REPEATING -> "e.g. Drink water"
    }
}
