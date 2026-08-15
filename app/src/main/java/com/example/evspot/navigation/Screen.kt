package com.example.evspot.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object ForgotPassword : Screen("forgot_password")
    object Main : Screen("main")
    object Home : Screen("home")
    object Map : Screen("map")
    object Vehicle : Screen("vehicle")
    object Bookings : Screen("bookings")
    object History : Screen("history")
    object Usage : Screen("usage")
    object Health : Screen("health")
    object TripPlanner : Screen("trip_planner")
    object NearbyChargers : Screen("nearby_chargers")
    object UpcomingBookings : Screen("upcoming_bookings")
}
