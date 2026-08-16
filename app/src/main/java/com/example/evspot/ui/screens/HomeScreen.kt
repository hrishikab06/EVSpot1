package com.example.evspot.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.model.EVInfo
import com.example.evspot.navigation.Screen
import com.example.evspot.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    evList: List<EVInfo> = listOf(EVInfo("EVSpot EV-01", 72, 246)),
    onNavigate: (String) -> Unit = {}
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 160.dp,
        sheetContainerColor = Color.White,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetShadowElevation = 16.dp,
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle(
                color = Color.LightGray.copy(alpha = 0.5f)
            )
        },
        sheetContent = {
            DashboardSheetContent(evList, onNavigate)
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Full screen map as background
            ChargingMap(
                modifier = Modifier.fillMaxSize(),
                bottomPadding = 160.dp,
                isLiteMode = true
            )

            // Floating Top UI
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
            ) {
                FloatingTopBar(onNavigate = onNavigate)
                Spacer(modifier = Modifier.height(12.dp))
                SearchBar(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun DashboardSheetContent(
    evList: List<EVInfo>,
    onNavigate: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
            QuickAccessSection(onNavigate)
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
fun QuickAccessSection(onNavigate: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Quick Access",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
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
                title = "Booking status",
                icon = Icons.Default.Event,
                badgeCount = 2,
                onClick = { onNavigate(Screen.UpcomingBookings.route) },
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickAccessCard(
                title = "Past trips",
                icon = Icons.Default.BarChart,
                onClick = { onNavigate(Screen.PastTrips.route) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatingTopBar(onNavigate: (String) -> Unit) {
    // We reuse the components from TopBar but make it floating and transparent
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = com.example.evspot.R.drawable.evspot_logo),
                contentDescription = "EVSpot Logo",
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ev",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 22.sp
            )
            Text(
                text = "Spot",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20),
                fontSize = 22.sp
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = Color.Red,
                        contentColor = Color.White,
                        modifier = Modifier.offset(x = (-4).dp, y = 4.dp)
                    ) {
                        Text("3")
                    }
                }
            ) {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.Black)
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = { onNavigate(Screen.Account.route) }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Account", tint = Color.Black)
            }
        }
    }
}
