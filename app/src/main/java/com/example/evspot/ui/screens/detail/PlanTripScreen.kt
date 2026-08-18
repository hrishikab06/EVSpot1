package com.example.evspot.ui.screens.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.BuildConfig
import com.example.evspot.data.PlacesRepository
import com.example.evspot.data.RouteRepository
import com.example.evspot.model.ChargingSpot
import com.example.evspot.model.MapConfig
import com.example.evspot.data.api.RetrofitClient
import com.example.evspot.data.api.PlanTripRequest
import com.example.evspot.data.api.PlanTripResponse
import com.example.evspot.data.api.RoutePlanStep
import com.example.evspot.data.api.CandidateStation
import com.example.evspot.ui.components.ChargingMap
import com.example.evspot.ui.components.search.SearchOverlay
import com.example.evspot.ui.theme.EVSpotTheme
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import kotlinx.coroutines.launch
import java.util.Locale

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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val placesRepository = remember { PlacesRepository(context) }
    val routeRepository = remember { RouteRepository(BuildConfig.MAPS_API_KEY) }
    val scaffoldState = rememberBottomSheetScaffoldState()

    var deviceLocation by remember { mutableStateOf<LatLng?>(null) }
    var destinationName by remember { mutableStateOf("Navi Mumbai Airport, Mumbai") }
    var destinationLatLng by remember { mutableStateOf<LatLng?>(null) }
    
    var routePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var chargingSpots by remember { mutableStateOf<List<ChargingSpot>>(emptyList()) }
    
    var totalDistance by remember { mutableStateOf("42 km") }
    var totalTime by remember { mutableStateOf("1 h 25 min") }
    
    var planResponse by remember { mutableStateOf<PlanTripResponse?>(null) }
    var isPlanning by remember { mutableStateOf(false) }

    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }

    LaunchedEffect(searchQuery) {
        if (searchQuery.length > 2) {
            suggestions = placesRepository.getAutocompleteSuggestions(searchQuery)
        }
    }

    val onPlaceSelected: (AutocompletePrediction) -> Unit = { prediction ->
        destinationName = prediction.getPrimaryText(null).toString()
        isSearchActive = false
        scope.launch {
            val latLng = placesRepository.getPlaceLatLng(prediction.placeId)
            destinationLatLng = latLng
            if (latLng != null && deviceLocation != null) {
                val result = routeRepository.getRoute(deviceLocation!!, latLng)
                result?.routes?.firstOrNull()?.let { route ->
                    val path = route.overviewPolyline.decodePath().map { LatLng(it.lat, it.lng) }
                    routePoints = path
                    totalDistance = route.legs.firstOrNull()?.distance?.humanReadable ?: ""
                    totalTime = route.legs.firstOrNull()?.duration?.humanReadable ?: ""
                    
                    // Search for charging stations along route
                    chargingSpots = placesRepository.searchAlongRoute(path, MapConfig.searchRadius.toDouble())
                    
                    // Call backend to plan EV trip
                    isPlanning = true
                    try {
                        val request = PlanTripRequest(
                            current_lat = deviceLocation!!.latitude,
                            current_lng = deviceLocation!!.longitude,
                            destination_lat = latLng.latitude,
                            destination_lng = latLng.longitude,
                            current_soc = 18.0,
                            battery_temp = 31.0,
                            battery_capacity_kwh = 40.5,
                            candidate_stations = chargingSpots.map {
                                CandidateStation(
                                    id = it.id,
                                    name = it.name,
                                    address = it.address,
                                    latitude = it.position.latitude,
                                    longitude = it.position.longitude
                                )
                            }
                        )
                        val response = RetrofitClient.instance.planTrip(request)
                        if (response.isSuccessful) {
                            planResponse = response.body()
                            // Update display values if backend provided them
                            planResponse?.let {
                                totalDistance = String.format(Locale.getDefault(), "%.1f km", it.total_distance_km)
                                totalTime = String.format(Locale.getDefault(), "%.0f min", it.total_trip_time_minutes)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        isPlanning = false
                    }
                }
            }
        }
    }

    if (isSearchActive) {
        BackHandler { isSearchActive = false }
        SearchOverlay(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            suggestions = suggestions,
            onSuggestionClick = onPlaceSelected,
            onClose = { isSearchActive = false }
        )
    } else {
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
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White.copy(alpha = 0.9f)
                    )
                )
            },
            bottomBar = {
                BottomActionButtons()
            }
        ) { innerPadding ->
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetPeekHeight = 280.dp,
                sheetContainerColor = Color.White,
                sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                sheetShadowElevation = 16.dp,
                sheetDragHandle = {
                    BottomSheetDefaults.DragHandle(
                        color = Color.LightGray.copy(alpha = 0.5f)
                    )
                },
                sheetContent = {
                    TripDetailsSheetContent(
                        distance = totalDistance,
                        duration = totalTime,
                        planResponse = planResponse,
                        isPlanning = isPlanning
                    )
                }
            ) { sheetPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // Full screen map as background
                    ChargingMap(
                        modifier = Modifier.fillMaxSize(),
                        bottomPadding = 280.dp,
                        destinationLocation = destinationLatLng,
                        routePoints = routePoints,
                        chargingSpots = chargingSpots,
                        recommendedSpot = planResponse?.recommended_station?.let {
                            ChargingSpot(
                                id = it.id,
                                name = it.name,
                                position = LatLng(it.latitude, it.longitude),
                                address = it.address
                            )
                        },
                        onDeviceLocationChanged = { loc ->
                            if (deviceLocation == null) {
                                deviceLocation = loc
                            }
                        }
                    )

                    // Floating Top UI
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        LocationInputCard(
                            destinationName = destinationName,
                            onDestinationClick = { isSearchActive = true }
                        )
                        TripFilterChips(sampleFilters)
                    }
                }
            }
        }
    }
}

@Composable
fun TripDetailsSheetContent(
    distance: String,
    duration: String,
    planResponse: PlanTripResponse?,
    isPlanning: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TripOverviewCard(
                distance = distance,
                duration = duration,
                planResponse = planResponse,
                isPlanning = isPlanning
            )
        }
        item {
            Text(
                text = "Route Plan",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        item {
            if (isPlanning) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF2E7D32))
                }
            } else if (planResponse != null) {
                RoutePlanCard(planResponse)
            } else {
                RoutePlanCard(sampleRouteStops, sampleTrafficLegs)
            }
        }
        item {
            EstimatedCostCard(planResponse?.charging_cost_inr)
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun LocationInputCard(
    destinationName: String,
    onDestinationClick: () -> Unit
) {
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDestinationClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = Color.Red, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = destinationName, fontSize = 15.sp, color = Color.Black)
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
fun TripOverviewCard(
    distance: String = "42 km",
    duration: String = "1 h 25 min",
    planResponse: PlanTripResponse? = null,
    isPlanning: Boolean = false
) {
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
                if (isPlanning) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = Color(0xFF2E7D32))
                } else {
                    TextButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF2E7D32))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Edit Trip", color = Color(0xFF2E7D32), fontSize = 14.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OverviewItem(Modifier.weight(1f), "Total Distance", distance)
                OverviewVerticalDivider()
                OverviewItem(Modifier.weight(1f), "Total Time (with stop)", duration)
                OverviewVerticalDivider()
                OverviewItem(Modifier.weight(1f), "Est. Energy Needed", "18.2 kWh") // Marking as static for now
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
                        if (planResponse?.recommended_station != null) {
                            Text(text = "Charging stop recommended", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF2E7D32))
                            Text(text = "Stop for ${String.format(Locale.getDefault(), "%.0f", planResponse.charging_time_minutes)} min at ${planResponse.recommended_station.name}", fontSize = 12.sp, color = Color.DarkGray)
                        } else {
                            Text(text = "No charging stop needed", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF2E7D32))
                            Text(text = "Direct route to destination", fontSize = 12.sp, color = Color.DarkGray)
                        }
                    }
                    if (planResponse?.recommended_station != null) {
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
                    }
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
fun RoutePlanCard(plan: PlanTripResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            plan.route_plan.forEachIndexed { index, step ->
                val icon = when (step.type) {
                    "start" -> Icons.Default.Circle
                    "charging" -> Icons.Default.Bolt
                    "destination" -> Icons.Default.Place
                    else -> Icons.Default.Circle
                }
                val iconTint = when (step.type) {
                    "start" -> Color(0xFF2196F3)
                    "charging" -> Color(0xFF2E7D32)
                    "destination" -> Color.Red
                    else -> Color.Gray
                }
                
                RouteTimelineStep(
                    step = step,
                    isLast = index == plan.route_plan.size - 1,
                    icon = icon,
                    iconTint = iconTint
                )
            }
        }
    }
}

@Composable
fun RouteTimelineStep(step: RoutePlanStep, isLast: Boolean, icon: ImageVector, iconTint: Color) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(32.dp)) {
            Surface(
                shape = CircleShape,
                color = if (step.type == "charging") iconTint else Color.White,
                modifier = Modifier.size(24.dp),
                border = if (step.type != "charging") BorderStroke(2.dp, iconTint) else null
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (step.type == "charging") Color.White else iconTint,
                    modifier = Modifier.padding(4.dp)
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(Color(0xFFE0E0E0))
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = step.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    if (step.type == "charging") {
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
                    if (step.charging_time_minutes != null) {
                        Text(text = "${String.format(Locale.getDefault(), "%.0f", step.charging_time_minutes)} min", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    } else if (step.drive_time_minutes != null) {
                        Text(text = "${String.format(Locale.getDefault(), "%.0f", step.drive_time_minutes)} min", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
            
            if (step.distance_km != null) {
                Text(text = "${String.format(Locale.getDefault(), "%.1f", step.distance_km)} km", fontSize = 12.sp, color = Color.Gray)
            }

            if (!isLast) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
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
fun EstimatedCostCard(cost: Double? = null) {
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
                Text(
                    text = cost?.let { String.format(Locale.getDefault(), "₹%.0f", it) } ?: "₹0",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
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
