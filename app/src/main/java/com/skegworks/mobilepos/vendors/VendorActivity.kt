package com.skegworks.mobilepos.vendors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.skegworks.mobilepos.data.domain.Vendor
import com.skegworks.mobilepos.ui.theme.MobilePOSTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VendorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobilePOSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    VendorListScreen(
//                        modifier = Modifier.padding(innerPadding),
//                        vendorList = list
//                    )
                    val viewModel: VendorViewModel by viewModels<VendorViewModel>()
                    VendorDetails(modifier = Modifier.padding(innerPadding),
                        viewModel)
                }
            }
        }
    }
}