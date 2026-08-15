package com.example.evspot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.ui.screens.MapScreen

@Composable
fun ChargingMap(
    modifier: Modifier = Modifier,
    bottomPadding: androidx.compose.ui.unit.Dp = 0.dp,
    useMock: Boolean = true // Set to true to avoid Google Maps crashes on emulator without API key
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (useMock) {
            MockMapBackground()
        } else {
            MapScreen(modifier = Modifier.fillMaxSize())
        }
        
        FloatingActionButton(
            onClick = { /* Handle my location */ },
            modifier = Modifier
                .padding(bottom = 16.dp + bottomPadding, end = 16.dp)
                .size(48.dp)
                .align(Alignment.BottomEnd),
            containerColor = Color.White,
            contentColor = Color(0xFF1B5E20),
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(4.dp)
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = "My Location", modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
fun MockMapBackground() {
    // A simplified visual representation of a map to prevent crashes
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E0E0)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Map Preview Mode",
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                fontSize = 18.sp
            )
            Text(
                "Add your API Key in AndroidManifest.xml\nto see the real map.",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
