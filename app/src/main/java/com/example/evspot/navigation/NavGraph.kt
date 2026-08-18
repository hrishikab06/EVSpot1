package com.example.evspot.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.evspot.ui.screens.detail.ChargingHistoryScreen
import com.example.evspot.ui.screens.UpcomingBookingsScreen
import com.example.evspot.ui.screens.auth.*
import com.example.evspot.ui.screens.detail.PlanTripScreen
import com.example.evspot.ui.screens.detail.AccountScreen
import com.example.evspot.ui.screens.detail.NearbyChargersScreen
import com.example.evspot.ui.screens.detail.StationDetailScreen
import com.example.evspot.ui.screens.detail.WalletScreen
import com.example.evspot.ui.screens.detail.EnergyUsageScreen
import com.example.evspot.ui.screens.detail.VehicleHealthScreen
import com.example.evspot.ui.screens.detail.PastTripsScreen
import com.example.evspot.ui.screens.AddVehicleScreen
import com.example.evspot.ui.screens.MainDashboardScreen
import com.example.evspot.ui.screens.NotificationScreen
import com.example.evspot.ui.screens.SplashScreen
import com.example.evspot.ui.screens.VehicleViewModel
import com.example.evspot.ui.UserViewModel
import com.example.evspot.ui.ThemeViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {
    val vehicleViewModel: VehicleViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateToNext = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Account.route) {
            AccountScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) },
                themeViewModel = themeViewModel
            )
        }
        composable(Screen.Notifications.route) {
            NotificationScreen(
                viewModel = userViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { userId, email, name ->
                    userViewModel.setUserSession(userId, email, name)
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.Login.route)
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Main.route) {
            MainDashboardScreen(
                onNavigateToDetail = { route ->
                    navController.navigate(route)
                },
                vehicleViewModel = vehicleViewModel
            )
        }
        
        // Detail Screens
        // Inside NavGraph.kt
        composable(Screen.TripPlanner.route) {
            PlanTripScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screen.StationDetail.route,
            arguments = listOf(navArgument("stationId") { type = NavType.IntType })
        ) { backStackEntry ->
            val stationId = backStackEntry.arguments?.getInt("stationId") ?: -1
            StationDetailScreen(
                stationId = stationId, 
                onBack = { navController.popBackStack() },
                userViewModel = userViewModel
            )
        }
        composable(Screen.NearbyChargers.route) {
            NearbyChargersScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable(Screen.History.route) { 
            ChargingHistoryScreen(
                onBack = { navController.popBackStack() },
                viewModel = userViewModel
            ) 
        }
        composable(Screen.Wallet.route) {
            WalletScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.UpcomingBookings.route) { 
            UpcomingBookingsScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) },
                viewModel = userViewModel
            ) 
        }
        composable(Screen.Usage.route) { 
            EnergyUsageScreen(onBack = { navController.popBackStack() }) 
        }
        composable(Screen.Health.route) { 
            VehicleHealthScreen(
                onBack = { navController.popBackStack() },
                viewModel = vehicleViewModel
            ) 
        }
        composable(Screen.PastTrips.route) {
            PastTripsScreen(
                onBackClick = { navController.popBackStack() },
                viewModel = userViewModel
            )
        }
        composable(Screen.AddVehicle.route) {
            AddVehicleScreen(
                onBack = { navController.popBackStack() },
                viewModel = vehicleViewModel
            )
        }
    }
}
