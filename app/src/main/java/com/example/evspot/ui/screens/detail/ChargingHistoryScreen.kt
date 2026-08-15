package com.example.evspot.ui.screens.detail

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class SessionStatus { COMPLETED, CANCELLED }

data class ChargingSession(
    val stationName: String,
    val location: String,
    val dateTime: String,
    val price: Int,
    val status: SessionStatus,
    val batteryFrom: Int? = null,
    val batteryTo: Int? = null,
    val energyKwh: Double? = null,
    val durationMinutes: Int? = null
)

val sampleSessions = listOf(
    ChargingSession("GreenCharge Station", "Bandra Kurla Complex, Mumbai", "28 May 2025, 08:30 PM", 312, SessionStatus.COMPLETED, 20, 82, 18.4, 72),
    ChargingSession("VoltPoint Charging Hub", "Powai, Mumbai", "25 May 2025, 06:15 PM", 454, SessionStatus.COMPLETED, 35, 90, 22.7, 88),
    ChargingSession("EcoCharge Station", "Andheri East, Mumbai", "21 May 2025, 10:05 AM", 284, SessionStatus.COMPLETED, 15, 70, 16.2, 65),
    ChargingSession("CityCharge Point", "Dadra West, Mumbai", "18 May 2025, 07:45 PM", 0, SessionStatus.CANCELLED),
    ChargingSession("GreenCharge Station", "Bandra Kurla Complex, Mumbai", "15 May 2025, 09:20 PM", 366, SessionStatus.COMPLETED, 22, 78, 19.6, 80)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChargingHistoryScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Charging History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FBF8)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { SummaryCard() }
            item {
                Text(
                    text = "Recent Charging Sessions",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            items(sampleSessions) { session ->
                SessionCard(session)
            }
        }
    }
}

@Composable
fun SummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Summary", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SummaryStat(Icons.Default.Bolt, "8", "Total Sessions")
                SummaryStat(Icons.Default.BatteryChargingFull, "142 kWh", "Total Energy")
                SummaryStat(Icons.Default.Schedule, "17h 32m", "Total Time")
                SummaryStat(Icons.Default.CurrencyRupee, "₹2,186", "Total Spent")
            }
        }
    }
}

@Composable
fun SummaryStat(icon: ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(shape = CircleShape, color = Color.White, modifier = Modifier.size(36.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(label, fontSize = 10.sp, color = Color.Gray)
    }
}

@Composable
fun SessionCard(session: ChargingSession) {
    val isCancelled = session.status == SessionStatus.CANCELLED
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(session.stationName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(session.location, fontSize = 12.sp, color = Color.Gray)
                    Text(session.dateTime, fontSize = 12.sp, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("₹${session.price}", fontWeight = FontWeight.Bold)
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isCancelled) Color(0xFFFCE8E8) else Color(0xFFE8F5E9)
                    ) {
                        Text(
                            if (isCancelled) "Cancelled" else "Completed",
                            color = if (isCancelled) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            if (!isCancelled) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${session.batteryFrom}% → ${session.batteryTo}%", fontSize = 12.sp)
                    Text("${session.energyKwh} kWh", fontSize = 12.sp)
                    Text("${session.durationMinutes} min", fontSize = 12.sp)
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Session Cancelled", color = Color(0xFFD32F2F), fontSize = 12.sp)
            }
        }
    }
}