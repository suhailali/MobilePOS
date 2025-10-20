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
import com.skegworks.mobilepos.data.Vendor
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
                    val list = listOf(
                        Vendor(
                            name = "Manish",
                            state = "Rajasthan",
                            city = "Jaipur",
                            country = "India",
                            gst = "29AHDKDKDK37",
                            gstPercentage = "5",
                            phone = "8374747474",
                            email = "manish@gmail.com",
                            address = "23/A, first floor",
                            zipCode = "456321",
                            currency = "Rupees"
                        ),
                        Vendor(
                            name = "Empire",
                            state = "Rajasthan",
                            city = "Jaipur",
                            country = "India",
                            gst = "29AJJHDKDK80",
                            gstPercentage = "5",
                            phone = "1234432123",
                            email = "empire@gmail.com",
                            address = "90/A, ground floor",
                            zipCode = "456321",
                            currency = "Rupees"
                        ),
                    )
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