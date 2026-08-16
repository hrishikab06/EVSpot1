package com.example.evspot.data.model

import com.example.evspot.R

enum class ConnectionStatus {
    CONNECTED, OFFLINE
}

data class VehicleListing(
    val id: String,
    val name: String,
    val model: String,
    val plate: String,
    val imageRes: Int,
    val batteryPercentage: Int,
    val estRangeKm: Int,
    val connectionStatus: ConnectionStatus,
    val chargingStatus: String,
    val lastChargedInfo: String,
    val isPrimary: Boolean = false,
    val issue: String? = null
)

val sampleVehicles = listOf(
    VehicleListing(
        id = "1",
        name = "EvSpot EV-01",
        model = "Tata Nexon EV Max",
        plate = "MH01AB1234",
        imageRes = R.drawable.car,
        batteryPercentage = 72,
        estRangeKm = 246,
        connectionStatus = ConnectionStatus.CONNECTED,
        chargingStatus = "Not Charging",
        lastChargedInfo = "Today, 07:42 AM",
        isPrimary = true
    ),
    VehicleListing(
        id = "2",
        name = "EvSpot Scooter",
        model = "Ather 450X",
        plate = "MH01CD5678",
        imageRes = R.drawable.scooter,
        batteryPercentage = 68,
        estRangeKm = 89,
        connectionStatus = ConnectionStatus.CONNECTED,
        chargingStatus = "Not Charging",
        lastChargedInfo = "Yesterday, 09:15 PM"
    ),
    VehicleListing(
        id = "3",
        name = "Comet EV",
        model = "MG Comet EV",
        plate = "MH01EF9012",
        imageRes = R.drawable.commet,
        batteryPercentage = 35,
        estRangeKm = 72,
        connectionStatus = ConnectionStatus.OFFLINE,
        chargingStatus = "Not Charging",
        lastChargedInfo = "2 days ago"
    )
)
