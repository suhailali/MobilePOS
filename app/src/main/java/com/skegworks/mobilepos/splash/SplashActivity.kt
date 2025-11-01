package com.skegworks.mobilepos.splash

import android.annotation.SuppressLint
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
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.skegworks.mobilepos.MainActivity
import com.skegworks.mobilepos.customer.CustomerActivity
import com.skegworks.mobilepos.login.LoginActivity
import com.skegworks.mobilepos.ui.theme.MobilePOSTheme
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity: ComponentActivity() {

    val viewModel: SplashViewModel by viewModels<SplashViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // ✅ Keep splash visible until data is ready
        splashScreen.setKeepOnScreenCondition {
            viewModel.uiState.value is SplashUiState.Loading
        }
        enableEdgeToEdge()
        setContent {
            MobilePOSTheme {
                val context = LocalContext.current
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SplashScreen(modifier = Modifier.padding(innerPadding), viewModel) {
                        val intent = Intent(context, LoginActivity::class.java)
                        val bundle = bundleOf()
                        bundle.putBoolean("isCreateUser", false)
                        intent.putExtras(bundle)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

}