package com.skegworks.mobilepos.print

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.skegworks.mobilepos.ui.theme.MobilePOSTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BLEPrinterConnectActivity : ComponentActivity() {
    val viewModel by viewModels<BLEPrinterConnectViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // ensureBluetoothPermission(this)

        setContent {
            MobilePOSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    ConnectBLEPrinterScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        onScanClick = {
                            checkAndStartScanning()
                        }, onStopScanClick = {
                            viewModel.stopDiscovery()
                        }, onTestPrintClick = {
                            viewModel.testPrint()
                        },
                        onConnectClick = {
                            viewModel.connect(it)
                        }
                    )
                }
            }
        }
    }

    private fun startScanning() {
        //viewModel.startDiscovery()
        viewModel.printInvoice()
    }

    private fun ensureBluetoothPermission(activity: Activity): Boolean {
        // For Android 12+
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val hasPermission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                    101
                )
            }
            hasPermission
        } else {
            // For pre-Android 12
            val hasPermission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.BLUETOOTH),
                    101
                )
            }
            hasPermission
        }
    }

    private val bluetoothPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val allGranted = permissions.all { it.value }

            if (allGranted) {
                startScanning()
            } else {
                Toast.makeText(
                    this,
                    "Bluetooth permissions are required",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun checkAndStartScanning() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            val requiredPermissions = arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )

            val missingPermissions =
                requiredPermissions.filter {
                    ContextCompat.checkSelfPermission(
                        this,
                        it
                    ) != PackageManager.PERMISSION_GRANTED
                }

            if (missingPermissions.isEmpty()) {

                // Permission already granted
                startScanning()

            } else {

                // Ask Android for the missing permissions
                bluetoothPermissionLauncher.launch(
                    missingPermissions.toTypedArray()
                )
            }

        } else {

            startScanning()
        }
    }
}