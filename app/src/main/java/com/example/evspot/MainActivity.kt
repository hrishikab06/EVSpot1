package com.example.evspot
import com.example.evspot.ui.screens.detail.PlanTripScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.android.libraries.places.api.Places
import com.example.evspot.BuildConfig
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.compose.rememberNavController
import com.example.evspot.navigation.AppNavGraph
import com.example.evspot.ui.UserViewModel
import com.example.evspot.ui.screens.VehicleViewModel
import com.example.evspot.MyVehiclesScreen
import com.example.evspot.ui.screens.detail.ChargingHistoryScreen
import com.example.evspot.ui.theme.EVSpotTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Google Places SDK
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        }

        enableEdgeToEdge()
        setContent {
            EVSpotTheme {
                val navController = rememberNavController()
                AppNavGraph(navController)
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun EVSpotApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.BOOKINGS) }
    val userViewModel: UserViewModel = viewModel()
    val vehicleViewModel: VehicleViewModel = viewModel()

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when (val dest = currentDestination) {
                AppDestinations.BOOKINGS -> ChargingHistoryScreen(onBack = {}, viewModel = userViewModel)
                AppDestinations.VEHICLE -> MyVehiclesScreen(
                    onAddVehicleClick = {},
                    onNavigate = { /* TODO: navigation logic */ },
                    viewModel = vehicleViewModel
                )
                AppDestinations.MAP -> PlanTripScreen()
                else -> Greeting(
                    name = dest.label,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    MAP("Map", Icons.Default.Map),
    VEHICLE("Vehicle", Icons.Default.DirectionsCar),
    BOOKINGS("Bookings", Icons.Default.CalendarMonth),
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EVSpotTheme {
        Greeting("Android")
    }
}
