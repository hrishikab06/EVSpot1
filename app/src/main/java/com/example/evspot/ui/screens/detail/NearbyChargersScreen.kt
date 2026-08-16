package com.example.evspot.ui.screens.detail

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
import com.example.evspot.ui.components.ChargingMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

data class ChargerStation(
    val name: String,
    val type: String,
    val location: String,
    val distanceKm: Double,
    val etaMin: Int,
    val pricePerKwh: Int,
    val rating: Double,
    val reviewCount: Int,
    val maxSpeedKw: Int,
    val connectors: String,
    val hours: String,
    val availability: String,
    val isFull: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyChargersScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val placesRepository = remember { PlacesRepository(context) }
    
    val scaffoldState = rememberBottomSheetScaffoldState()
    
    var deviceLocation by remember { mutableStateOf<LatLng?>(null) }
    var searchCenter by remember { mutableStateOf<LatLng?>(null) }
    var chargingSpots by remember { mutableStateOf<List<ChargingSpot>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val activeSearchCenter = searchCenter ?: deviceLocation

    LaunchedEffect(activeSearchCenter) {
        activeSearchCenter?.let { center ->
            isLoading = true
            chargingSpots = placesRepository.searchChargingStations(
                center,
                MapConfig.searchRadius.toDouble()
            )
            isLoading = false
        }
    }

    // Convert ChargingSpot to ChargerStation for the list UI
    val chargerStations = chargingSpots.map { spot ->
        ChargerStation(
            name = spot.name,
            type = "EV Charging",
            location = spot.address,
            distanceKm = 0.0, // We could calculate this
            etaMin = 0,
            pricePerKwh = 15, // Dummy
            rating = 4.5, // Dummy
            reviewCount = 50, // Dummy
            maxSpeedKw = 50, // Dummy
            connectors = "CCS2", // Dummy
            hours = "24/7",
            availability = "Available"
        )
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 320.dp,
        sheetContainerColor = Color.White,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetShadowElevation = 16.dp,
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle(
                color = Color.LightGray.copy(alpha = 0.5f)
            )
        },
        sheetContent = {
            NearbyStationsSheetContent(chargerStations, isLoading)
        },
        topBar = {
            TopAppBar(
                title = { Text("Nearby Charging Stations") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Map Background
            ChargingMap(
                modifier = Modifier.fillMaxSize(),
                bottomPadding = 320.dp,
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

            // Floating Search Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = if (searchCenter != null) "Selected Location" else "Current Location",
                        onValueChange = { },
                        readOnly = true,
                        placeholder = { Text("Search location or station") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchCenter != null) {
                                IconButton(onClick = { searchCenter = null }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear search")
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color(0xFF2E7D32)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedButton(
                        onClick = { /* TODO: Open Filter */ },
                        modifier = Modifier.height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                    ) {
                        Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Filter")
                    }
                }
            }
            
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
fun NearbyStationsSheetContent(stations: List<ChargerStation>, isLoading: Boolean) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text("Nearby Stations", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                val radiusKm = MapConfig.searchRadius / 1000
                Text("Within $radiusKm km radius", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        
        if (stations.isEmpty() && !isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No charging stations found in this area.", color = Color.Gray)
                }
            }
        } else {
            items(stations) { station ->
                StationCard(station)
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StationCard(station: ChargerStation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFE8F5E9)) {
                            Text(
                                station.type,
                                fontSize = 10.sp,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(station.location, fontSize = 12.sp, color = Color.Gray)
                    Text(
                        "${station.distanceKm} km • ${station.etaMin} min",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (station.isFull) Color(0xFFFCE8E8) else Color(0xFFE8F5E9)
                    ) {
                        Text(
                            station.availability,
                            fontSize = 11.sp,
                            color = if (station.isFull) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        "₹${station.pricePerKwh}/kWh",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        "★ ${station.rating} (${station.reviewCount})",
                        fontSize = 11.sp,
                        color = Color.Gray
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
                        modifier = Modifier.alignByBaseline()
                    )
                    Text(
                        "${station.connectors}  ",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.alignByBaseline()
                    )
                    Text(
                        station.hours,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.alignByBaseline()
                    )
                }
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("View Details", color = Color(0xFF2E7D32), fontSize = 12.sp)
                }
            }
        }
    }
}
