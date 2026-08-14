package com.example.evspot

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.ui.theme.EVSpotTheme

enum class ChargingStatus {
    COMPLETED, CANCELLED
}

data class ChargingSession(
    val id: String,
    val stationName: String,
    val location: String,
    val dateTime: String,
    val price: Double,
    val status: ChargingStatus,
    val batteryFrom: Int,
    val batteryTo: Int,
    val energyKwh: Double,
    val durationMinutes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChargingHistoryScreen() {
    Scaffold(
        topBar = {
            Column {
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
                                text = "VoltWay",
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
            }
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
                    selected = false,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                    label = { Text("Bookings") },
                    selected = true,
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
                HistoryHeader()
            }
            item {
                SummaryCard()
            }
            item {
                Text(
                    text = "Recent Charging Sessions",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(sampleSessions) { session ->
                ChargingSessionCard(session)
            }
        }
    }
}

@Composable
fun HistoryHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Charging History",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        OutlinedButton(
            onClick = { /* TODO */ },
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.Black)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Filter", color = Color.Black)
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Black)
        }
    }
}

@Composable
fun SummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("01 May - 31 May 2025", fontSize = 12.sp)
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SummaryItem(Modifier.weight(1f), Icons.Default.Bolt, "8", "Total Sessions", Color(0xFFE8F5E9), Color(0xFF2E7D32))
                SummaryVerticalDivider()
                SummaryItem(Modifier.weight(1f), Icons.Outlined.BatteryChargingFull, "142 kWh", "Total Energy", Color(0xFFE8F5E9), Color(0xFF2E7D32))
                SummaryVerticalDivider()
                SummaryItem(Modifier.weight(1f), Icons.Default.Schedule, "17h 32m", "Total Time", Color(0xFFE8F5E9), Color(0xFF2E7D32))
                SummaryVerticalDivider()
                SummaryItem(Modifier.weight(1f), Icons.Default.CurrencyRupee, "₹2,186", "Total Spent", Color(0xFFE8F5E9), Color(0xFF2E7D32))
            }
        }
    }
}

@Composable
fun SummaryVerticalDivider() {
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(1.dp)
            .background(Color(0xFFEEEEEE))
    )
}

@Composable
fun SummaryItem(modifier: Modifier, icon: ImageVector, value: String, label: String, bgColor: Color, iconColor: Color) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = bgColor,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = label, fontSize = 10.sp, color = Color.Gray)
    }
}

@Composable
fun ChargingSessionCard(session: ChargingSession) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                val iconBgColor = when {
                    session.status == ChargingStatus.CANCELLED -> Color(0xFFFFEBEE)
                    session.stationName.contains("VoltPoint") -> Color(0xFFFFF3E0)
                    else -> Color(0xFFE8F5E9)
                }
                val iconTint = when {
                    session.status == ChargingStatus.CANCELLED -> Color(0xFFD32F2F)
                    session.stationName.contains("VoltPoint") -> Color(0xFFF57C00)
                    else -> Color(0xFF2E7D32)
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = iconBgColor,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.EvStation,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(24.dp)
                        )
                        if (session.status == ChargingStatus.CANCELLED) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White,
                                modifier = Modifier
                                    .size(14.dp)
                                    .align(Alignment.BottomEnd)
                                    .padding(1.dp)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = null,
                                    tint = Color.Red,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = session.stationName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = session.location, fontSize = 12.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.CalendarToday, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = session.dateTime, fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${session.price.toInt()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    StatusBadge(session.status)
                }
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            if (session.status == ChargingStatus.COMPLETED) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFF5F5F5))
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailItem(Icons.Outlined.BatteryChargingFull, "${session.batteryFrom}% → ${session.batteryTo}%", "Battery")
                    DetailItem(Icons.Default.Bolt, "${session.energyKwh} kWh", "Energy")
                    DetailItem(Icons.Default.Schedule, "${session.durationMinutes / 60}h ${session.durationMinutes % 60}m", "Duration")
                }
            } else {
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Session Cancelled", color = Color.Red, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: ChargingStatus) {
    val bgColor = if (status == ChargingStatus.COMPLETED) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
    val textColor = if (status == ChargingStatus.COMPLETED) Color(0xFF2E7D32) else Color(0xFFD32F2F)
    val text = if (status == ChargingStatus.COMPLETED) "Completed" else "Cancelled"

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = bgColor
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun DetailItem(icon: ImageVector, value: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(text = label, fontSize = 10.sp, color = Color.Gray)
        }
    }
}

val sampleSessions = listOf(
    ChargingSession("1", "GreenCharge Station", "Bandra Kurla Complex, Mumbai", "28 May 2025, 08:30 PM", 312.0, ChargingStatus.COMPLETED, 20, 82, 18.4, 72),
    ChargingSession("2", "VoltPoint Charging Hub", "Powai, Mumbai", "25 May 2025, 06:15 PM", 454.0, ChargingStatus.COMPLETED, 35, 90, 22.7, 88),
    ChargingSession("3", "EcoCharge Station", "Andheri East, Mumbai", "21 May 2025, 10:05 AM", 284.0, ChargingStatus.COMPLETED, 15, 70, 16.2, 65),
    ChargingSession("4", "CityCharge Point", "Dadra West, Mumbai", "18 May 2025, 07:45 PM", 0.0, ChargingStatus.CANCELLED, 0, 0, 0.0, 0),
    ChargingSession("5", "GreenCharge Station", "Bandra Kurla Complex, Mumbai", "15 May 2025, 09:20 PM", 366.0, ChargingStatus.COMPLETED, 22, 78, 19.6, 80)
)

@Preview(showBackground = true)
@Composable
fun ChargingHistoryScreenPreview() {
    EVSpotTheme {
        ChargingHistoryScreen()
    }
}
