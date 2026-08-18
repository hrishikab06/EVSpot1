package com.example.evspot.ui.screens.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.evspot.model.*
import com.example.evspot.navigation.Screen
import com.example.evspot.ui.components.ChargingMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import kotlin.math.*

enum class NearbyFilter(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    RECOMMENDED("Recommended", Icons.Default.Star),
    NEAREST("Nearest", Icons.Default.Place),
    CHEAPEST("Cheapest", Icons.Default.CurrencyRupee),
    FASTEST("Fastest", Icons.Default.Bolt),
    RATING("Rating", Icons.Default.ThumbUp)
}

fun calculateEvSpotScore(
    distanceKm: Double,
    rating: Double?,
    userRatingsTotal: Int?,
    priceLevel: Int?
): Double {
    val wDistance = 0.40
    val wPrice = 0.25
    val wRating = 0.15
    val wReliability = 0.10
    val wSuitability = 0.10

    val maxDistance = 5.0 // km
    val maxPrice = 4.0
    val maxRating = 5.0
    val targetRatings = 100.0

    val distanceScore = (1.0 - min(1.0, distanceKm / maxDistance)) * wDistance
    val priceScore = (1.0 - ((priceLevel ?: 2).toDouble() / maxPrice)) * wPrice
    val ratingScore = ((rating ?: 3.0) / maxRating) * wRating
    val reliabilityScore = (min(1.0, (userRatingsTotal ?: 0).toDouble() / targetRatings)) * wReliability
    val suitabilityScore = 0.5 * wSuitability // Neutral suitability for now

    return distanceScore + priceScore + ratingScore + reliabilityScore + suitabilityScore
}

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
    var selectedFilter by remember { mutableStateOf(NearbyFilter.RECOMMENDED) }

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
    val chargerStations = remember(chargingSpots, deviceLocation, selectedFilter, activeSearchCenter) {
        val list = chargingSpots.map { spot ->
            val distance = calculateDistance(
                activeSearchCenter.latitude, activeSearchCenter.longitude,
                spot.position.latitude, spot.position.longitude
            )
            
            val score = calculateEvSpotScore(
                distanceKm = distance,
                rating = spot.rating,
                userRatingsTotal = spot.userRatingsTotal,
                priceLevel = spot.priceLevel
            )

            ChargerStation(
                id = spot.id,
                name = spot.name,
                type = "EV Charging",
                location = spot.address,
                distanceKm = (distance * 10).toInt() / 10.0,
                etaMin = (distance * 2).toInt(),
                pricePerKwh = if (spot.priceLevel != null) 15 + spot.priceLevel * 2 else null,
                rating = spot.rating ?: 0.0,
                reviewCount = spot.userRatingsTotal ?: 0,
                maxSpeedKw = null, // Not available from Places API
                connectors = "CCS2",
                hours = "24/7",
                availability = "Available",
                evSpotScore = score
            )
        }
        
        val sortedList = when (selectedFilter) {
            NearbyFilter.RECOMMENDED -> list.sortedByDescending { it.evSpotScore }
            NearbyFilter.NEAREST -> list.sortedBy { it.distanceKm }
            NearbyFilter.CHEAPEST -> list.sortedBy { it.pricePerKwh ?: Int.MAX_VALUE }
            NearbyFilter.FASTEST -> list.sortedByDescending { it.maxSpeedKw ?: 0 }
            NearbyFilter.RATING -> list.sortedByDescending { it.rating }
        }

        // If no nearby real stations, show sample stations as fallback for demo
        if (sortedList.isEmpty() && !isLoading) {
            sampleStations
        } else {
            sortedList
        }
    }

    val recommendedSpotId = chargerStations.firstOrNull()?.id
    val recommendedSpot = chargingSpots.find { it.id == recommendedSpotId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nearby Charging Stations", color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Map at the top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                ChargingMap(
                    modifier = Modifier.fillMaxSize(),
                    searchCenter = activeSearchCenter,
                    chargingSpots = chargingSpots,
                    recommendedSpot = recommendedSpot,
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

            // Filter UI
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NearbyFilter.entries.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter.label) },
                        leadingIcon = {
                            Icon(
                                filter.icon,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFFE8F5E9).copy(alpha = 0.5f),
                            labelColor = Color(0xFF2E7D32),
                            iconColor = Color(0xFF2E7D32),
                            selectedContainerColor = Color(0xFF2E7D32),
                            selectedLabelColor = Color.White,
                            selectedLeadingIconColor = Color.White
                        ),
                        border = null
                    )
                }
            }

            // List content below map
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column {
                    Text("Nearby Stations", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)
                    val radiusKm = MapConfig.searchRadius / 1000
                    Text("Within $radiusKm km radius", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                if (chargerStations.isEmpty() && !isLoading) {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No charging stations found in this area.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    chargerStations.forEachIndexed { index, station ->
                        StationCard(
                            station = station, 
                            onClick = { onNavigate(Screen.StationDetail.createRoute(station.name)) },
                            isHighlighted = index == 0 // Highlight the top recommendation
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
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
fun StationCard(station: ChargerStation, onClick: () -> Unit, isHighlighted: Boolean = false) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlighted) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surface
        ),
        border = if (isHighlighted) BorderStroke(1.dp, Color(0xFF2E7D32)) else null
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
                        if (station.pricePerKwh != null) "₹${station.pricePerKwh}/kWh" else "Price N/A",
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
                        if (station.maxSpeedKw != null) "${station.maxSpeedKw} kW  " else "Power N/A  ",
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
