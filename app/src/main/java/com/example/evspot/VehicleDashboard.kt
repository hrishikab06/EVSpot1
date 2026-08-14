package com.example.evspot

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.ui.theme.*
import java.util.Locale

data class Vehicle(
    val name: String,
    val model: String,
    val registration: String,
    val battery: Int,
    val range: Int,
    val temperature: Int,
    val batteryHealth: Int,
    val charging: Boolean,
    val odometer: Int,
    val efficiency: Double,
    val averageSpeed: Int
)

val mockVehicle = Vehicle(
    name = "VoltWay EV-01",
    model = "Tata Nexon EV Max",
    registration = "MH01AB1234",
    battery = 72,
    range = 246,
    temperature = 31,
    batteryHealth = 96,
    charging = false,
    odometer = 12458,
    efficiency = 5.2,
    averageSpeed = 42
)

@Composable
fun VehicleDashboard(modifier: Modifier = Modifier) {
    Scaffold(
        bottomBar = { BottomNavigationBar() },
        containerColor = BackgroundGray
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item { VehicleHeader() }
            item { TitleRow() }
            item { VehicleHeroCard(mockVehicle) }
            item { ChargingStatusCard(mockVehicle.charging) }
            item { VehicleHealthSection(mockVehicle) }
            item { OdometerEfficiencyCard(mockVehicle) }
            item { QuickActionsSection() }
        }
    }
}

@Composable
fun VehicleHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = VoltGreen,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = "Volt",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Way",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = VoltGreen
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box {
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
                }
                Surface(
                    color = ErrorRed,
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 8.dp, end = 8.dp)
                        .size(16.dp)
                ) {
                    Text(
                        text = "3",
                        color = Color.White,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = CircleShape,
                color = Color.LightGray,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun TitleRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "My Vehicle",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = DarkNavy
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = PaleGreen,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = VoltGreen,
                        shape = CircleShape,
                        modifier = Modifier.size(6.dp)
                    ) {}
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Connected",
                        color = VoltGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SecondaryGray)
        }
    }
}

@Composable
fun VehicleHeroCard(vehicle: Vehicle) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(24.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = vehicle.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = DarkNavy
                    )
                    Text(
                        text = vehicle.model,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecondaryGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = BackgroundGray,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = vehicle.registration,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = DarkNavy
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                modifier = Modifier.size(12.dp),
                                tint = SecondaryGray
                            )
                        }
                    }
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for nexon_ev.png
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Vehicle Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = PaleGreen,
                    shape = CircleShape,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Route,
                        contentDescription = null,
                        tint = VoltGreen,
                        modifier = Modifier.padding(6.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = vehicle.range.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkNavy
                        )
                        Text(
                            text = " km",
                            fontSize = 14.sp,
                            color = SecondaryGray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    Text(
                        text = "Est. Range",
                        fontSize = 12.sp,
                        color = SecondaryGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem(
                    icon = Icons.Default.BatteryChargingFull,
                    value = "${vehicle.battery}%",
                    label = "Battery Level",
                    modifier = Modifier.weight(1f)
                )
                VerticalDivider(modifier = Modifier.height(40.dp).padding(horizontal = 8.dp))
                MetricItem(
                    icon = Icons.Default.Thermostat,
                    value = "${vehicle.temperature}°C",
                    label = "Battery Temp.",
                    modifier = Modifier.weight(1f)
                )
                VerticalDivider(modifier = Modifier.height(40.dp).padding(horizontal = 8.dp))
                MetricItem(
                    icon = Icons.Default.Favorite,
                    value = "${vehicle.batteryHealth}%",
                    label = "Battery Health",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(BackgroundGray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(vehicle.battery / 100f)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(VoltGreen)
                )
            }
        }
    }
}

@Composable
fun MetricItem(icon: ImageVector, value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = VoltGreen, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkNavy)
        }
        Text(text = label, fontSize = 10.sp, color = SecondaryGray)
    }
}

@Composable
fun ChargingStatusCard(isCharging: Boolean) {
    val context = LocalContext.current
    Card(
        colors = CardDefaults.cardColors(containerColor = PaleGreen),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (isCharging) Icons.Default.EvStation else Icons.Default.ElectricalServices,
                        contentDescription = null,
                        tint = VoltGreen,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isCharging) "Charging" else "Not Charging",
                        fontWeight = FontWeight.Bold,
                        color = DarkNavy
                    )
                    Text(
                        text = if (isCharging) "Charging at 7.2 kW" else "Plug in to start charging",
                        style = MaterialTheme.typography.bodySmall,
                        color = SecondaryGray
                    )
                }
            }

            OutlinedButton(
                onClick = { Toast.makeText(context, "Charging stations coming next", Toast.LENGTH_SHORT).show() },
                border = BorderStroke(1.dp, VoltGreen),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = VoltGreen)
            ) {
                Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Find Charger", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun VehicleHealthSection(vehicle: Vehicle) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Vehicle Health",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DarkNavy
            )
            TextButton(onClick = { /* TODO */ }) {
                Text("View All", color = VoltGreen, fontWeight = FontWeight.Bold)
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = VoltGreen, modifier = Modifier.size(16.dp))
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item {
                HealthCard(
                    icon = Icons.Default.BatteryFull,
                    title = "Battery Health",
                    value = "${vehicle.batteryHealth}%",
                    status = "Excellent"
                )
            }
            item {
                HealthCard(
                    icon = Icons.Default.SettingsInputComponent,
                    title = "Motor Status",
                    value = "Good",
                    status = "No issues"
                )
            }
            item {
                HealthCard(
                    icon = Icons.Default.Thermostat,
                    title = "Temperature",
                    value = "${vehicle.temperature}°C",
                    status = "Normal"
                )
            }
            item {
                HealthCard(
                    icon = Icons.Default.Shield,
                    title = "System Status",
                    value = "Good",
                    status = "All systems normal"
                )
            }
        }
    }
}

@Composable
fun HealthCard(icon: ImageVector, title: String, value: String, status: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .width(140.dp)
            .height(160.dp)
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                color = PaleGreen,
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(icon, contentDescription = null, tint = VoltGreen, modifier = Modifier.padding(10.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontSize = 12.sp, color = SecondaryGray)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkNavy)
            Text(status, fontSize = 10.sp, color = VoltGreen)
        }
    }
}

@Composable
fun OdometerEfficiencyCard(vehicle: Vehicle) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(24.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Odometer & Efficiency",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DarkNavy
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EfficiencyMetric(
                    icon = Icons.Default.PinDrop,
                    value = "${String.format(Locale.getDefault(), "%,d", vehicle.odometer)} km",
                    label = "Total Km Driven",
                    modifier = Modifier.weight(1f)
                )
                VerticalDivider(modifier = Modifier.height(40.dp))
                EfficiencyMetric(
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    value = "${vehicle.efficiency} km/kWh",
                    label = "Avg. Efficiency",
                    modifier = Modifier.weight(1f)
                )
                VerticalDivider(modifier = Modifier.height(40.dp))
                EfficiencyMetric(
                    icon = Icons.Default.Speed,
                    value = "${vehicle.averageSpeed} km/h",
                    label = "Avg. Speed",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun EfficiencyMetric(icon: ImageVector, value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, tint = VoltGreen, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkNavy)
        Text(text = label, fontSize = 10.sp, color = SecondaryGray, textAlign = TextAlign.Center)
    }
}

@Composable
fun QuickActionsSection() {
    Column {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = DarkNavy,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item { ActionCard(Icons.Default.Description, "Vehicle Details") }
            item { ActionCard(Icons.Default.Build, "Service History") }
            item { ActionCard(Icons.Default.Download, "Software Update") }
            item { ActionCard(Icons.Default.Lock, "Remote Lock") }
            item { ActionCard(Icons.Default.Settings, "Settings") }
        }
    }
}

@Composable
fun ActionCard(icon: ImageVector, title: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .width(100.dp)
            .height(110.dp)
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = VoltGreen, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                color = DarkNavy,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = { /* TODO */ },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = SecondaryGray,
                unselectedTextColor = SecondaryGray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Map, contentDescription = "Map") },
            label = { Text("Map") },
            selected = false,
            onClick = { /* TODO */ },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = SecondaryGray,
                unselectedTextColor = SecondaryGray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DirectionsCar, contentDescription = "Vehicle") },
            label = { Text("Vehicle") },
            selected = true,
            onClick = { /* TODO */ },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = VoltGreen,
                selectedTextColor = VoltGreen,
                indicatorColor = PaleGreen
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Bookings") },
            label = { Text("Bookings") },
            selected = false,
            onClick = { /* TODO */ },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = SecondaryGray,
                unselectedTextColor = SecondaryGray
            )
        )
    }
}

@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun VehicleDashboardPreview() {
    EVSpotTheme {
        VehicleDashboard()
    }
}
