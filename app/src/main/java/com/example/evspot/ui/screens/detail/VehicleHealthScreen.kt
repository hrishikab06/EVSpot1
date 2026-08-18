package com.example.evspot.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.evspot.VehicleHeroCard
import com.example.evspot.ChargingStatusCard
import com.example.evspot.VehicleHealthSection
import com.example.evspot.OdometerEfficiencyCard
import com.example.evspot.mockVehicle
import com.example.evspot.data.api.RangePredictionRequest
import com.example.evspot.data.api.RetrofitClient
import com.example.evspot.model.VehicleConfig

import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleHealthScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var predictedRange by remember { mutableStateOf<Double?>(null) }
    var isRangeLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Log.d("VehicleHealth", "Screen loaded, starting API call")
        isRangeLoading = true
        try {
            val request = RangePredictionRequest(
                soc = VehicleConfig.SOC,
                battery_temp = VehicleConfig.BATTERY_TEMP,
                speed = VehicleConfig.SPEED,
                ac_on = VehicleConfig.AC_ON,
                distance_travelled = VehicleConfig.DISTANCE_TRAVELLED,
                energy_consumed = VehicleConfig.ENERGY_CONSUMED
            )
            Log.d("VehicleHealth", "Request: $request")
            val response = RetrofitClient.instance.predictRange(request)
            if (response.isSuccessful) {
                predictedRange = response.body()?.predicted_range_km
                Log.d("VehicleHealth", "Success: $predictedRange")
                Toast.makeText(context, "Range predicted: ${predictedRange}km", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("VehicleHealth", "Error: ${response.code()} ${response.message()}")
                Toast.makeText(context, "API Error: ${response.code()}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("VehicleHealth", "Exception: ${e.message}", e)
            Toast.makeText(context, "Connection failed: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            isRangeLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vehicle Health") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 24.dp, top = 16.dp)
        ) {
            item { 
                VehicleHeroCard(
                    vehicle = mockVehicle,
                    predictedRange = predictedRange,
                    isRangeLoading = isRangeLoading
                ) 
            }
            item { ChargingStatusCard(mockVehicle.charging) }
            item { VehicleHealthSection(mockVehicle) }
            item { OdometerEfficiencyCard(mockVehicle) }
        }
    }
}
