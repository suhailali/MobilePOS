package com.skegworks.mobilepos.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.skegworks.mobilepos.ui.theme.MobilePOSTheme

class LoginActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        val isCreateUser = bundle?.getBoolean("isCreateUser") ?: false
        enableEdgeToEdge()
        setContent {
            MobilePOSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: LoginViewModel by viewModels<LoginViewModel>()
                    LoginScreen(modifier = Modifier.padding(innerPadding), viewModel, isCreateUser) {
                        // Navigate to next screen on successful login
                        finish()
                    }
                }
            }
        }
    }
}