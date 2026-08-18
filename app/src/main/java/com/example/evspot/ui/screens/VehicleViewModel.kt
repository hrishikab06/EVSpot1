package com.example.evspot.ui.screens

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.evspot.data.BatteryRepository
import com.example.evspot.data.model.BatteryStatus
import com.example.evspot.data.model.VehicleListing
import com.example.evspot.data.model.sampleVehicles
import kotlin.math.abs
import kotlin.math.roundToInt
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

            // Pre-process the dataset: map each integer SOC (0-100) to its closest row
            val socMap = mutableMapOf<Int, BatteryStatus>()
            for (socInt in 0..100) {
                val closestRow = allData.minByOrNull { abs(it.soc - socInt) }
                if (closestRow != null) {
                    // We copy the row but force the integer SOC for deterministic display
                    socMap[socInt] = closestRow.copy(soc = socInt.toDouble())
                }
            }

            var currentSoc = 100
            while (currentSoc >= 0) {
                val currentStatus = socMap[currentSoc]
                if (currentStatus != null) {
                    _bmsStatus.value = currentStatus
                    
                    // Update shared vehicle list to maintain consistency across the app
                    if (_vehicles.isNotEmpty()) {
                        val primaryIndex = _vehicles.indexOfFirst { it.isPrimary }.let { if (it == -1) 0 else it }
                        val primaryVehicle = _vehicles[primaryIndex]
                        _vehicles[primaryIndex] = primaryVehicle.copy(
                            batteryPercentage = currentSoc,
                            estRangeKm = currentStatus.remainingRange.roundToInt()
                        )
                    }
                }

                // Progress every 60 seconds
                delay(60000L)
                
                if (currentSoc == 0) break // Stay at 0% once reached
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
