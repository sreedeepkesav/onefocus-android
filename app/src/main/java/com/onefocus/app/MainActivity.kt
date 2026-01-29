package com.onefocus.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.onefocus.app.core.design.OneFocusTheme
import com.onefocus.app.core.navigation.Destination
import com.onefocus.app.core.navigation.OneFocusNavGraph
import com.onefocus.app.data.repository.JourneyRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var journeyRepository: JourneyRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            OneFocusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OneFocusApp(journeyRepository)
                }
            }
        }
    }
}

@Composable
fun OneFocusApp(journeyRepository: JourneyRepository) {
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Check if journey exists to determine start destination
    LaunchedEffect(Unit) {
        scope.launch {
            val journey = journeyRepository.getJourney().first()
            startDestination = if (journey != null) {
                Destination.Home.route
            } else {
                Destination.Welcome.route
            }
        }
    }

    // Wait for start destination to be determined
    startDestination?.let { start ->
        OneFocusNavGraph(
            navController = navController,
            startDestination = start
        )
    }
}
