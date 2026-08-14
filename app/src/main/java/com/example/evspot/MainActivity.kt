package com.example.evspot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.evspot.navigation.AppNavGraph
import com.example.evspot.ui.theme.EVSpotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EVSpotTheme {
                val navController = rememberNavController()
                AppNavGraph(navController)
            }
        }
    }
}
