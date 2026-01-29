package com.onefocus.app.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.onefocus.app.core.design.OnSurface
import com.onefocus.app.core.design.Surface
import com.onefocus.app.data.model.enums.HabitType
import com.onefocus.app.data.model.enums.TriggerType
import com.onefocus.app.feature.onboarding.*

@Composable
fun OneFocusNavGraph(
    navController: NavHostController,
    startDestination: String = Destination.Welcome.route
) {
    val viewModel: OnboardingViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Welcome Screen
        composable(Destination.Welcome.route) {
            WelcomeScreen(
                onBeginJourney = {
                    navController.navigate(Destination.HabitType.route)
                }
            )
        }

        // Habit Type Selection
        composable(Destination.HabitType.route) {
            HabitTypeScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onTypeSelected = { type ->
                    viewModel.setHabitType(type)
                    navController.navigate(Destination.HabitName.route)
                }
            )
        }

        // Habit Name Input
        composable(Destination.HabitName.route) {
            state.habitType?.let { habitType ->
                HabitNameScreen(
                    habitType = habitType,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onContinue = { habitName ->
                        viewModel.setHabitName(habitName)
                        
                        // Navigate based on habit type
                        if (habitType.requiresTrigger) {
                            navController.navigate(Destination.TriggerSetup.route)
                        } else {
                            // Repeating habit - show optional trigger screen
                            navController.navigate(Destination.TriggerRepeating.route)
                        }
                    }
                )
            }
        }

        // Trigger Setup (for non-repeating habits)
        composable(Destination.TriggerSetup.route) {
            state.habitType?.let { habitType ->
                TriggerSetupScreen(
                    habitName = state.habitName,
                    habitType = habitType,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onContinue = { trigger ->
                        viewModel.setTrigger(TriggerType.ANCHOR, trigger)
                        navController.navigate(Destination.Ready.route)
                    }
                )
            }
        }

        // Trigger Repeating (for repeating habits - optional)
        composable(Destination.TriggerRepeating.route) {
            TriggerRepeatingScreen(
                habitName = state.habitName,
                onBackClick = {
                    navController.popBackStack()
                },
                onContinue = { triggerType, triggerText ->
                    viewModel.setTrigger(triggerType, triggerText)
                    navController.navigate(Destination.Ready.route)
                }
            )
        }

        // Ready Screen
        composable(Destination.Ready.route) {
            ReadyScreen(
                habitName = state.habitName,
                triggerText = if (state.triggerType == TriggerType.THROUGHOUT) "" else state.triggerText,
                onStartJourney = {
                    viewModel.createHabitAndJourney {
                        navController.navigate(Destination.Home.route) {
                            // Clear back stack to prevent going back to onboarding
                            popUpTo(Destination.Welcome.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // Home Screen (placeholder for now)
        composable(Destination.Home.route) {
            HomeScreenPlaceholder()
        }
    }
}

@Composable
fun HomeScreenPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Home Screen Coming Soon",
            style = MaterialTheme.typography.headlineMedium,
            color = OnSurface
        )
    }
}
