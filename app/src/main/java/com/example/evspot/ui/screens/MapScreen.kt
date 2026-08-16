package com.example.evspot.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.evspot.model.ChargingSpot
import com.example.evspot.model.MapConfig
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    chargingSpots: List<ChargingSpot> = mockChargingSpots,
    liteModeEnabled: Boolean = false,
    vehicleLocation: LatLng? = null,
    cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(MapConfig.DEFAULT_LOCATION, MapConfig.defaultZoom)
    }
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var deviceLocation by remember { mutableStateOf<LatLng?>(null) }
    
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
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        deviceLocation = latLng
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLngZoom(
                                latLng,
                                MapConfig.defaultZoom
                            )
                        )
                    }
                }
            } catch (e: SecurityException) {
                // Permission not granted
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
        uiSettings = MapUiSettings(myLocationButtonEnabled = hasLocationPermission)
    ) {
        // Vehicle Marker - Uses provided location or device location, falls back to default
        val vehiclePos = vehicleLocation ?: deviceLocation ?: MapConfig.DEFAULT_LOCATION
        Marker(
            state = MarkerState(position = vehiclePos),
            title = "My Vehicle",
            snippet = "EVSpot EV-01",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        )

        chargingSpots.forEach { spot ->
            Marker(
                state = MarkerState(position = spot.position),
                title = spot.name,
                snippet = spot.address
            )
        }
    }
}

val mockChargingSpots = listOf(
    ChargingSpot("1", "EV Hub A", LatLng(1.352, 103.872), "123 Green Ave"),
    ChargingSpot("2", "FastCharge B", LatLng(1.360, 103.885), "456 Volt St"),
    ChargingSpot("3", "EcoStation C", LatLng(1.345, 103.860), "789 Solar Rd")
)
