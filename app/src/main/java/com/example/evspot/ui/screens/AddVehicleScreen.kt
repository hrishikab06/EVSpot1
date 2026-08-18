package com.example.evspot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.evspot.R
import com.example.evspot.data.model.ConnectionStatus
import com.example.evspot.data.model.VehicleListing
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVehicleScreen(
    onBack: () -> Unit,
    viewModel: VehicleViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }
    var issue by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Vehicle", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FBF8))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Vehicle Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Vehicle Nickname") },
                        placeholder = { Text("e.g. My Nexon EV") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) }
                    )

                    OutlinedTextField(
                        value = model,
                        onValueChange = { model = it },
                        label = { Text("Model Name") },
                        placeholder = { Text("e.g. Tata Nexon EV Max") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = plate,
                        onValueChange = { plate = it },
                        label = { Text("Car Number (Plate)") },
                        placeholder = { Text("e.g. MH01AB1234") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = issue,
                        onValueChange = { issue = it },
                        label = { Text("Known Issues (Optional)") },
                        placeholder = { Text("e.g. Battery drain, AC issues") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 3
                    )
                }
            }

            Button(
                onClick = {
                    if (name.isNotBlank() && model.isNotBlank() && plate.isNotBlank()) {
                        val newVehicle = VehicleListing(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            model = model,
                            plate = plate,
                            imageRes = R.drawable.car, // Default image
                            batteryPercentage = 100, // New vehicle default
                            estRangeKm = 300, // New vehicle default
                            connectionStatus = ConnectionStatus.CONNECTED,
                            chargingStatus = "Not Charging",
                            lastChargedInfo = "Just added",
                            issue = issue.takeIf { it.isNotBlank() }
                        )
                        viewModel.addVehicle(newVehicle)
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                enabled = name.isNotBlank() && model.isNotBlank() && plate.isNotBlank()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Register Vehicle", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
