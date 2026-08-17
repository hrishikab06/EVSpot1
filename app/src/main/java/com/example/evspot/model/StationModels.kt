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
        name = "BKC EV Hub",
        type = "Superfast",
        location = "G Block, BKC, Mumbai",
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
        name = "Powai Charging Point",
        type = "Fast",
        location = "Central Ave, Hiranandani, Mumbai",
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
    ),
    ChargerStation(
        name = "Worli Seaface Charger",
        type = "Superfast",
        location = "Dr Annie Besant Rd, Worli",
        distanceKm = 8.2,
        etaMin = 25,
        pricePerKwh = 20,
        rating = 4.7,
        reviewCount = 56,
        maxSpeedKw = 150,
        connectors = "CCS2",
        hours = "24/7",
        availability = "3/3 Available",
        slots = listOf(
            ChargingSlot(1, true, "CCS2 - 150kW", "Ready to charge"),
            ChargingSlot(2, true, "CCS2 - 150kW", "Ready to charge"),
            ChargingSlot(3, true, "CCS2 - 150kW", "Ready to charge")
        )
    )
)
