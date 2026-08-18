package com.example.evspot.ui.screens.detail

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.model.ChargingSession
import com.example.evspot.model.SessionStatus
import com.example.evspot.ui.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChargingHistoryScreen(onBack: () -> Unit, viewModel: UserViewModel) {
    val sessions = viewModel.sessions
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Charging History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { SummaryCard() }
            item {
                Text(
                    text = "Recent Charging Sessions",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            items(sessions) { session ->
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
            Text("Summary", fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
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
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1B5E20))
        Text(label, fontSize = 10.sp, color = Color(0xFF1B5E20).copy(alpha = 0.7f))
    }
}

@Composable
fun SessionCard(session: ChargingSession) {
    val isCancelled = session.status == SessionStatus.CANCELLED
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(session.stationName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                    Text(session.location, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(session.dateTime, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("₹${session.price}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
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
                    Text("${session.batteryFrom}% → ${session.batteryTo}%", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    Text("${session.energyKwh} kWh", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    Text("${session.durationMinutes} min", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Session Cancelled", color = Color(0xFFD32F2F), fontSize = 12.sp)
            }
        }
    }
}