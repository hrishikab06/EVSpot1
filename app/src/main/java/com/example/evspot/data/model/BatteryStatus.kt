package com.example.evspot.data.model

data class BatteryStatus(
    val soc: Double,
    val batteryTemp: Double,
    val speed: Double,
    val acOn: Boolean,
    val distanceTravelled: Double,
    val energyConsumed: Double,
    val remainingRange: Double,
    val voltage: Double = 0.0
)
