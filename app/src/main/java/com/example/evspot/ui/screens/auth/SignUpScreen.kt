package com.example.evspot.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.ui.components.auth.*

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
                
                Spacer(modifier = Modifier.height(32.dp))
                
                EVSpotPrimaryButton(
                    text = "Create Account",
                    onClick = onSignUpSuccess
                )
                
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
