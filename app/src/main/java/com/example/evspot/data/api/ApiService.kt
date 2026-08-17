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
}
