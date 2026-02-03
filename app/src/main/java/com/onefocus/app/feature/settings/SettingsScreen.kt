package com.onefocus.app.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Notifications Section
            SettingsSection(
                title = "Notifications",
                icon = Icons.Default.Notifications
            ) {
                SwitchSettingItem(
                    title = "Enable Notifications",
                    subtitle = "Receive daily reminders",
                    checked = state.notificationsEnabled,
                    onCheckedChange = { viewModel.toggleNotifications(it) }
                )

                if (state.notificationsEnabled) {
                    ClickableSettingItem(
                        title = "Notification Time",
                        subtitle = viewModel.getFormattedTime(),
                        onClick = { viewModel.showTimePicker() }
                    )

                    SwitchSettingItem(
                        title = "Milestone Notifications",
                        subtitle = "Celebrate your achievements",
                        checked = state.milestoneNotifications,
                        onCheckedChange = { viewModel.toggleMilestoneNotifications(it) }
                    )

                    SwitchSettingItem(
                        title = "Streak Protection",
                        subtitle = "Get reminded before you lose your streak",
                        checked = state.streakProtectionNotifications,
                        onCheckedChange = { viewModel.toggleStreakProtectionNotifications(it) }
                    )
                }
            }

            // Journey Section
            SettingsSection(
                title = "Journey",
                icon = Icons.Default.Timeline
            ) {
                ClickableSettingItem(
                    title = "Reset Journey",
                    subtitle = "Start your 66-day journey over",
                    onClick = { viewModel.showResetDialog() },
                    isDestructive = true
                )
            }

            // About Section
            SettingsSection(
                title = "About",
                icon = Icons.Default.Info
            ) {
                InfoSettingItem(
                    title = "Version",
                    subtitle = "1.0.0"
                )
                InfoSettingItem(
                    title = "OneFocus",
                    subtitle = "One habit at a time, for 66 days"
                )
            }
        }
    }

    // Time Picker Dialog
    if (state.showTimePickerDialog) {
        TimePickerDialog(
            initialHour = state.notificationHour,
            initialMinute = state.notificationMinute,
            onDismiss = { viewModel.hideTimePicker() },
            onConfirm = { hour, minute ->
                viewModel.updateNotificationTime(hour, minute)
            }
        )
    }

    // Reset Confirmation Dialog
    if (state.showResetDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideResetDialog() },
            title = { Text("Reset Journey?") },
            text = {
                Text(
                    "This will delete all your progress and start fresh. " +
                            "This action cannot be undone."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.resetJourney() },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideResetDialog() }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SwitchSettingItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun ClickableSettingItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isDestructive) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun InfoSettingItem(
    title: String,
    subtitle: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = false
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Time") },
        text = {
            TimePicker(state = timePickerState)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(timePickerState.hour, timePickerState.minute)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
