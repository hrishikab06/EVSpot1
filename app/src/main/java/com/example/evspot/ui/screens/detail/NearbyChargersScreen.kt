package com.example.evspot.ui.screens.detail

import com.example.evspot.navigation.Screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.data.PlacesRepository
import com.example.evspot.model.ChargingSpot
import com.example.evspot.model.MapConfig
import com.example.evspot.model.ChargerStation
import com.example.evspot.model.ChargingSlot
import com.example.evspot.model.sampleStations
import com.example.evspot.ui.components.ChargingMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import kotlin.math.*

fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val r = 6371 // Radius of the earth
    val latDistance = Math.toRadians(lat2 - lat1)
    val lonDistance = Math.toRadians(lon2 - lon1)
    val a = sin(latDistance / 2) * sin(latDistance / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(lonDistance / 2) * sin(lonDistance / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return r * c
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyChargersScreen(onBack: () -> Unit, onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val placesRepository = remember { PlacesRepository(context) }

    var deviceLocation by remember { mutableStateOf<LatLng?>(null) }
    var searchCenter by remember { mutableStateOf<LatLng?>(null) }
    var chargingSpots by remember { mutableStateOf<List<ChargingSpot>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val activeSearchCenter = searchCenter ?: deviceLocation ?: MapConfig.DEFAULT_LOCATION

    LaunchedEffect(activeSearchCenter) {
        isLoading = true
        chargingSpots = placesRepository.searchChargingStations(
            activeSearchCenter,
            MapConfig.searchRadius.toDouble()
        )
        isLoading = false
    }

    // Convert ChargingSpot to ChargerStation for the list UI
    val chargerStations = remember(chargingSpots, deviceLocation) {
        val list = chargingSpots.map { spot ->
            val distance = deviceLocation?.let {
                calculateDistance(it.latitude, it.longitude, spot.position.latitude, spot.position.longitude)
            } ?: 0.0
            
            ChargerStation(
                name = spot.name,
                type = "EV Charging",
                location = spot.address,
                distanceKm = (distance * 10).toInt() / 10.0,
                etaMin = (distance * 2).toInt(),
                pricePerKwh = 15,
                rating = 4.5,
                reviewCount = 50,
                maxSpeedKw = 50,
                connectors = "CCS2",
                hours = "24/7",
                availability = "Available"
            )
        }
        
        // If no nearby real stations, show sample stations as fallback for demo
        if (list.isEmpty() && !isLoading) {
            sampleStations
        } else {
            list.sortedBy { it.distanceKm }
        }
    }

    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Nearby Charging Stations", color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        sheetPeekHeight = 300.dp, // Increased peek height to show list below map
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetShadowElevation = 16.dp,
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle(
                color = Color.LightGray.copy(alpha = 0.5f)
            )
        },
        sheetContent = {
            NearbyStationsSheetContent(
                stations = chargerStations,
                isLoading = isLoading,
                onStationClick = { station ->
                    onNavigate(Screen.StationDetail.createRoute(station.name))
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Full screen map as background
            ChargingMap(
                modifier = Modifier.fillMaxSize(),
                bottomPadding = 300.dp,
                searchCenter = activeSearchCenter,
                chargingSpots = chargingSpots,
                onDeviceLocationChanged = { loc ->
                    if (deviceLocation == null) {
                        deviceLocation = loc
                    }
                },
                onMapClick = { loc ->
                    searchCenter = loc
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
}

@Composable
fun NearbyStationsSheetContent(
    stations: List<ChargerStation>,
    isLoading: Boolean,
    onStationClick: (ChargerStation) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text("Nearby Stations", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                val radiusKm = MapConfig.searchRadius / 1000
                Text("Within $radiusKm km radius", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (stations.isEmpty() && !isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No charging stations found in this area.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            items(stations) { station ->
                StationCard(station, onClick = { onStationClick(station) })
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StationCard(station: ChargerStation, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            station.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f, fill = false),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFE8F5E9).copy(alpha = 0.2f)) {
                            Text(
                                station.type,
                                fontSize = 10.sp,
                                color = Color(0xFF81C784),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(station.location, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        "${station.distanceKm} km • ${station.etaMin} min",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (station.isFull) Color(0xFFFCE8E8).copy(alpha = 0.2f) else Color(0xFFE8F5E9).copy(alpha = 0.2f)
                    ) {
                        Text(
                            station.availability,
                            fontSize = 11.sp,
                            color = if (station.isFull) Color(0xFFE57373) else Color(0xFF81C784),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        "₹${station.pricePerKwh}/kWh",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "★ ${station.rating} (${station.reviewCount})",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Text(
                        "${station.maxSpeedKw} kW  ",
                        fontSize = 12.sp,
                        modifier = Modifier.alignByBaseline(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "${station.connectors}  ",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.alignByBaseline()
                    )
                    Text(
                        station.hours,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.alignByBaseline()
                    )
                }
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8F5E9).copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("View Details", color = Color(0xFF81C784), fontSize = 12.sp)
                }
            }
        }
    }
}
