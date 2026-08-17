package com.example.evspot.data.api

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

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
