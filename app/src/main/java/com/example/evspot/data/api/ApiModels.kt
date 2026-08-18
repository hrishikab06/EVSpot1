package com.example.evspot.data.api

import com.google.gson.annotations.SerializedName

data class Station(
    val id: Int,
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val postal_code: String?,
    val latitude: Double,
    val longitude: Double,
    val operator_name: String?,
    val access_type: String?,
    val open_24_7: Boolean,
    val opening_hours: String?,
    val amenities: String?
)

data class StationResponse(
    val count: Int,
    val stations: List<Station>
)

data class Charger(
    val id: Int,
    val station_id: Int,
    val connector_type: String,
    val power_kw: Double,
    val status: String,
    val is_available: Boolean
)

data class ChargersResponse(
    val station_id: Int,
    val count: Int,
    val chargers: List<Charger>
)

data class AvailabilityRequest(
    val start_time: String,
    val end_time: String
)

data class AvailabilityResponse(
    val charger_id: Int,
    val available: Boolean,
    val reason: String
)

data class BookingRequest(
    val user_id: Int,
    val charger_id: Int,
    val start_time: String,
    val end_time: String,
    val estimated_cost_inr: Int? = null
)

data class BookingResponse(
    val message: String,
    val booking_id: Int,
    val status: String,
    val start_time: String,
    val end_time: String,
    val arrival_deadline: String,
    val created_at: String
)
