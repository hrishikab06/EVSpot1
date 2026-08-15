package com.example.evspot.model

import com.google.android.gms.maps.model.LatLng

object MapConfig {
    // Configurable dummy vehicle location
    val vehicleLocation = LatLng(1.355, 103.875)
    
    // Default camera zoom
    val defaultZoom = 14f
    
    // Search radius for charging stations (in meters)
    val searchRadius = 2000 
}
