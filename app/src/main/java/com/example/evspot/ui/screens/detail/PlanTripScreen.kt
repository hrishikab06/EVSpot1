package com.example.evspot.ui.screens.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.ui.components.ChargingMap
import com.example.evspot.ui.theme.EVSpotTheme

data class TripFilter(
    val label: String,
    val icon: ImageVector,
    val isSelected: Boolean = false
)

data class RouteStop(
    val name: String,
    val address: String,
    val batteryPercent: String,
    val time: String,
    val isRecommendedStop: Boolean = false,
    val stopDuration: String? = null,
    val icon: ImageVector,
    val iconTint: Color
)

data class TrafficLeg(
    val distance: String,
    val duration: String,
    val condition: String,
    val conditionColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanTripScreen(
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Text(
                        text = "Plan a Trip",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FBF8))
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    LocationInputCard()
                }
                item {
                    TripFilterChips(sampleFilters)
                }
                item {
                    MapPlaceholder()
                }
                item {
                    TripOverviewCard()
                }
                item {
                    Text(
                        text = "Route Plan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                item {
                    RoutePlanCard(sampleRouteStops, sampleTrafficLegs)
                }
                item {
                    EstimatedCostCard()
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            BottomActionButtons()
        }
    }
}

@Composable
fun LocationInputCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = CircleShape, color = Color(0xFF2E7D32), modifier = Modifier.size(8.dp)) {}
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Current Location", fontSize = 15.sp, color = Color.Black)
                }
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFF5F5F5))
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = Color.Red, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Navi Mumbai Airport, Mumbai", fontSize = 15.sp, color = Color.Black)
                }
            }
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.SwapVert, contentDescription = "Swap", tint = Color.Gray)
            }
        }
    }
}

@Composable
fun TripFilterChips(filters: List<TripFilter>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            FilterChip(
                selected = filter.isSelected,
                onClick = { /* TODO */ },
                label = { Text(text = filter.label) },
                leadingIcon = {
                    Icon(
                        imageVector = filter.icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                shape = RoundedCornerShape(24.dp),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.White,
                    labelColor = Color.Black,
                    iconColor = Color.Black,
                    selectedContainerColor = Color(0xFF1B5E20),
                    selectedLabelColor = Color.White,
                    selectedLeadingIconColor = Color.White
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = filter.isSelected,
                    borderColor = Color.LightGray,
                    selectedBorderColor = Color.Transparent,
                    borderWidth = 1.dp
                )
            )
        }
    }
}

@Composable
fun MapPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(Color(0xFFE0E0E0), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        ChargingMap(
            modifier = Modifier.fillMaxSize(),
            isLiteMode = true
        )
    }
}

@Composable
fun TripOverviewCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Trip Overview", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                TextButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF2E7D32))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Edit Trip", color = Color(0xFF2E7D32), fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OverviewItem(Modifier.weight(1f), "Total Distance", "42 km")
                OverviewVerticalDivider()
                OverviewItem(Modifier.weight(1f), "Total Time (with stop)", "1 h 25 min")
                OverviewVerticalDivider()
                OverviewItem(Modifier.weight(1f), "Est. Energy Needed", "18.2 kWh")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFE8F5E9).copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Charging stop recommended", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF2E7D32))
                        Text(text = "1 stop for 20 min at GreenCharge Station", fontSize = 12.sp, color = Color.DarkGray)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun OverviewItem(modifier: Modifier, label: String, value: String) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 10.sp, color = Color.Gray)
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
    }
}

@Composable
fun OverviewVerticalDivider() {
    Box(modifier = Modifier.height(32.dp).width(1.dp).background(Color(0xFFEEEEEE)))
}

@Composable
fun RoutePlanCard(stops: List<RouteStop>, legs: List<TrafficLeg>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            stops.forEachIndexed { index, stop ->
                RouteTimelineStop(
                    stop = stop,
                    isLast = index == stops.size - 1,
                    legInfo = if (index < legs.size) legs[index] else null
                )
            }
        }
    }
}

@Composable
fun RouteTimelineStop(stop: RouteStop, isLast: Boolean, legInfo: TrafficLeg?) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(32.dp)) {
            Surface(
                shape = CircleShape,
                color = if (stop.isRecommendedStop) stop.iconTint else Color.White,
                modifier = Modifier.size(24.dp),
                border = if (!stop.isRecommendedStop) BorderStroke(2.dp, stop.iconTint) else null
            ) {
                Icon(
                    imageVector = stop.icon,
                    contentDescription = null,
                    tint = if (stop.isRecommendedStop) Color.White else stop.iconTint,
                    modifier = Modifier.padding(4.dp)
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(80.dp) // Adjusted to fit leg info
                        .background(Color(0xFFE0E0E0))
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = stop.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(text = stop.address, fontSize = 12.sp, color = Color.Gray)
                    if (stop.isRecommendedStop) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = "Recommended Stop",
                                color = Color(0xFF2E7D32),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = stop.batteryPercent, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(text = stop.time, fontSize = 12.sp, color = Color.Gray)
                    if (stop.stopDuration != null) {
                        Text(text = stop.stopDuration, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
            
            if (legInfo != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Drive ${legInfo.distance} • ${legInfo.duration}", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = legInfo.conditionColor.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = legInfo.condition,
                            color = legInfo.conditionColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFF5F5F5))
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun EstimatedCostCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Estimated Cost", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = "(including charging)", fontSize = 12.sp, color = Color.Gray)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "₹312 – ₹340", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Black)
            }
        }
    }
}

@Composable
fun BottomActionButtons() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { /* TODO */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2E7D32))
            ) {
                Icon(Icons.Default.StarOutline, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Trip")
            }
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
            ) {
                Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Navigation")
            }
        }
    }
}

val sampleFilters = listOf(
    TripFilter("Recommended", Icons.Default.AutoAwesome, isSelected = true),
    TripFilter("Fastest", Icons.Default.Schedule)
)

val sampleRouteStops = listOf(
    RouteStop(
        name = "Your Location",
        address = "Bandra Kurla Complex, Mumbai",
        batteryPercent = "100%",
        time = "9:30 AM",
        icon = Icons.Default.Circle,
        iconTint = Color(0xFF2196F3)
    ),
    RouteStop(
        name = "GreenCharge Station",
        address = "Near Chembur, Mumbai",
        batteryPercent = "18% → 62%",
        time = "20 min charge",
        isRecommendedStop = true,
        icon = Icons.Default.Bolt,
        iconTint = Color(0xFF2E7D32)
    ),
    RouteStop(
        name = "Navi Mumbai Airport",
        address = "Navi Mumbai, Maharashtra",
        batteryPercent = "31%",
        time = "10:55 AM",
        icon = Icons.Default.Place,
        iconTint = Color.Red
    )
)

val sampleTrafficLegs = listOf(
    TrafficLeg("22 km", "40 min", "Good Traffic", Color(0xFF2E7D32)),
    TrafficLeg("20 km", "45 min", "Moderate Traffic", Color(0xFFF57C00))
)

@Preview(showBackground = true)
@Composable
fun PlanTripScreenPreview() {
    EVSpotTheme {
        PlanTripScreen()
    }
}
