package com.example.evspot.model

data class ChargingSlot(
    val id: Int,
    val isAvailable: Boolean,
    val type: String,
    val statusText: String? = null
)

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
    val isFull: Boolean = false,
    val slots: List<ChargingSlot> = emptyList()
)

val sampleStations = listOf(
    ChargerStation(
        name = "Koramangala EV Hub",
        type = "Superfast",
        location = "8th Block, Koramangala",
        distanceKm = 1.2,
        etaMin = 5,
        pricePerKwh = 18,
        rating = 4.8,
        reviewCount = 124,
        maxSpeedKw = 120,
        connectors = "CCS2, CHAdeMO",
        hours = "24/7",
        availability = "2/4 Available",
        slots = listOf(
            ChargingSlot(1, true, "CCS2 - 120kW", "Ready to charge"),
            ChargingSlot(2, false, "CCS2 - 120kW", "Occupied (ETA 15 min)"),
            ChargingSlot(3, true, "CHAdeMO - 50kW", "Ready to charge"),
            ChargingSlot(4, false, "CCS2 - 120kW", "Occupied (ETA 40 min)")
        )
    ),
    ChargerStation(
        name = "Indiranagar Charging Point",
        type = "Fast",
        location = "100 Feet Rd, Indiranagar",
        distanceKm = 4.5,
        etaMin = 15,
        pricePerKwh = 15,
        rating = 4.5,
        reviewCount = 89,
        maxSpeedKw = 50,
        connectors = "CCS2, Type 2",
        hours = "24/7",
        availability = "1/2 Available",
        slots = listOf(
            ChargingSlot(1, true, "CCS2 - 50kW", "Ready to charge"),
            ChargingSlot(2, false, "Type 2 - 22kW", "Occupied (ETA 10 min)")
        )
    )
)
