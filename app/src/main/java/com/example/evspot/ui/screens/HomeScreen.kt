package com.example.evspot.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.evspot.model.EVInfo
import com.example.evspot.navigation.Screen
import com.example.evspot.ui.components.*

@Composable
fun HomeScreen(
    evList: List<EVInfo> = listOf(EVInfo("EVSpot EV-01", 72, 246)),
    onNavigate: (String) -> Unit = {}
) {
    Scaffold(
        topBar = { EVSpotTopBar() },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            
            item { SearchBar() }
            
            item { ChargingMap() }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionCard(
                        title = "Plan a Trip",
                        subtitle = "Plan your journey with charging stops",
                        icon = Icons.Default.Route,
                        containerColor = Color(0xFF004D40),
                        contentColor = Color.White,
                        onClick = { onNavigate(Screen.TripPlanner.route) },
                        modifier = Modifier.weight(1f)
                    )
                    ActionCard(
                        title = "Find Nearby",
                        subtitle = "Explore charging stations near you",
                        icon = Icons.Default.Bolt,
                        containerColor = Color(0xFFE8F5E9),
                        contentColor = Color(0xFF004D40),
                        onClick = { onNavigate(Screen.NearbyChargers.route) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            items(evList) { ev ->
                VehicleCard(ev = ev)
            }
            
            item { 
                BatteryAlertCard(
                    onClick = { onNavigate(Screen.Health.route) }
                ) 
            }
            
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Quick Access", 
                        fontWeight = FontWeight.Bold, 
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        QuickAccessCard(
                            title = "Charging History",
                            icon = Icons.Default.History,
                            onClick = { onNavigate(Screen.History.route) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickAccessCard(
                            title = "Upcoming Bookings",
                            icon = Icons.Default.Event,
                            badgeCount = 2,
                            onClick = { onNavigate(Screen.UpcomingBookings.route) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        QuickAccessCard(
                            title = "Energy Usage",
                            icon = Icons.Default.BarChart,
                            onClick = { onNavigate(Screen.Usage.route) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickAccessCard(
                            title = "Vehicle Health",
                            icon = Icons.Default.Favorite,
                            onClick = { onNavigate(Screen.Health.route) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
