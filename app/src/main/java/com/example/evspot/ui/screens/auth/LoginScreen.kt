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
import com.example.evspot.data.api.LoginRequest
import com.example.evspot.data.api.RetrofitClient
import com.example.evspot.ui.components.auth.*
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun LoginScreen(
    onLoginSuccess: (Int, String, String) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            EVSpotAuthTopBar(onBack = { /* Optional: Navigate back or exit app */ })
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
                    text = "Welcome Back",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Text(
                    text = "Sign in to continue your journey",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
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
                
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(onClick = onNavigateToForgotPassword) {
                        Text(
                            text = "Forgot Password?",
                            color = Color(0xFF1B5E20),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )
                }

                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                } else {
                    EVSpotPrimaryButton(
                        text = "Login",
                        onClick = {
                            errorMessage = null
                            if (email.isBlank() || password.isBlank()) {
                                errorMessage = "Email and password are required"
                                return@EVSpotPrimaryButton
                            }
                            
                            isLoading = true
                            scope.launch {
                                try {
                                    val response = RetrofitClient.instance.login(
                                        LoginRequest(email = email, password = password)
                                    )
                                    
                                    if (response.isSuccessful) {
                                        val loginData = response.body()
                                        if (loginData?.user_id != null) {
                                            onLoginSuccess(
                                                loginData.user_id,
                                                loginData.email ?: "",
                                                loginData.full_name ?: ""
                                            )
                                        } else {
                                            errorMessage = "Invalid server response"
                                        }
                                    } else {
                                        val errorBody = response.errorBody()?.string()
                                        val message = try {
                                            JSONObject(errorBody ?: "").getString("detail")
                                        } catch (e: Exception) {
                                            "Login failed"
                                        }
                                        errorMessage = message
                                    }
                                } catch (e: Exception) {
                                    Log.e("LoginScreen", "Login error", e)
                                    errorMessage = "Connection error: ${e.localizedMessage ?: "Check your internet"}"
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                    Text(
                        text = " OR ",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                GoogleAuthButton(onClick = { /* Handle Google Auth */ })
                
                Spacer(modifier = Modifier.height(32.dp))
                
                AuthFooterLink(
                    text = "Don't have an account?",
                    linkText = "Sign Up",
                    onLinkClick = onNavigateToSignUp
                )
            }
        }
    }
}
