package com.example.evspot.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.evspot.model.ChargingSpot
import com.example.evspot.model.MapConfig
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    chargingSpots: List<ChargingSpot> = emptyList(),
    liteModeEnabled: Boolean = false,
    vehicleLocation: LatLng? = null,
    destinationLocation: LatLng? = null,
    routePoints: List<LatLng> = emptyList(),
    searchCenter: LatLng? = null,
    searchRadius: Double = MapConfig.searchRadius.toDouble(),
    onMapClick: ((LatLng) -> Unit)? = null,
    onDeviceLocationChanged: ((LatLng) -> Unit)? = null,
    cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(MapConfig.DEFAULT_LOCATION, MapConfig.defaultZoom)
    }
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val scope = rememberCoroutineScope()

    var deviceLocation by remember { mutableStateOf<LatLng?>(null) }
    var initialLocationFetched by remember { mutableStateOf(false) }
    
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions.values.all { it }
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            try {
                // Try getting last known location first
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        deviceLocation = latLng
                        onDeviceLocationChanged?.invoke(latLng)
                        if (searchCenter == null && !initialLocationFetched) {
                            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, MapConfig.defaultZoom))
                            initialLocationFetched = true
                        }
                    }
                }
                
                // Request a fresh location update
                val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                    .setWaitForAccurateLocation(false)
                    .setMinUpdateIntervalMillis(3000)
                    .setMaxUpdates(1)
                    .build()

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            result.lastLocation?.let { location ->
                                val latLng = LatLng(location.latitude, location.longitude)
                                deviceLocation = latLng
                                onDeviceLocationChanged?.invoke(latLng)
                                if (searchCenter == null && !initialLocationFetched) {
                                    scope.launch {
                                        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, MapConfig.defaultZoom))
                                        initialLocationFetched = true
                                    }
                                }
                            }
                        }
                    },
                    context.mainLooper
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    val mapProperties by remember(hasLocationPermission) {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = hasLocationPermission
            )
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        googleMapOptionsFactory = {
            GoogleMapOptions().liteMode(liteModeEnabled)
        },
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = hasLocationPermission,
            zoomControlsEnabled = false // REMOVE DUPLICATE ZOOM CONTROLS
        ),
        onMapClick = onMapClick
    ) {
        // Search Radius Circle - ONLY SHOW AROUND SEARCHED CENTER
        searchCenter?.let { center ->
            Circle(
                center = center,
                radius = searchRadius,
                fillColor = Color(0x224CAF50), // Very light/dim green
                strokeColor = Color(0x884CAF50), // Subtle green border
                strokeWidth = 2f
            )
        }

        // Route Polyline
        if (routePoints.isNotEmpty()) {
            Polyline(
                points = routePoints,
                color = Color(0xFF2E7D32),
                width = 5f
            )
        }

        // Vehicle Marker - Uses provided location or device location, falls back to default
        val vehiclePos = vehicleLocation ?: deviceLocation ?: MapConfig.DEFAULT_LOCATION
        Marker(
            state = MarkerState(position = vehiclePos),
            title = "My Vehicle",
            snippet = "EVSpot EV-01",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
        )

        // Selected Search Center Marker
        searchCenter?.let {
            Marker(
                state = MarkerState(position = it),
                title = "Search Center",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )
        }

        chargingSpots.forEach { spot ->
            Marker(
                state = MarkerState(position = spot.position),
                title = spot.name,
                snippet = spot.address,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
            )
        }

        // Destination Marker
        destinationLocation?.let {
            Marker(
                state = MarkerState(position = it),
                title = "Destination",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            )
        }
    }
}
