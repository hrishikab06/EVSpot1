package com.example.evspot

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.ui.theme.EVSpotTheme

enum class ConnectionStatus {
    CONNECTED, OFFLINE
}

data class Vehicle(
    val id: String,
    val name: String,
    val model: String,
    val plate: String,
    val batteryPercentage: Int,
    val estRangeKm: Int,
    val connectionStatus: ConnectionStatus,
    val chargingStatus: String,
    val lastChargedInfo: String,
    val isPrimary: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyVehiclesScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Ev",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "Spot",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20),
                            fontSize = 20.sp
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
                        }
                        Surface(
                            shape = CircleShape,
                            color = Color.Red,
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.TopEnd)
                                .padding(top = 8.dp, end = 8.dp)
                        ) {
                            Text(
                                text = "3",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.wrapContentSize(Alignment.Center)
                            )
                        }
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile", modifier = Modifier.size(32.dp))
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Home") },
                    selected = false,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Map, contentDescription = null) },
                    label = { Text("Map") },
                    selected = false,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) },
                    label = { Text("Vehicle") },
                    selected = true,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                    label = { Text("Bookings") },
                    selected = false,
                    onClick = {}
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FBF8)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                VehiclesHeader()
            }
            item {
                VehiclesSummaryCard()
            }
            item {
                Text(
                    text = "Your Vehicles",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(sampleVehicles) { vehicle ->
                VehicleCard(vehicle)
            }
            item {
                TipsCard()
            }
        }
    }
}

@Composable
fun VehiclesHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "My Vehicles",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Manage and monitor all your EVs",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Button(
            onClick = { /* TODO */ },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add Vehicle", fontSize = 14.sp)
        }
    }
}

@Composable
fun VehiclesSummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SummaryItem(Modifier.weight(1f), Icons.Outlined.DirectionsCar, "3", "Total Vehicles", Color(0xFFE8F5E9), Color(0xFF2E7D32))
            SummaryVerticalDivider()
            SummaryItem(Modifier.weight(1f), Icons.Outlined.EvStation, "2", "Online", Color(0xFFE8F5E9), Color(0xFF2E7D32))
            SummaryVerticalDivider()
            SummaryItem(Modifier.weight(1f), Icons.Outlined.BatteryChargingFull, "78%", "Avg. Battery", Color(0xFFE8F5E9), Color(0xFF2E7D32))
            SummaryVerticalDivider()
            SummaryItem(Modifier.weight(1f), Icons.Outlined.LocationOn, "186 km", "Avg. Range", Color(0xFFE8F5E9), Color(0xFF2E7D32))
        }
    }
}

@Composable
fun VehicleCard(vehicle: Vehicle) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Box {
            if (vehicle.isPrimary) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(110.dp)
                        .padding(vertical = 12.dp)
                        .background(Color(0xFF2E7D32), RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                        .align(Alignment.CenterStart)
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.Top) {
                    // Vehicle Image Placeholder
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF5F5F5),
                        modifier = Modifier.size(100.dp, 80.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = vehicle.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            if (vehicle.isPrimary) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFE8F5E9)
                                ) {
                                    Text(
                                        text = "Primary",
                                        color = Color(0xFF2E7D32),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(text = vehicle.model, fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFF5F5F5)
                        ) {
                            Text(
                                text = vehicle.plate,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        ConnectionStatusBadge(vehicle.connectionStatus)
                        Spacer(modifier = Modifier.height(8.dp))
                        IconButton(onClick = { /* TODO */ }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    VehicleDetailItem(Icons.Outlined.BatteryChargingFull, "${vehicle.batteryPercentage}%", "Battery")
                    VehicleDetailItem(Icons.Outlined.Route, "${vehicle.estRangeKm} km", "Est. Range")
                    VehicleDetailItem(
                        icon = Icons.Outlined.Power,
                        value = vehicle.chargingStatus,
                        label = "Last charged\n${vehicle.lastChargedInfo}"
                    )
                }
            }
        }
    }
}

@Composable
fun ConnectionStatusBadge(status: ConnectionStatus) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = CircleShape,
            color = if (status == ConnectionStatus.CONNECTED) Color(0xFF2E7D32) else Color.Gray,
            modifier = Modifier.size(8.dp)
        ) {}
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = if (status == ConnectionStatus.CONNECTED) "Connected" else "Offline",
            color = if (status == ConnectionStatus.CONNECTED) Color(0xFF2E7D32) else Color.Gray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun VehicleDetailItem(icon: ImageVector, value: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = label, fontSize = 10.sp, color = Color.Gray, lineHeight = 12.sp)
        }
    }
}

@Composable
fun TipsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9).copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Keep your vehicles healthy", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    text = "Regular monitoring and timely charging keeps your EVs in the best condition.",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = { /* TODO */ },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2E7D32))
            ) {
                Text("View Tips", fontSize = 12.sp)
            }
        }
    }
}

val sampleVehicles = listOf(
    Vehicle(
        id = "1",
        name = "EvSpot EV-01",
        model = "Tata Nexon EV Max",
        plate = "MH01AB1234",
        batteryPercentage = 72,
        estRangeKm = 246,
        connectionStatus = ConnectionStatus.CONNECTED,
        chargingStatus = "Not Charging",
        lastChargedInfo = "Today, 07:42 AM",
        isPrimary = true
    ),
    Vehicle(
        id = "2",
        name = "EvSpot Scooter",
        model = "Ather 450X",
        plate = "MH01CD5678",
        batteryPercentage = 68,
        estRangeKm = 89,
        connectionStatus = ConnectionStatus.CONNECTED,
        chargingStatus = "Not Charging",
        lastChargedInfo = "Yesterday, 09:15 PM"
    ),
    Vehicle(
        id = "3",
        name = "Comet EV",
        model = "MG Comet EV",
        plate = "MH01EF9012",
        batteryPercentage = 35,
        estRangeKm = 72,
        connectionStatus = ConnectionStatus.OFFLINE,
        chargingStatus = "Not Charging",
        lastChargedInfo = "2 days ago"
    )
)

@Preview(showBackground = true)
@Composable
fun MyVehiclesScreenPreview() {
    EVSpotTheme {
        MyVehiclesScreen()
    }
}
