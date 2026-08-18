package com.example.evspot.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.evspot.VehicleHeroCard
import com.example.evspot.ChargingStatusCard
import com.example.evspot.VehicleHealthSection
import com.example.evspot.OdometerEfficiencyCard
import com.example.evspot.mockVehicle
import com.example.evspot.ui.screens.VehicleViewModel
import com.example.evspot.data.model.BatteryStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleHealthScreen(
    onBack: () -> Unit,
    viewModel: VehicleViewModel = viewModel()
) {
    val bmsStatus by viewModel.bmsStatus.collectAsState()

    // Map BMS status to mockVehicle for some components, or use it directly
    val currentVehicle = bmsStatus?.let { status ->
        mockVehicle.copy(
            battery = status.soc.toInt(),
            range = status.remainingRange.toInt(),
            temperature = status.batteryTemp.toInt(),
            averageSpeed = status.speed.toInt()
        )
    } ?: mockVehicle

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vehicle Health") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            bmsStatus?.let { status ->
                item { BmsStatusCard(status) }
            }
            item { VehicleHeroCard(currentVehicle) }
            item { ChargingStatusCard(currentVehicle.charging) }
            item { VehicleHealthSection(currentVehicle) }
            item { OdometerEfficiencyCard(currentVehicle) }
        }
    }
}

@Composable
fun BmsStatusCard(status: BatteryStatus) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "BMS Live Simulation",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "LIVE",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                BmsItem("SOC", "${status.soc.toInt()}%", Icons.Default.BatteryChargingFull)
                BmsItem("Voltage", "${"%.1f".format(status.voltage)}V", Icons.Default.ElectricBolt)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                BmsItem("Temp", "${status.batteryTemp.toInt()}°C", Icons.Default.Thermostat)
                BmsItem("Range", "${status.remainingRange.toInt()} km", Icons.Default.DirectionsCar)
            }
        }
    }
}

@Composable
fun BmsItem(label: String, value: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}
