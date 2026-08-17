package com.example.evspot.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.evspot.ui.UserViewModel

@Composable
fun BookingsScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: UserViewModel = viewModel()
) {
    UpcomingBookingsScreen(
        onNavigate = onNavigate,
        viewModel = viewModel
    )
}
