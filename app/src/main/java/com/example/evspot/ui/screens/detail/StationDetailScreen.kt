package com.example.evspot.ui.screens.detail
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationDetailScreen(stationName: String, onBack: () -> Unit) {
    val station = sampleStations.find { it.name == stationName } ?: sampleStations.first()
    var selectedSlotId by remember { mutableStateOf<Int?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(station.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp, color = Color.White) {
                val isSelected = selectedSlotId != null
                Button(
                    onClick = { 
                        if (isSelected) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Slot booked successfully!")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color(0xFF2E7D32) else Color(0xFFE0E0E0),
                        contentColor = if (isSelected) Color.White else Color.Gray
                    )
                ) {
                    Icon(Icons.Default.EvStation, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Book Your EV Slot")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFFEFEFEF), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Map goes here", color = Color.Gray, fontSize = 13.sp)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(station.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFE8F5E9)) {
                                    Text(station.type, fontSize = 10.sp, color = Color(0xFF2E7D32), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Text(station.location, fontSize = 13.sp, color = Color.Gray)
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (station.isFull) Color(0xFFFCE8E8) else Color(0xFFE8F5E9)
                        ) {
                            Text(
                                station.availability,
                                fontSize = 11.sp,
                                color = if (station.isFull) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFA000), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${station.rating} (${station.reviewCount} reviews)", fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("₹${station.pricePerKwh}/kWh", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Station Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailRow(Icons.Default.Speed, "Max Speed", "${station.maxSpeedKw} kW")
                    DetailRow(Icons.Default.Cable, "Connectors", station.connectors)
                    DetailRow(Icons.Default.Schedule, "Hours", station.hours)
                    DetailRow(Icons.Default.DirectionsCar, "Distance", "${station.distanceKm} km • ${station.etaMin} min")
                }
            }

            // Available Slots Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Available Slots", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    station.slots.forEach { slot ->
                        SlotRow(
                            slot = slot,
                            isSelected = selectedSlotId == slot.id,
                            onClick = {
                                if (slot.isAvailable) {
                                    selectedSlotId = if (selectedSlotId == slot.id) null else slot.id
                                } else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("You can't book this slot")
                                    }
                                }
                            }
                        )
                        if (station.slots.indexOf(slot) != station.slots.size - 1) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF5F5F5))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SlotRow(slot: ChargingSlot, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color(0xFFE8F5E9) else Color.Transparent,
        border = if (isSelected) BorderStroke(1.dp, Color(0xFF2E7D32)) else null
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = if (slot.isAvailable) Color(0xFFE8F5E9) else Color(0xFFFCE8E8),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (slot.isAvailable) Icons.Default.CheckCircle else Icons.Default.Block,
                            contentDescription = null,
                            tint = if (slot.isAvailable) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Slot ${slot.id}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(slot.type, fontSize = 12.sp, color = Color.Gray)
                    slot.statusText?.let {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = if (slot.isAvailable) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = it,
                                fontSize = 11.sp,
                                color = if (slot.isAvailable) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (slot.isAvailable) Color(0xFFE8F5E9) else Color(0xFFFCE8E8)
            ) {
                Text(
                    text = if (slot.isAvailable) "Available" else "Busy",
                    color = if (slot.isAvailable) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, fontSize = 13.sp, color = Color.Gray)
        }
        Text(value, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}