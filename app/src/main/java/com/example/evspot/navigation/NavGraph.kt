package com.example.evspot.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.evspot.ui.screens.*
import com.example.evspot.ui.screens.detail.*

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateToHome = {
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Main.route) {
            MainDashboardScreen(onNavigateToDetail = { route ->
                navController.navigate(route)
            })
        }
        
        // Detail Screens
        composable(Screen.TripPlanner.route) { 
            PlanTripScreen(onBack = { navController.popBackStack() }) 
        }
        composable(Screen.NearbyChargers.route) { 
            NearbyChargersScreen(onBack = { navController.popBackStack() }) 
        }
        composable(Screen.History.route) { 
            ChargingHistoryScreen(onBack = { navController.popBackStack() }) 
        }
        composable(Screen.UpcomingBookings.route) { 
            UpcomingBookingsScreen(onBack = { navController.popBackStack() }) 
        }
        composable(Screen.Usage.route) { 
            EnergyUsageScreen(onBack = { navController.popBackStack() }) 
        }
        composable(Screen.Health.route) { 
            VehicleHealthScreen(onBack = { navController.popBackStack() }) 
        }
    }
}
