package com.example.evspot.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.evspot.VehicleHeroCard
import com.example.evspot.ChargingStatusCard
import com.example.evspot.VehicleHealthSection
import com.example.evspot.OdometerEfficiencyCard
import com.example.evspot.mockVehicle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleHealthScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vehicle Health") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 24.dp, top = 16.dp)
        ) {
            item { VehicleHeroCard(mockVehicle) }
            item { ChargingStatusCard(mockVehicle.charging) }
            item { VehicleHealthSection(mockVehicle) }
            item { OdometerEfficiencyCard(mockVehicle) }
        }
    }
}