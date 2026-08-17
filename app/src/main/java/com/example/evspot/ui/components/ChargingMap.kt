package com.example.evspot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.model.ChargingSpot
import com.example.evspot.model.MapConfig
import com.example.evspot.ui.screens.MapScreen
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun ChargingMap(
    modifier: Modifier = Modifier,
    bottomPadding: androidx.compose.ui.unit.Dp = 0.dp,
    useMock: Boolean = false,
    isLiteMode: Boolean = false,
    vehicleLocation: LatLng? = null,
    destinationLocation: LatLng? = null,
    routePoints: List<LatLng> = emptyList(),
    searchCenter: LatLng? = null,
    searchRadius: Double = MapConfig.searchRadius.toDouble(),
    chargingSpots: List<ChargingSpot> = emptyList(),
    onMapClick: ((LatLng) -> Unit)? = null,
    onDeviceLocationChanged: ((LatLng) -> Unit)? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(MapConfig.DEFAULT_LOCATION, MapConfig.defaultZoom)
    }

    // Move camera when search center or route changes
    LaunchedEffect(searchCenter, routePoints) {
        if (searchCenter != null) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(searchCenter, MapConfig.defaultZoom)
            )
        } else if (routePoints.isNotEmpty()) {
            // Fit route
            val builder = com.google.android.gms.maps.model.LatLngBounds.Builder()
            routePoints.forEach { builder.include(it) }
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(builder.build(), 100)
            )
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (useMock) {
            MockMapBackground()
        } else {
            MapScreen(
                modifier = Modifier.fillMaxSize(),
                liteModeEnabled = isLiteMode,
                cameraPositionState = cameraPositionState,
                vehicleLocation = vehicleLocation,
                destinationLocation = destinationLocation,
                routePoints = routePoints,
                searchCenter = searchCenter,
                searchRadius = searchRadius,
                chargingSpots = chargingSpots,
                onMapClick = onMapClick,
                onDeviceLocationChanged = onDeviceLocationChanged
            )
        }

        // Zoom Controls
        Column(
            modifier = Modifier
                .padding(bottom = 80.dp + bottomPadding, end = 16.dp)
                .align(Alignment.BottomEnd),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        cameraPositionState.animate(CameraUpdateFactory.zoomIn())
                    }
                },
                modifier = Modifier.size(40.dp),
                containerColor = Color.White,
                contentColor = Color.Black,
                shape = RoundedCornerShape(8.dp),
                elevation = FloatingActionButtonDefaults.elevation(2.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Zoom In")
            }
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        cameraPositionState.animate(CameraUpdateFactory.zoomOut())
                    }
                },
                modifier = Modifier.size(40.dp),
                containerColor = Color.White,
                contentColor = Color.Black,
                shape = RoundedCornerShape(8.dp),
                elevation = FloatingActionButtonDefaults.elevation(2.dp)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
            }
        }
        
        FloatingActionButton(
            onClick = {
                try {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(it.latitude, it.longitude),
                                        MapConfig.defaultZoom
                                    )
                                )
                            }
                        }
                    }
                } catch (e: SecurityException) {
                    // Handled by permission logic in MapScreen
                }
            },
            modifier = Modifier
                .padding(bottom = 16.dp + bottomPadding, end = 16.dp)
                .size(48.dp)
                .align(Alignment.BottomEnd),
            containerColor = Color.White,
            contentColor = Color(0xFF1B5E20),
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(4.dp)
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = "My Location", modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
fun MockMapBackground() {
    // A simplified visual representation of a map to prevent crashes
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E0E0)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Map Preview Mode",
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                fontSize = 18.sp
            )
            Text(
                "Add your API Key in AndroidManifest.xml\nto see the real map.",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
