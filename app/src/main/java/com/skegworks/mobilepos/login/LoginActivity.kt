package com.skegworks.mobilepos.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.skegworks.mobilepos.home.MainActivity
import com.skegworks.mobilepos.ui.theme.MobilePOSTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        val isCreateUser = bundle?.getBoolean("isCreateUser") ?: false
        enableEdgeToEdge()
        setContent {
            MobilePOSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: LoginViewModel by viewModels<LoginViewModel>()
                    val context = LocalContext.current
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel,
                        isCreateUser
                    ) {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }

                }
            }
        }
    }
}