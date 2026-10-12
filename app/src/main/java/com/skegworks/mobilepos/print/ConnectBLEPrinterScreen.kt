package com.skegworks.mobilepos.print

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skegworks.mobilepos.ui.component.SpacerLarge

@Composable
fun ConnectBLEPrinterScreen(
    modifier: Modifier,
    viewModel: BLEPrinterConnectViewModel,
    onScanClick: () -> Unit,
    onStopScanClick: () -> Unit,
    onTestPrintClick: () -> Unit,
    onConnectClick: (BLEPrinterDevice) -> Unit
) {

    val printerState by
    viewModel.printerState
        .collectAsStateWithLifecycle()

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        // Check if all requested permissions were granted
        val allGranted = permissionsMap.values.all { it }
        if (allGranted) {
            onScanClick() // Start scanning
        } else {
            // Handle permission denied state (e.g., show an explanation dialog)
        }
    }
    val deviceListState = viewModel.discoveredDevicesState.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "BLE Printer",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = when (printerState) {

                PrinterState.Idle ->
                    "Printer idle"

                PrinterState.Scanning ->
                    "Searching for printer..."

                PrinterState.Connecting ->
                    "Connecting to printer..."

                PrinterState.DiscoveringServices ->
                    "Preparing printer..."

                PrinterState.Ready ->
                    "Printer ready"

                PrinterState.Printing ->
                    "Printing..."

                PrinterState.Printed ->
                    "Printed successfully"

                PrinterState.Disconnected ->
                    "Printer disconnected"

                is PrinterState.Error ->
                    "Error: "
            }
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Button(
            onClick = {
                if (hasBluetoothPermissions(context = context)) {
                    onScanClick()
                } else {
                    permissionLauncher.launch(bluetoothPermissions)
                }
            }
        ) {
            Text("Start Scan")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = onStopScanClick
        ) {
            Text("Stop Scan")
        }

        Button(
            onClick = { viewModel.testPrint() }
        ) {
            Text("Test Print")
        }

        SpacerLarge()
        LazyColumn {
            items(deviceListState.value) { bleDevice ->
                Row(modifier = Modifier
                    .padding(24.dp)
                    .clickable(onClick = {
                        onConnectClick(bleDevice)
                    })) {
                    Text(bleDevice.name)
                    SpacerLarge()
                    Text(bleDevice.address)
                }
            }
        }
    }

    // Helper function to check current permission status

}

val bluetoothPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    arrayOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT
    )
} else {
    arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
}
fun hasBluetoothPermissions(context: Context): Boolean {
    return bluetoothPermissions.all { permission ->
        ContextCompat.checkSelfPermission(context, permission) ==   PackageManager.PERMISSION_GRANTED
    }
}