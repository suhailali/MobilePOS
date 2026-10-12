package com.skegworks.mobilepos.print

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.data.domain.Invoice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BLEPrinterConnectViewModel @Inject constructor(
    private val bleDiscoveryHelper: BluetoothDiscoveryHelper,
    private val blePrinterManager: BluetoothPrinterManager,
    private val newBlePrinterManager: BlePrinterManager
): ViewModel() {

    val printerState:
            StateFlow<PrinterState> =
        newBlePrinterManager.printerState
    private val _discoveredDevicesState: StateFlow<List<BLEPrinterDevice>> = bleDiscoveryHelper.discoveredDevices

    val discoveredDevicesState: StateFlow<List<BLEPrinterDevice>> get() = _discoveredDevicesState
    fun startDiscovery() {

//        bleDiscoveryHelper.startDiscovery()
        viewModelScope.launch(Dispatchers.IO) {
            newBlePrinterManager.startScan()
        }
    }

    fun stopDiscovery() {

    }

    fun testPrint() {
        viewModelScope.launch {
//            blePrinterManager.printLabel("1234567890", "1000")
            val content = "\u001B\u0040 \n\n\n Test Re-Print \n\n".plus("\n\n\n\n\n")
            newBlePrinterManager.printInvoice(content)
        }
    }

    @SuppressLint("MissingPermission")
    fun connect(bleDevice: BLEPrinterDevice) {
        viewModelScope.launch {
            blePrinterManager.connect(bleDevice.address)
        }
    }

    override fun onCleared() {
        super.onCleared()

        newBlePrinterManager.close()
    }


    private var pendingPrintContent:
            String? = null

    init {

        observePrinterState()
    }

    private fun observePrinterState() {

        viewModelScope.launch {

            newBlePrinterManager
                .printerState
                .collect { state ->

                    when (state) {

                        PrinterState.Ready -> {

                            /*
                             * Printer is connected and
                             * the write characteristic is
                             * available.
                             */
//                            pendingPrintContent
//                                ?.let { content ->
//
//                                    pendingPrintContent =
//                                        null
//
//                                    newBlePrinterManager
//                                        .printInvoice(
//                                            content
//                                        )
//                                }
                        }

                        PrinterState.Printed -> {

                            /*
                             * Printing completed.
                             */
                        }

                        is PrinterState.Error -> {

                            /*
                             * Handle error if required.
                             */
                        }

                        else -> {
                            // Nothing to do.
                        }
                    }
                }
        }
    }

    /*
     * Called by UI.
     */
    fun printInvoice(
//        invoice: Invoice,
//        infoForCustomer: List<String>
    ) {

        /*
         * Convert Invoice model into
         * thermal printer text.
         */
        val content = "\u001B\u0040 \n\n\n Test Print \n\n".plus("\n\n\n\n\n")
//            createThermalInvoice(
//                invoice = invoice,
//                infoForCustomer = infoForCustomer
//            )

        /*
         * Store it because the printer isn't
         * necessarily connected yet.
         */
        pendingPrintContent =
            content

        /*
         * Start BLE connection process.
         */
        newBlePrinterManager.startScan()
    }
}