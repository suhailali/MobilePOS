package com.skegworks.mobilepos.pos

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.print.SeznikPrinterManager
import com.skegworks.mobilepos.ui.theme.MobilePOSTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class POSActivity : ComponentActivity() {

    private lateinit var printerManager: SeznikPrinterManager


    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ensureBluetoothPermission(this)

//        printerManager = SeznikPrinterManager(this)

        //zprinterManager.fetchUUID()
        val json = intent.getStringExtra("customer")
        val customer = json?.let { Json.decodeFromString<Customer>(it) }
        setContent {
            MobilePOSTheme {
                val viewModel: POSViewModel by viewModels<POSViewModel>()
                customer?.let {
                    viewModel.handleIntent(POSIntent.AddCustomer(it))
                }
//                viewModel.printLabel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    POSScreenNavigation(
                        modifier = Modifier.padding(innerPadding),
                        viewmodel = viewModel
                    ) {
                        finish()
                    }
                }
            }
        }
    }

    private fun ensureBluetoothPermission(activity: Activity): Boolean {
        // For Android 12+
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val hasPermission = ActivityCompat.checkSelfPermission(
                activity, android.Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT),
                    101
                )
            }
            hasPermission
        } else {
            // For pre-Android 12
            val hasPermission = ActivityCompat.checkSelfPermission(
                activity, android.Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(android.Manifest.permission.BLUETOOTH),
                    101
                )
            }
            hasPermission
        }
    }
}