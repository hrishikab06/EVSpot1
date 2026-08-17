package com.example.evspot.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.model.EVInfo
import com.example.evspot.R

@Composable
fun VehicleCard(ev: EVInfo, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("YOUR EV", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(ev.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                }
                ConnectionBadge(ev.isConnected)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(100.dp, 70.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ev_car),
                        contentDescription = "Vehicle image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    StatusItem(
                        icon = Icons.Default.BatteryChargingFull,
                        value = "${ev.batteryPercentage}%",
                        label = "Battery",
                        iconColor = if (ev.batteryPercentage > 20) Color(0xFF4CAF50) else Color.Red
                    )
                    Text("${ev.rangeKm} km Est. Range", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    StatusItem(
                        icon = Icons.Default.Thermostat,
                        value = "${ev.temperature}°C",
                        label = "Inside Temp",
                        iconColor = Color(0xFFFF9800) // Orange
                    )
                }
            }
        }
    }
}

@Composable
private fun ConnectionBadge(isConnected: Boolean) {
    val isDark = isSystemInDarkTheme()
    Surface(
        color = if (isConnected) Color(0xFFE8F5E9).copy(alpha = if (isDark) 0.2f else 1.0f) else Color(0xFFFFEBEE).copy(alpha = if (isDark) 0.2f else 1.0f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (isConnected) Color(0xFF4CAF50) else Color.Red)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                if (isConnected) "Connected" else "Disconnected",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isConnected) (if (isDark) Color(0xFF81C784) else Color(0xFF1B5E20)) else (if (isDark) Color(0xFFE57373) else Color(0xFFB71C1C))
            )
        }
    }
}

@Composable
private fun StatusItem(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String, iconColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
