package com.example.evspot.navigation
import com.example.evspot.ui.screens.detail.PlanTripScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.evspot.ui.screens.*
import com.example.evspot.ui.screens.auth.*
import com.example.evspot.ui.screens.detail.*
import com.example.evspot.ui.screens.VehicleViewModel
import com.example.evspot.ui.UserViewModel
import com.example.evspot.ui.ThemeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.navArgument
import androidx.navigation.NavType

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
                onLoginSuccess = {
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
            arguments = listOf(navArgument("stationName") { type = NavType.StringType })
        ) { backStackEntry ->
            val stationName = backStackEntry.arguments?.getString("stationName") ?: ""
            StationDetailScreen(stationName = stationName, onBack = { navController.popBackStack() })
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
            VehicleHealthScreen(onBack = { navController.popBackStack() }) 
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
