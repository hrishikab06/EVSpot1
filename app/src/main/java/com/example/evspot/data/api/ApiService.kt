package com.example.evspot.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterRequest(
    val email: String,
    val password: String,
    val full_name: String,
    val phone: String? = null
)

data class RegisterResponse(
    val message: String,
    val user_id: Int? = null,
    val detail: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val user_id: Int? = null,
    val email: String? = null,
    val full_name: String? = null,
    val detail: String? = null
)

data class RangePredictionRequest(
    @SerializedName("soc") val soc: Int,
    @SerializedName("battery_temp") val battery_temp: Int,
    @SerializedName("speed") val speed: Int,
    @SerializedName("ac_on") val ac_on: Int,
    @SerializedName("distance_travelled") val distance_travelled: Int,
    @SerializedName("energy_consumed") val energy_consumed: Double
)

data class RangePredictionResponse(
    @SerializedName("predicted_range_km") val predicted_range_km: Double
)

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("predict-range")
    suspend fun predictRange(@Body request: RangePredictionRequest): Response<RangePredictionResponse>

    @POST("plan-trip")
    suspend fun planTrip(@Body request: PlanTripRequest): Response<PlanTripResponse>

    @retrofit2.http.GET("stations")
    suspend fun getStations(): Response<StationResponse>

    @retrofit2.http.GET("stations/{stationId}")
    suspend fun getStationDetails(@retrofit2.http.Path("stationId") stationId: Int): Response<Station>

    @retrofit2.http.GET("stations/{stationId}/chargers")
    suspend fun getChargers(@retrofit2.http.Path("stationId") stationId: Int): Response<ChargersResponse>

    @POST("chargers/{chargerId}/availability")
    suspend fun checkAvailability(
        @retrofit2.http.Path("chargerId") chargerId: Int,
        @Body request: AvailabilityRequest
    ): Response<AvailabilityResponse>

    @POST("bookings")
    suspend fun createBooking(@Body request: BookingRequest): Response<BookingResponse>
}

data class PlanTripRequest(
    val current_lat: Double,
    val current_lng: Double,
    val destination_lat: Double,
    val destination_lng: Double,
    val current_soc: Double,
    val battery_temp: Double,
    val battery_capacity_kwh: Double,
    val candidate_stations: List<CandidateStation>
)

data class CandidateStation(
    val id: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)

data class PlanTripResponse(
    val one_stop_possible: Boolean,
    val message: String,
    val total_distance_km: Double,
    val drive_time_minutes: Double,
    val recommended_station: CandidateStation?,
    val arrival_soc_percent: Double?,
    val target_soc_percent: Double,
    val charging_time_minutes: Double?,
    val charging_cost_inr: Double,
    val total_trip_time_minutes: Double?,
    val route_plan: List<RoutePlanStep>
)

data class RoutePlanStep(
    val type: String,
    val name: String,
    val distance_km: Double? = null,
    val drive_time_minutes: Double? = null,
    val charging_time_minutes: Double? = null
)
