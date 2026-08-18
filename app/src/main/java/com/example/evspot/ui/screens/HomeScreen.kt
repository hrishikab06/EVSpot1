package com.example.evspot.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
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
                title = "Wallet",
                icon = Icons.Default.AccountBalanceWallet,
                onClick = { onNavigate(Screen.Wallet.route) },
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickAccessCard(
                title = "Vehicle Health",
                icon = Icons.Default.Favorite,
                onClick = { onNavigate(Screen.Health.route) },
                modifier = Modifier.weight(0.5f)
            )
            Spacer(modifier = Modifier.weight(0.5f))
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
    // We reuse the components from TopBar but make it floating and transparent
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = com.example.evspot.R.drawable.evspot_logo),
                contentDescription = "EVSpot Logo",
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            
            // MODERN SEARCH BAR replacing "EvSpot" text
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp),
                placeholder = { 
                    Text(
                        "Search location...", 
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant 
                    ) 
                },
                leadingIcon = { 
                    Icon(
                        Icons.Default.Search, 
                        contentDescription = null, 
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    ) 
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty() || isSearching) {
                        IconButton(onClick = onClearSearch) {
                            Icon(
                                Icons.Default.Close, 
                                contentDescription = "Clear search", 
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

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
                IconButton(onClick = { onNavigate(Screen.Notifications.route) }) {
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
