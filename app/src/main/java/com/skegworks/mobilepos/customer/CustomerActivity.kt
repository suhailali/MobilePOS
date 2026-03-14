package com.skegworks.mobilepos.customer

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
import com.skegworks.mobilepos.pos.POSActivity
import com.skegworks.mobilepos.ui.theme.MobilePOSTheme
import com.skegworks.mobilepos.utils.findActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class CustomerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            MobilePOSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: CustomerViewModel by viewModels<CustomerViewModel>()
                    CustomerScreenNavigation(
                        viewModel,
                        modifier = Modifier.padding(innerPadding),
                        isFromLandingScreen = intent.getBooleanExtra("isFromLandingScreen", false)
                            .not()
                    ) { customer ->
                        val intent = Intent(context, POSActivity::class.java)
                        val json = Json.encodeToString(customer)
                        val bundle = bundleOf()
                        bundle.putSerializable("customer", json)
                        intent.putExtras(bundle)
                        context.startActivity(intent)
                        context.findActivity()?.finish()
                    }
                }
            }
        }
    }
}