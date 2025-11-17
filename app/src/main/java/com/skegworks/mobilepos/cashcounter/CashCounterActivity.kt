package com.skegworks.mobilepos.cashcounter

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
import com.skegworks.mobilepos.customer.CustomerActivity
import com.skegworks.mobilepos.ui.theme.MobilePOSTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CashCounterActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobilePOSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    val viewModel: CashCounterViewModel by viewModels<CashCounterViewModel>()
                    CashCounterScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel
                    ) {
                        val intent = Intent(context, CustomerActivity::class.java)
                        context.startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}