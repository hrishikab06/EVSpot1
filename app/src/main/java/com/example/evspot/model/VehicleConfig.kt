package com.example.evspot.model

/**
 * Configuration object for dummy vehicle values used in testing.
 * Change these values to test how the ML model predicts the range.
 */
object VehicleConfig {
    const val SOC = 90
    const val BATTERY_TEMP = 25
    const val SPEED = 65
    const val AC_ON = 1 // 1 for ON, 0 for OFF
    const val DISTANCE_TRAVELLED = 35
    const val ENERGY_CONSUMED = 6.5
}
