package com.example.evspot.model

import com.google.android.gms.maps.model.LatLng

object MapConfig {
    // Fallback location (Bangalore) if user location is unavailable
    val DEFAULT_LOCATION = LatLng(12.9716, 77.5946)
    
    // Default camera zoom
    val defaultZoom = 14f
    
    // Search radius for charging stations (in meters)
    val searchRadius = 10000 
}
