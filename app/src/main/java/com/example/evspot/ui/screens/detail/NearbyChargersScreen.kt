package com.example.evspot.ui.screens.detail
import com.example.evspot.navigation.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ChargingSlot(
    val id: Int,
    val isAvailable: Boolean,
    val type: String,
    val statusText: String? = null // e.g., "Ready" or "Available until 5 PM" or "Available in 15 min"
)

data class ChargerStation(
    val name: String,
    val type: String,
    val location: String,
    val distanceKm: Double,
    val etaMin: Int,
    val pricePerKwh: Int,
    val rating: Double,
    val reviewCount: Int,
    val maxSpeedKw: Int,
    val connectors: String,
    val hours: String,
    val availability: String,
    val isFull: Boolean = false,
    val slots: List<ChargingSlot> = emptyList()
)

val sampleStations = listOf(
    ChargerStation(
        name = "GreenCharge Station",
        type = "DC Fast",
        location = "Bandra Kurla Complex, Mumbai",
        distanceKm = 0.8,
        etaMin = 3,
        pricePerKwh = 20,
        rating = 4.6,
        reviewCount = 128,
        maxSpeedKw = 60,
        connectors = "CCS, CHAdeMO",
        hours = "24/7",
        availability = "3 available",
        slots = listOf(
            ChargingSlot(1, true, "CCS (60kW)", "Available: 2:30 PM to 3:00 PM"),
            ChargingSlot(2, false, "CCS (60kW)", "Busy: 2:15 PM to 3:30 PM"),
            ChargingSlot(3, true, "CHAdeMO (50kW)", "Available: 3:00 PM to 3:30 PM"),
            ChargingSlot(4, true, "CCS (60kW)", "Available: 3:30 PM to 4:00 PM")
        )
    ),
    ChargerStation(
        name = "VoltPoint Hub",
        type = "DC Fast",
        location = "Powai, Mumbai",
        distanceKm = 1.4,
        etaMin = 5,
        pricePerKwh = 18,
        rating = 4.4,
        reviewCount = 96,
        maxSpeedKw = 50,
        connectors = "CCS, Type 2",
        hours = "24/7",
        availability = "2 available",
        slots = listOf(
            ChargingSlot(1, true, "CCS (50kW)", "Available: 2:45 PM to 3:45 PM"),
            ChargingSlot(2, false, "Type 2 (22kW)", "Busy: 2:30 PM to 5:00 PM"),
            ChargingSlot(3, true, "CCS (50kW)", "Available: 3:45 PM to 4:45 PM")
        )
    ),
    ChargerStation(
        name = "EcoCharge Station",
        type = "DC Fast",
        location = "Chandivali, Mumbai",
        distanceKm = 2.1,
        etaMin = 7,
        pricePerKwh = 19,
        rating = 4.5,
        reviewCount = 112,
        maxSpeedKw = 60,
        connectors = "CCS, CHAdeMO",
        hours = "24/7",
        availability = "4 available",
        slots = listOf(
            ChargingSlot(1, true, "CCS (60kW)", "Available: 2:00 PM to 3:00 PM"),
            ChargingSlot(2, true, "CCS (60kW)", "Available: 3:00 PM to 4:00 PM"),
            ChargingSlot(3, true, "CHAdeMO (50kW)", "Available: 4:15 PM to 5:15 PM"),
            ChargingSlot(4, true, "CCS (60kW)", "Available: 5:15 PM to 6:15 PM")
        )
    ),
    ChargerStation(
        name = "ChargeZone DC Fast",
        type = "DC Fast",
        location = "Saki Naka, Mumbai",
        distanceKm = 2.6,
        etaMin = 8,
        pricePerKwh = 22,
        rating = 4.2,
        reviewCount = 78,
        maxSpeedKw = 100,
        connectors = "CCS, CHAdeMO",
        hours = "24/7",
        availability = "Full",
        isFull = true,
        slots = listOf(
            ChargingSlot(1, false, "CCS (100kW)", "Busy: 2:40 PM to 3:00 PM"),
            ChargingSlot(2, false, "CCS (100kW)", "Busy: 2:45 PM to 3:15 PM")
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyChargersScreen(onBack: () -> Unit, onNavigate: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nearby Charging Stations") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    placeholder = { Text("Search location or station") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedButton(onClick = { }) {
                    Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Filter")
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(Color(0xFFEFEFEF)),
                contentAlignment = Alignment.Center
            ) {
                Text("Map goes here", color = Color.Gray, fontSize = 13.sp)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Column {
                        Text("Nearby Stations", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Within 5 km radius", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                items(sampleStations) { station ->
                    StationCard(station, onClick = { onNavigate(Screen.StationDetail.createRoute(station.name)) })
                }
            }
        }
    }
}

@Composable
fun StationCard(station: ChargerStation, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(station.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFE8F5E9)) {
                            Text(station.type, fontSize = 10.sp, color = Color(0xFF2E7D32), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Text(station.location, fontSize = 12.sp, color = Color.Gray)
                    Text("${station.distanceKm} km • ${station.etaMin} min", fontSize = 12.sp, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (station.isFull) Color(0xFFFCE8E8) else Color(0xFFE8F5E9)
                    ) {
                        Text(
                            station.availability,
                            fontSize = 11.sp,
                            color = if (station.isFull) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Text("₹${station.pricePerKwh}/kWh", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("★ ${station.rating} (${station.reviewCount})", fontSize = 11.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row {
                    Text("${station.maxSpeedKw} kW  ", fontSize = 12.sp)
                    Text("${station.connectors}  ", fontSize = 12.sp, color = Color.Gray)
                    Text(station.hours, fontSize = 12.sp, color = Color.Gray)
                }
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("View Details", color = Color(0xFF2E7D32), fontSize = 12.sp)
                }
            }
        }
    }
}