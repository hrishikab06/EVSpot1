package com.example.evspot.ui.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.evspot.data.model.VehicleListing
import com.example.evspot.data.model.sampleVehicles

class VehicleViewModel : ViewModel() {
    private val _vehicles = mutableStateListOf<VehicleListing>().apply {
        addAll(sampleVehicles)
    }
    val vehicles: List<VehicleListing> = _vehicles

    fun addVehicle(vehicle: VehicleListing) {
        _vehicles.add(vehicle)
    }

    val totalVehicles: Int get() = _vehicles.size
    val onlineVehicles: Int get() = _vehicles.count { it.connectionStatus == com.example.evspot.data.model.ConnectionStatus.CONNECTED }
    val avgBattery: Int get() = if (_vehicles.isEmpty()) 0 else _vehicles.map { it.batteryPercentage }.average().toInt()
    val avgRange: Int get() = if (_vehicles.isEmpty()) 0 else _vehicles.map { it.estRangeKm }.average().toInt()
}
