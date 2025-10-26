package com.skegworks.mobilepos.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(modifier: Modifier, viewModel: SplashViewModel, onNavigateToMain: () -> Unit) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state) {
        when (state) {
            is SplashUiState.Success -> {
                delay(300) // Optional short delay for smooth transition

            }
            is SplashUiState.Error -> {
                // Optionally handle error
            }
            else -> Unit
        }
    }
    Column {
        SplashLogo()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                SplashUiState.Loading -> CircularProgressIndicator()
                is SplashUiState.Error -> Text("Error loading data", color = Color.Red)
                is SplashUiState.Success -> onNavigateToMain()
            }
        }
    }
}