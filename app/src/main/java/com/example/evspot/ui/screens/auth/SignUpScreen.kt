package com.example.evspot.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.data.api.RegisterRequest
import com.example.evspot.data.api.RetrofitClient
import com.example.evspot.ui.components.auth.*
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            EVSpotAuthTopBar(onBack = onNavigateToLogin)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthMapBanner()
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EVSpotLogo()
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "Create Account",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Text(
                    text = "Join the future of EV charging",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                EVSpotTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = "Full Name"
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                EVSpotTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address"
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                EVSpotPasswordField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password"
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                EVSpotPasswordField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm Password"
                )
                
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                } else {
                    EVSpotPrimaryButton(
                        text = "Create Account",
                        onClick = {
                            errorMessage = null
                            
                            // Validation
                            when {
                                fullName.isBlank() -> errorMessage = "Full name is required"
                                email.isBlank() -> errorMessage = "Email is required"
                                password.length < 8 -> errorMessage = "Password must be at least 8 characters"
                                password.toByteArray().size > 72 -> errorMessage = "Password is too long"
                                password != confirmPassword -> errorMessage = "Passwords do not match"
                                else -> {
                                    isLoading = true
                                    scope.launch {
                                        try {
                                            val response = RetrofitClient.instance.register(
                                                RegisterRequest(
                                                    email = email,
                                                    password = password,
                                                    full_name = fullName
                                                )
                                            )
                                            
                                            if (response.isSuccessful) {
                                                onSignUpSuccess()
                                            } else {
                                                val errorBody = response.errorBody()?.string()
                                                val message = try {
                                                    JSONObject(errorBody ?: "").getString("detail")
                                                } catch (e: Exception) {
                                                    "Registration failed"
                                                }
                                                errorMessage = message
                                            }
                                        } catch (e: Exception) {
                                            Log.e("SignUpScreen", "Registration error", e)
                                            errorMessage = "Connection error: ${e.javaClass.simpleName} - ${e.localizedMessage ?: "Check your internet"}"
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                AuthFooterLink(
                    text = "Already have an account?",
                    linkText = "Login",
                    onLinkClick = onNavigateToLogin
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
