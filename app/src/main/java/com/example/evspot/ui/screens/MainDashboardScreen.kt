package com.example.evspot.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.evspot.navigation.Screen
import com.example.evspot.ui.components.EVSpotBottomNavigation
import com.example.evspot.ui.screens.VehicleViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.evspot.ui.screens.detail.WalletScreen

@Composable
fun MainDashboardScreen(
    onNavigateToDetail: (String) -> Unit,
    vehicleViewModel: VehicleViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            EVSpotBottomNavigation(
                currentRoute = currentDestination,
                onNavigate = { route ->
                    if (currentDestination != route) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { 
                HomeScreen(onNavigate = onNavigateToDetail) 
            }
            composable(Screen.Wallet.route) { 
                WalletScreen(onBack = { 
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }) 
            }
            composable(Screen.Vehicle.route) { 
                VehicleScreen(
                    onAddVehicleClick = { onNavigateToDetail(Screen.AddVehicle.route) },
                    onNavigate = onNavigateToDetail,
                    viewModel = vehicleViewModel
                ) 
            }
            composable(Screen.Bookings.route) { 
                BookingsScreen(onNavigate = onNavigateToDetail) 
            }
        }
    }
}

