package com.example.evspot.ui.screens

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.evspot.data.BatteryRepository
import com.example.evspot.data.model.BatteryStatus
import com.example.evspot.data.model.VehicleListing
import com.example.evspot.data.model.sampleVehicles
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VehicleViewModel(application: Application) : AndroidViewModel(application) {
    private val batteryRepository = BatteryRepository(application)

    private val _vehicles = mutableStateListOf<VehicleListing>().apply {
        addAll(sampleVehicles)
    }
    val vehicles: List<VehicleListing> = _vehicles

    private val _bmsStatus = MutableStateFlow<BatteryStatus?>(null)
    val bmsStatus: StateFlow<BatteryStatus?> = _bmsStatus.asStateFlow()

    init {
        startDeterministicBmsSimulation()
    }

    private fun startDeterministicBmsSimulation() {
        viewModelScope.launch {
            val allData = batteryRepository.loadBatteryData()
            if (allData.isEmpty()) return@launch

            // Create a map of SOC integer to the best matching row
            val socMap = mutableMapOf<Int, BatteryStatus>()
            for (status in allData) {
                val socInt = status.soc.roundToInt().coerceIn(0, 100)
                val current = socMap[socInt]
                if (current == null || 
                    Math.abs(status.soc - socInt) < Math.abs(current.soc - socInt)) {
                    socMap[socInt] = status
                }
            }

            var currentSoc = 100
            while (currentSoc >= 0) {
                val status = socMap[currentSoc] ?: run {
                    val closestSoc = socMap.keys.minByOrNull { Math.abs(it - currentSoc) }
                    if (closestSoc != null) {
                        socMap[closestSoc]?.copy(soc = currentSoc.toDouble())
                    } else null
                }
                
                status?.let {
                    _bmsStatus.value = it
                    // Update the primary vehicle in the list to reflect live data
                    if (_vehicles.isNotEmpty()) {
                        val primaryIndex = _vehicles.indexOfFirst { v -> v.isPrimary }.let { if (it == -1) 0 else it }
                        val primaryVehicle = _vehicles[primaryIndex]
                        _vehicles[primaryIndex] = primaryVehicle.copy(
                            batteryPercentage = it.soc.toInt(),
                            estRangeKm = it.remainingRange.toInt()
                        )
                    }
                }
                
                delay(1.minutes)
                currentSoc--
            }
        }
    }

    fun addVehicle(vehicle: VehicleListing) {
        _vehicles.add(vehicle)
    }

    val totalVehicles: Int get() = _vehicles.size
    val onlineVehicles: Int get() = _vehicles.count { it.connectionStatus == com.example.evspot.data.model.ConnectionStatus.CONNECTED }
    val avgBattery: Int get() = if (_vehicles.isEmpty()) 0 else _vehicles.map { it.batteryPercentage }.average().toInt()
    val avgRange: Int get() = if (_vehicles.isEmpty()) 0 else _vehicles.map { it.estRangeKm }.average().toInt()
}
