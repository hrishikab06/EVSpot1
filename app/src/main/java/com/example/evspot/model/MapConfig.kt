package com.example.evspot.model

import com.google.android.gms.maps.model.LatLng

object MapConfig {
    // Fallback location (Singapore) if user location is unavailable
    val DEFAULT_LOCATION = LatLng(1.355, 103.875)
    
    // Default camera zoom
    val defaultZoom = 14f
    
    // Search radius for charging stations (in meters)
    val searchRadius = 10000 
}
