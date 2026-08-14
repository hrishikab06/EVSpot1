package com.example.evspot.model

data class EVInfo(
    val name: String,
    val batteryPercentage: Int,
    val rangeKm: Int,
    val temperature: Int = 24,
    val isConnected: Boolean = true
)
