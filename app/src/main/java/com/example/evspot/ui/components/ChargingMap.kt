package com.example.evspot.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.evspot.ui.screens.MapScreen

@Composable
fun ChargingMap(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        MapScreen(modifier = Modifier.fillMaxSize())
        
        FloatingActionButton(
            onClick = { /* Handle my location */ },
            modifier = Modifier
                .padding(12.dp)
                .size(40.dp)
                .align(Alignment.BottomEnd),
            containerColor = Color.White,
            contentColor = Color(0xFF1B5E20),
            shape = CircleShape
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = "My Location", modifier = Modifier.size(20.dp))
        }
    }
}
