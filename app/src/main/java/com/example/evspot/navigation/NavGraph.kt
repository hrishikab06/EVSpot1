package com.example.evspot.navigation
import com.example.evspot.ui.screens.detail.PlanTripScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.evspot.ui.screens.*
import com.example.evspot.ui.screens.auth.*
import com.example.evspot.ui.screens.detail.*

@Composable
fun AppNavGraph(navController: NavHostController) {
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
            MainDashboardScreen(onNavigateToDetail = { route ->
                navController.navigate(route)
            })
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
