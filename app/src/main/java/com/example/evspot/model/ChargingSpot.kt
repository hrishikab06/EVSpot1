package com.example.evspot.model

import com.google.android.gms.maps.model.LatLng

data class ChargingSpot(
    val id: String,
    val name: String,
    val position: LatLng,
    val address: String,
    val rating: Double? = null,
    val userRatingsTotal: Int? = null,
    val priceLevel: Int? = null
)
