package com.example.evspot.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.evspot.data.PlacesRepository
import com.example.evspot.model.ChargingSpot
import com.example.evspot.model.EVInfo
import com.example.evspot.model.MapConfig
import com.example.evspot.navigation.Screen
import com.example.evspot.ui.components.*
import com.example.evspot.ui.components.search.SuggestionList
import com.example.evspot.ui.theme.EVSpotTheme
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit = {},
    vehicleViewModel: VehicleViewModel = viewModel()
) {
    val bmsStatus by vehicleViewModel.bmsStatus.collectAsState()
    
    // Live vehicle data derived from centralized BMS simulation
    val evList = bmsStatus?.let { status ->
        listOf(EVInfo("EVSpot EV-01", status.soc.toInt(), status.remainingRange.toInt(), temperature = status.batteryTemp.toInt()))
    } ?: listOf(EVInfo("EVSpot EV-01", 72, 246))

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val placesRepository = remember { PlacesRepository(context) }
    val scaffoldState = rememberBottomSheetScaffoldState()

    var deviceLocation by remember { mutableStateOf<LatLng?>(null) }
    var searchCenter by remember { mutableStateOf<LatLng?>(null) }
    var chargingSpots by remember { mutableStateOf<List<ChargingSpot>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }

    val activeSearchCenter = searchCenter ?: deviceLocation ?: MapConfig.DEFAULT_LOCATION

    // HIDE TOP BAR WHEN EXPANDED
    val isSheetExpanded = scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded

    LaunchedEffect(searchQuery) {
        if (searchQuery.length > 2) {
            suggestions = placesRepository.getAutocompleteSuggestions(searchQuery)
        } else {
            suggestions = emptyList()
        }
    }

    LaunchedEffect(activeSearchCenter, searchCenter) {
        // Only search if we have a selected search center
        if (searchCenter != null) {
            isLoading = true
            chargingSpots = placesRepository.searchChargingStations(
                activeSearchCenter,
                5000.0 // Exactly 5 km radius
            )
            isLoading = false
        } else {
            chargingSpots = emptyList()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 160.dp,
            sheetContainerColor = MaterialTheme.colorScheme.surface,
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
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                // Full screen map as background
                ChargingMap(
                    modifier = Modifier.fillMaxSize(),
                    bottomPadding = 160.dp,
                    isLiteMode = false, // ENABLE INTERACTIVITY
                    searchCenter = searchCenter,
                    searchRadius = 5000.0,
                    chargingSpots = chargingSpots,
                    onDeviceLocationChanged = { loc ->
                        if (deviceLocation == null) {
                            deviceLocation = loc
                        }
                    },
                    onMapClick = { loc ->
                        searchCenter = loc
                        searchQuery = "Selected Location"
                    }
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }

        // Floating Top UI - ABOVE EVERYTHING ELSE
        if (!isSheetExpanded) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp) // PADDING AROUND THE CARD
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(24.dp), // HIGHLY ROUNDED CORNERS
                shadowElevation = 12.dp // PRONOUNCED SHADOW
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    FloatingTopBar(
                        onNavigate = onNavigate,
                        isSearching = searchCenter != null,
                        onClearSearch = {
                            searchCenter = null
                            searchQuery = ""
                            isSearchActive = false
                        },
                        searchQuery = searchQuery,
                        onSearchQueryChange = {
                            searchQuery = it
                            isSearchActive = it.isNotEmpty()
                        }
                    )

                    // In-place Search Suggestions - INSIDE THE TOP BAR AREA TO ENSURE LAYER ORDER
                    if (isSearchActive && suggestions.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .heightIn(max = 400.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            SuggestionList(
                                suggestions = suggestions,
                                onSuggestionClick = { prediction ->
                                    searchQuery = prediction.getPrimaryText(null).toString()
                                    isSearchActive = false
                                    scope.launch {
                                        val latLng = placesRepository.getPlaceLatLng(prediction.placeId)
                                        searchCenter = latLng
                                    }
                                },
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
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
                    containerColor = Color(0xFF004D43),
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
            color = MaterialTheme.colorScheme.onSurface
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
fun FloatingTopBar(
    onNavigate: (String) -> Unit,
    isSearching: Boolean,
    onClearSearch: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // LOGO SECTION
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Image(
                painter = painterResource(id = com.example.evspot.R.drawable.evspot_logo),
                contentDescription = "EVSpot Logo",
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = "EVSpot",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Drive Green",
                fontSize = 7.sp,
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold
            )
        }

        // SEARCH BAR SECTION
        Box(
            modifier = Modifier
                .weight(1f)
                .height(44.dp) // SLIGHTLY SLEEKER
                .background(
                    color = Color(0xFFF1F8E9).copy(alpha = 0.6f), // LIGHT GREENISH TINT
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                
                // Real Input
                BasicTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                "Search location or charging station",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                maxLines = 1
                            )
                        }
                        innerTextField()
                    }
                )

                if (searchQuery.isNotEmpty() || isSearching) {
                    IconButton(onClick = onClearSearch, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                    }
                }

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .width(1.dp)
                        .height(20.dp)
                        .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                )

                // Filter Icon
                Icon(
                    Icons.Default.Tune,
                    contentDescription = "Filter",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // ACTIONS SECTION
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = Color(0xFF4CAF50), // BRIGHTER GREEN FOR BADGE
                        contentColor = Color.White,
                        modifier = Modifier.offset(x = (-4).dp, y = 4.dp)
                    ) {
                        Text("3", fontSize = 10.sp)
                    }
                }
            ) {
                IconButton(onClick = { onNavigate(Screen.Notifications.route) }, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Default.NotificationsNone, 
                        contentDescription = "Notifications", 
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Vertical Divider
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .width(1.dp)
                    .height(24.dp)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
            )

            Surface(
                onClick = { onNavigate(Screen.Account.route) },
                shape = CircleShape,
                color = Color(0xFFE8F5E9),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Person, 
                        contentDescription = "Account", 
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardSheetContentPreview() {
    val sampleEvList = listOf(
        EVInfo("EVSpot EV-01", 72, 246, temperature = 28)
    )
    EVSpotTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            DashboardSheetContent(
                evList = sampleEvList,
                onNavigate = {}
            )
        }
    }
}
