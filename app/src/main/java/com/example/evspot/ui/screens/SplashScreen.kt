package com.example.evspot.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.evspot.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.95f) }

    LaunchedEffect(Unit) {
        val animationSpec = tween<Float>(durationMillis = 1000)
        delay(200)
        alpha.animateTo(1f, animationSpec)
        scale.animateTo(1f, animationSpec)
        delay(1500)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.evspot_logo),
                contentDescription = "EVSpot Logo",
                modifier = Modifier
                    .size(200.dp)
                    .alpha(alpha.value)
                    .scale(scale.value)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = Color(0xFF1B5E20),
                strokeWidth = 3.dp
            )
        }
    }
}
