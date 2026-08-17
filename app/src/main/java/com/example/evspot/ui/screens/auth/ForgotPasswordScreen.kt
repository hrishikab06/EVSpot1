package com.example.evspot.ui.screens.auth

import androidx.compose.foundation.layout.*
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
fun ForgotPasswordScreen(
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            EVSpotAuthTopBar(onBack = onBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EVSpotLogo()
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "Reset Password",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Enter your email address and we'll send you a link to reset your password.",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            EVSpotTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email Address"
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            EVSpotPrimaryButton(
                text = "Send Reset Link",
                onClick = { /* Handle reset password */ }
            )
            
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
