package com.example.evspot.ui.screens.detail

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
import com.example.evspot.ui.components.ChargingMap

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
    val isFull: Boolean = false
)

val sampleStations = listOf(
    ChargerStation("GreenCharge Station", "DC Fast", "Bandra Kurla Complex, Mumbai", 0.8, 3, 20, 4.6, 128, 60, "CCS, CHAdeMO", "24/7", "3 available"),
    ChargerStation("VoltPoint Hub", "DC Fast", "Powai, Mumbai", 1.4, 5, 18, 4.4, 96, 50, "CCS, Type 2", "24/7", "2 available"),
    ChargerStation("EcoCharge Station", "DC Fast", "Chandivali, Mumbai", 2.1, 7, 19, 4.5, 112, 60, "CCS, CHAdeMO", "24/7", "4 available"),
    ChargerStation("ChargeZone DC Fast", "DC Fast", "Saki Naka, Mumbai", 2.6, 8, 22, 4.2, 78, 100, "CCS, CHAdeMO", "24/7", "Full", isFull = true)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyChargersScreen(onBack: () -> Unit) {
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
                ChargingMap(
                    modifier = Modifier.fillMaxSize(),
                    isLiteMode = true
                )
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
                    StationCard(station)
                }
            }
        }
    }
}

@Composable
fun StationCard(station: ChargerStation) {
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
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("View Details", color = Color(0xFF2E7D32), fontSize = 12.sp)
                }
            }
        }
    }
}