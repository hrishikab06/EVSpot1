package com.example.evspot.ui.screens
import com.example.evspot.MyVehiclesScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun VehicleScreen(
    onAddVehicleClick: () -> Unit,
    viewModel: VehicleViewModel = viewModel()
) {
    MyVehiclesScreen(
        onAddVehicleClick = onAddVehicleClick,
        viewModel = viewModel
    )
}
