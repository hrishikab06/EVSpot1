package com.example.evspot.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.verticalScroll

import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
data class RouteStop(
    val name: String,
    val address: String,
    val batteryPercent: Int,
    val time: String,
    val isRecommendedStop: Boolean = false
)

val sampleRouteStops = listOf(
    RouteStop("Your Location", "Bandra Kurla Complex, Mumbai", 100, "9:30 AM"),
    RouteStop("GreenCharge Station", "Near Chembur, Mumbai", 62, "20 min charge", isRecommendedStop = true),
    RouteStop("Navi Mumbai Airport", "Navi Mumbai, Maharashtra", 31, "10:55 AM")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanTripScreen(onBack: () -> Unit) {
    var selectedFilter by remember { mutableStateOf("Recommended") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plan a Trip") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LocationInputCard()

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Recommended", "Fastest", "Eco-friendly").forEach { filter ->
                    FilterChipItem(
                        label = filter,
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFEFEFEF), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Map goes here", color = Color.Gray, fontSize = 13.sp)
            }

            TripOverviewCard()

            RoutePlanCard()

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Estimated Cost", fontWeight = FontWeight.Bold)
                        Text("(including charging)", fontSize = 12.sp, color = Color.Gray)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("₹312 – ₹340", fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Trip")
                }
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Start Navigation")
                }
            }
        }
    }
}

@Composable
fun LocationInputCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = Color(0xFF2E7D32), modifier = Modifier.size(8.dp)) {}
                Spacer(modifier = Modifier.width(12.dp))
                Text("Current Location", modifier = Modifier.weight(1f))
                Icon(Icons.Default.SwapVert, contentDescription = "Swap")
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Navi Mumbai Airport, Mumbai")
            }
        }
    }
}

@Composable
fun FilterChipItem(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (selected) Color(0xFF2E7D32) else Color.White,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else Color.Black,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun TripOverviewCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Trip Overview", fontWeight = FontWeight.Bold)
                Text("Edit Trip", color = Color(0xFF2E7D32), fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Total Distance", fontSize = 12.sp, color = Color.Gray)
                    Text("42 km", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Column {
                    Text("Total Time (with stop)", fontSize = 12.sp, color = Color.Gray)
                    Text("1 h 25 min", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Column {
                    Text("Est. Energy Needed", fontSize = 12.sp, color = Color.Gray)
                    Text("18.2 kWh", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE8F5E9),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Charging stop recommended", color = Color(0xFF2E7D32), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("1 stop for 20 min at GreenCharge Station", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun RoutePlanCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Route Plan", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            sampleRouteStops.forEach { stop ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = if (stop.isRecommendedStop) Icons.Default.Bolt else Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(stop.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(stop.address, fontSize = 12.sp, color = Color.Gray)
                            if (stop.isRecommendedStop) {
                                Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFE8F5E9)) {
                                    Text(
                                        "Recommended Stop",
                                        fontSize = 10.sp,
                                        color = Color(0xFF2E7D32),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("${stop.batteryPercent}%", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(stop.time, fontSize = 11.sp, color = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}