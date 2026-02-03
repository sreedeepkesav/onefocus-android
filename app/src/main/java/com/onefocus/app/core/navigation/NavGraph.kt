package com.onefocus.app.core.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.onefocus.app.data.model.enums.TriggerType
import com.onefocus.app.feature.onboarding.*
import com.onefocus.app.feature.home.HomeScreen
import com.onefocus.app.feature.mood.MoodBeforeScreen
import com.onefocus.app.feature.mood.MoodAfterScreen
import com.onefocus.app.feature.analytics.AnalyticsScreen
import com.onefocus.app.feature.settings.SettingsScreen
import com.onefocus.app.feature.secondhabit.AddSecondHabitScreen
import com.onefocus.app.feature.failureanalysis.FailureAnalysisScreen

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

        // Home Screen
        composable(Destination.Home.route) {
            HomeScreen(
                onNavigateToMood = {
                    navController.navigate(Destination.MoodBefore.route)
                },
                onNavigateToAddSecondHabit = {
                    navController.navigate(Destination.AddSecondHabit.route)
                },
                onNavigateToAnalytics = {
                    navController.navigate(Destination.Analytics.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Destination.Settings.route)
                },
                onNavigateToFailureAnalysis = {
                    navController.navigate(Destination.FailureAnalysis.route)
                },
                onNavigateToReflection = {
                    navController.navigate(Destination.Reflection.route)
                }
            )
        }

        // Focus Mode
        composable(Destination.Focus.route) {
            FocusScreen(
                onComplete = {
                    navController.navigate(Destination.MoodBefore.route)
                }
            )
        }

        // Mood Before
        composable(Destination.MoodBefore.route) {
            MoodBeforeScreen(
                onContinue = {
                    navController.navigate(Destination.MoodAfter.route)
                }
            )
        }

        // Mood After
        composable(Destination.MoodAfter.route) {
            MoodAfterScreen(
                onContinue = {
                    navController.navigate(Destination.Home.route) {
                        popUpTo(Destination.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // Analytics/Insights Screen
        composable(Destination.Analytics.route) {
            AnalyticsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Settings Screen
        composable(Destination.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Add Second Habit Screen
        composable(Destination.AddSecondHabit.route) {
            val homeViewModel: com.onefocus.app.feature.home.HomeViewModel = hiltViewModel()
            val homeState by homeViewModel.state.collectAsState()

            AddSecondHabitScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToOnboarding = {
                    // Navigate to habit type selection for second habit
                    navController.navigate(Destination.HabitType.route)
                },
                isUnlocked = homeState.isSecondHabitUnlocked,
                daysCompleted = homeState.journey?.completedDays?.size ?: 0
            )
        }

        // Reflection Screen
        composable(Destination.Reflection.route) {
            com.onefocus.app.feature.reflection.ReflectionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Failure Analysis Screen
        composable(Destination.FailureAnalysis.route) {
            FailureAnalysisScreen(
                onNavigateToHome = {
                    navController.navigate(Destination.Home.route) {
                        popUpTo(Destination.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
