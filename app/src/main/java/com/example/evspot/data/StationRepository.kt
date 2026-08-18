package com.example.evspot.data

import android.util.Log
import com.example.evspot.data.api.*
import retrofit2.Response

class StationRepository(private val apiService: ApiService = RetrofitClient.instance) {
    private val TAG = "StationRepository"

    init {
        Log.d(TAG, "StationRepository initialized with API service: $apiService")
    }

    suspend fun getStations(): Result<List<Station>> {
        return try {
            Log.d(TAG, "Fetching stations from API...")
            val response = apiService.getStations()
            if (response.isSuccessful && response.body() != null) {
                val stationsList = response.body()!!.stations
                Log.d(TAG, "Successfully fetched ${stationsList.size} stations")
                Result.success(stationsList)
            } else {
                val errorMsg = "API Error: ${response.code()} ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network Error", e)
            Result.failure(e)
        }
    }

    suspend fun getStationDetails(stationId: Int): Result<Station> {
        return try {
            val response = apiService.getStationDetails(stationId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch station details: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChargers(stationId: Int): Result<List<Charger>> {
        return try {
            val response = apiService.getChargers(stationId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.chargers)
            } else {
                Result.failure(Exception("Failed to fetch chargers: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkAvailability(chargerId: Int, startTime: String, endTime: String): Result<AvailabilityResponse> {
        return try {
            val request = AvailabilityRequest(startTime, endTime)
            val response = apiService.checkAvailability(chargerId, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to check availability: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createBooking(
        userId: Int,
        chargerId: Int,
        startTime: String,
        endTime: String,
        estimatedCost: Int? = null
    ): Result<BookingResponse> {
        return try {
            val request = BookingRequest(userId, chargerId, startTime, endTime, estimatedCost)
            val response = apiService.createBooking(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
