package com.skegworks.mobilepos.print

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Build
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.ArrayDeque
import java.util.UUID
import javax.inject.Inject

class BlePrinterManager @Inject constructor(
    private val context: Context
) {

    companion object {

        private const val TAG = "BLE_PRINTER"

        private const val PRINTER_NAME_PREFIX = "PT210"

        /*
         * From your GATT discovery.
         */
        private val SERVICE_UUID =
            UUID.fromString(
                "000018F0-0000-1000-8000-00805F9B34FB"
            )

        /*
         * From your GATT discovery.
         *
         * Properties = 12
         * = WRITE + WRITE_NO_RESPONSE
         */
        private val WRITE_CHARACTERISTIC_UUID =
            UUID.fromString(
                "00002AF1-0000-1000-8000-00805F9B34FB"
            )
    }

    private val bluetoothManager =
        context.getSystemService(
            BluetoothManager::class.java
        )

    private val bluetoothAdapter =
        bluetoothManager.adapter

    private var bluetoothGatt: BluetoothGatt? = null

    private var writeCharacteristic:
            BluetoothGattCharacteristic? = null

    private var isScanning = false

    private var isConnecting = false

    private var isConnected = false

    /*
     * Default BLE MTU.
     *
     * MTU = 23
     * ATT payload = 23 - 3 = 20 bytes
     */
    private var mtu = 23

    /*
     * Chunks waiting to be written.
     */
    private val printQueue =
        ArrayDeque<ByteArray>()

    /*
     * Only one GATT write can be in progress.
     */
    private var isWriting = false

    /*
     * Printer state exposed to ViewModel/UI.
     */
    private val _printerState =
        MutableStateFlow<PrinterState>(
            PrinterState.Idle
        )

    val printerState:
            StateFlow<PrinterState> =
        _printerState.asStateFlow()

    // ========================================================================
    // SCANNER
    // ========================================================================

    private val scannerCallback =
        object : ScanCallback() {

            @SuppressLint("MissingPermission")
            override fun onScanResult(
                callbackType: Int,
                result: ScanResult
            ) {

                /*
                 * Ignore additional devices once we have
                 * started connecting.
                 */
                if (
                    isConnecting ||
                    isConnected
                ) {
                    return
                }

                val device =
                    result.device

                val deviceName =
                    device.name ?: ""

                Log.d(
                    TAG,
                    "Found device: " +
                            "name=$deviceName " +
                            "address=${device.address} " +
                            "RSSI=${result.rssi}"
                )

                /*
                 * Don't connect to arbitrary BLE devices.
                 *
                 * Your printer was:
                 *
                 * PT210_2394
                 */
                val isPrinter =
                    deviceName.startsWith(
                        PRINTER_NAME_PREFIX,
                        ignoreCase = true
                    )

                if (!isPrinter) {
                    return
                }

                Log.d(
                    TAG,
                    "Printer found: $deviceName"
                )

                /*
                 * Prevent duplicate connections.
                 */
                isConnecting = true

                stopScan()

                connect(device)
            }

            override fun onScanFailed(
                errorCode: Int
            ) {

                Log.e(
                    TAG,
                    "Scan failed: $errorCode"
                )

                isScanning = false
                isConnecting = false

                _printerState.value =
                    PrinterState.Error(
                        "BLE scan failed: $errorCode"
                    )
            }
        }

    // ========================================================================
    // START SCAN
    // ========================================================================

    @SuppressLint("MissingPermission")
    fun startScan() {

        if (!bluetoothAdapter.isEnabled) {

            Log.e(
                TAG,
                "Bluetooth disabled"
            )

            _printerState.value =
                PrinterState.Error(
                    "Bluetooth is disabled"
                )

            return
        }

        if (
            isScanning ||
            isConnecting ||
            isConnected
        ) {

            Log.d(
                TAG,
                "Already scanning/connecting/connected"
            )

            return
        }

        /*
         * Reset previous print state.
         */
        printQueue.clear()

        isWriting = false

        writeCharacteristic = null

        mtu = 23

        _printerState.value =
            PrinterState.Scanning

        isScanning = true

        bluetoothAdapter
            .bluetoothLeScanner
            ?.startScan(
                scannerCallback
            )

        Log.d(
            TAG,
            "Scanning started"
        )
    }

    // ========================================================================
    // STOP SCAN
    // ========================================================================

    @SuppressLint("MissingPermission")
    private fun stopScan() {

        if (!isScanning) {
            return
        }

        bluetoothAdapter
            .bluetoothLeScanner
            ?.stopScan(
                scannerCallback
            )

        isScanning = false

        Log.d(
            TAG,
            "Scanning stopped"
        )
    }

    // ========================================================================
    // CONNECT
    // ========================================================================

    @SuppressLint("MissingPermission")
    private fun connect(
        device: BluetoothDevice
    ) {

        Log.d(
            TAG,
            "Connecting to " +
                    "${device.name} " +
                    "${device.address}"
        )

        _printerState.value =
            PrinterState.Connecting

        bluetoothGatt =
            if (
                Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.M
            ) {

                device.connectGatt(
                    context,
                    false,
                    gattCallback,
                    BluetoothDevice.TRANSPORT_LE
                )

            } else {

                device.connectGatt(
                    context,
                    false,
                    gattCallback
                )
            }
    }

    // ========================================================================
    // GATT CALLBACK
    // ========================================================================

    private val gattCallback =
        object : BluetoothGattCallback() {

            // =================================================================
            // CONNECTION STATE
            // =================================================================

            @SuppressLint("MissingPermission")
            override fun onConnectionStateChange(
                gatt: BluetoothGatt,
                status: Int,
                newState: Int
            ) {

                Log.d(
                    TAG,
                    "Connection state: " +
                            "status=$status " +
                            "state=$newState"
                )

                /*
                 * status == 0 means success.
                 */
                if (
                    status == 0 &&
                    newState ==
                    BluetoothProfile.STATE_CONNECTED
                ) {

                    Log.d(
                        TAG,
                        "CONNECTED"
                    )

                    isConnected = true
                    isConnecting = false

                    mtu = 23

                    _printerState.value =
                        PrinterState.DiscoveringServices

                    val started =
                        gatt.discoverServices()

                    Log.d(
                        TAG,
                        "discoverServices started=$started"
                    )

                    if (!started) {

                        _printerState.value =
                            PrinterState.Error(
                                "Failed to start service discovery"
                            )
                    }

                } else if (
                    newState ==
                    BluetoothProfile.STATE_DISCONNECTED
                ) {

                    Log.e(
                        TAG,
                        "DISCONNECTED " +
                                "status=$status"
                    )

                    isConnected = false
                    isConnecting = false
                    isWriting = false

                    printQueue.clear()

                    writeCharacteristic = null

                    if (bluetoothGatt == gatt) {
                        bluetoothGatt = null
                    }

                    /*
                     * status 19 is the error you were
                     * seeing previously.
                     */
                    if (status != 0) {

                        _printerState.value =
                            PrinterState.Error(
                                "Printer disconnected. GATT status=$status"
                            )

                    } else {

                        _printerState.value =
                            PrinterState.Disconnected
                    }

                    gatt.close()
                }
            }

            // =================================================================
            // MTU
            // =================================================================

            override fun onMtuChanged(
                gatt: BluetoothGatt,
                mtu: Int,
                status: Int
            ) {

                Log.d(
                    TAG,
                    "MTU changed: " +
                            "mtu=$mtu " +
                            "status=$status"
                )

                if (status == 0) {

                    this@BlePrinterManager.mtu =
                        mtu

                    Log.d(
                        TAG,
                        "BLE payload=${mtu - 3}"
                    )
                }
            }

            // =================================================================
            // SERVICES DISCOVERED
            // =================================================================

            @SuppressLint("MissingPermission")
            override fun onServicesDiscovered(
                gatt: BluetoothGatt,
                status: Int
            ) {

                Log.d(
                    TAG,
                    "Services discovered: status=$status"
                )

                if (status != 0) {

                    _printerState.value =
                        PrinterState.Error(
                            "Service discovery failed: $status"
                        )

                    return
                }

                /*
                 * Print all services for debugging.
                 */
                for (service in gatt.services) {

                    Log.d(
                        TAG,
                        "SERVICE ${service.uuid}"
                    )

                    for (
                    characteristic
                    in service.characteristics
                    ) {

                        Log.d(
                            TAG,
                            "  CHARACTERISTIC " +
                                    "${characteristic.uuid} " +
                                    "properties=" +
                                    "${characteristic.properties}"
                        )
                    }
                }

                /*
                 * Find 18F0.
                 */
                val service =
                    gatt.getService(
                        SERVICE_UUID
                    )

                if (service == null) {

                    Log.e(
                        TAG,
                        "18F0 service not found"
                    )

                    _printerState.value =
                        PrinterState.Error(
                            "Printer service 18F0 not found"
                        )

                    return
                }

                Log.d(
                    TAG,
                    "18F0 service found"
                )

                /*
                 * Find 2AF1.
                 */
                val characteristic =
                    service.getCharacteristic(
                        WRITE_CHARACTERISTIC_UUID
                    )

                if (characteristic == null) {

                    Log.e(
                        TAG,
                        "2AF1 characteristic not found"
                    )

                    _printerState.value =
                        PrinterState.Error(
                            "Printer write characteristic 2AF1 not found"
                        )

                    return
                }

                Log.d(
                    TAG,
                    "2AF1 characteristic found"
                )

                Log.d(
                    TAG,
                    "Properties=${characteristic.properties}"
                )

                writeCharacteristic =
                    characteristic

                /*
                 * Printer is now ready.
                 */
                Log.d(
                    TAG,
                    "PRINTER READY"
                )

                _printerState.value =
                    PrinterState.Ready
            }

            // =================================================================
            // CHARACTERISTIC WRITE
            // =================================================================

            @SuppressLint("MissingPermission")
            override fun onCharacteristicWrite(
                gatt: BluetoothGatt,
                characteristic:
                BluetoothGattCharacteristic,
                status: Int
            ) {

                Log.d(
                    TAG,
                    "onCharacteristicWrite " +
                            "status=$status"
                )

                isWriting = false

                /*
                 * status == 0 = success.
                 */
                if (status == 0) {

                    Log.d(
                        TAG,
                        "Chunk written successfully"
                    )

                    /*
                     * Continue with next chunk.
                     */
                    sendNextChunk(gatt)

                } else {

                    Log.e(
                        TAG,
                        "BLE write failed: $status"
                    )

                    printQueue.clear()

                    _printerState.value =
                        PrinterState.Error(
                            "BLE write failed: $status"
                        )
                }
            }
        }

    // ========================================================================
    // PRINT INVOICE
    // ========================================================================

    @SuppressLint("MissingPermission")
    fun printInvoice(
        invoiceContent: String
    ) {

        val gatt =
            bluetoothGatt

        if (
            gatt == null ||
            !isConnected
        ) {

            Log.e(
                TAG,
                "Printer is not connected"
            )

            _printerState.value =
                PrinterState.Error(
                    "Printer is not connected"
                )

            return
        }

        val characteristic =
            writeCharacteristic

        if (characteristic == null) {

            Log.e(
                TAG,
                "Write characteristic unavailable"
            )

            _printerState.value =
                PrinterState.Error(
                    "Printer write characteristic unavailable"
                )

            return
        }

        /*
         * Clear old queue.
         */
        printQueue.clear()

        isWriting = false

        /*
         * Convert text to bytes.
         */
        val data =
            invoiceContent.toByteArray(
                Charsets.UTF_8
            )

        /*
         * BLE ATT:
         *
         * MTU - 3 bytes
         *
         * Default:
         *
         * 23 - 3 = 20 bytes
         */
        val chunkSize =
            mtu - 3

        if (chunkSize <= 0) {

            _printerState.value =
                PrinterState.Error(
                    "Invalid BLE chunk size"
                )

            return
        }

        Log.d(
            TAG,
            "Starting print"
        )

        Log.d(
            TAG,
            "Invoice bytes=${data.size}"
        )

        Log.d(
            TAG,
            "MTU=$mtu"
        )

        Log.d(
            TAG,
            "Chunk size=$chunkSize"
        )

        /*
         * Divide invoice into chunks.
         */
        var offset = 0

        while (offset < data.size) {

            val end =
                minOf(
                    offset + chunkSize,
                    data.size
                )

            printQueue.add(
                data.copyOfRange(
                    offset,
                    end
                )
            )

            offset = end
        }

        Log.d(
            TAG,
            "Total chunks=${printQueue.size}"
        )

        _printerState.value =
            PrinterState.Printing

        /*
         * Start first chunk.
         */
        sendNextChunk(gatt)
    }

    // ========================================================================
    // SEND NEXT CHUNK
    // ========================================================================

    @SuppressLint("MissingPermission")
    private fun sendNextChunk(
        gatt: BluetoothGatt
    ) {

        /*
         * Don't send another write until
         * previous write completes.
         */
        if (isWriting) {
            return
        }

        /*
         * Nothing left to print.
         */
        if (printQueue.isEmpty()) {

            Log.d(
                TAG,
                "PRINT COMPLETED"
            )

            _printerState.value =
                PrinterState.Printed

            return
        }

        val characteristic =
            writeCharacteristic

        if (characteristic == null) {

            printQueue.clear()

            _printerState.value =
                PrinterState.Error(
                    "Write characteristic unavailable"
                )

            return
        }

        val chunk =
            printQueue.removeFirst()

        Log.d(
            TAG,
            "Sending chunk " +
                    "size=${chunk.size} " +
                    "remaining=${printQueue.size}"
        )

        /*
         * Use WRITE_TYPE_DEFAULT.
         *
         * This allows us to receive
         * onCharacteristicWrite().
         */
        characteristic.writeType =
            BluetoothGattCharacteristic
                .WRITE_TYPE_DEFAULT

        isWriting = true

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {

            /*
             * Android 13+ API.
             *
             * Returns:
             *
             * 0 = success
             * non-zero = error
             */
            val result =
                gatt.writeCharacteristic(
                    characteristic,
                    chunk,
                    BluetoothGattCharacteristic
                        .WRITE_TYPE_DEFAULT
                )

            Log.d(
                TAG,
                "writeCharacteristic result=$result"
            )

            if (result != 0) {

                Log.e(
                    TAG,
                    "Failed to start write: $result"
                )

                isWriting = false

                printQueue.addFirst(
                    chunk
                )

                _printerState.value =
                    PrinterState.Error(
                        "Failed to start BLE write: $result"
                    )
            }

        } else {

            /*
             * Older Android API.
             */
            characteristic.value =
                chunk

            val started =
                gatt.writeCharacteristic(
                    characteristic
                )

            Log.d(
                TAG,
                "writeCharacteristic started=$started"
            )

            if (!started) {

                Log.e(
                    TAG,
                    "Failed to start BLE write"
                )

                isWriting = false

                printQueue.addFirst(
                    chunk
                )

                _printerState.value =
                    PrinterState.Error(
                        "Failed to start BLE write"
                    )
            }
        }
    }

    // ========================================================================
    // CLOSE
    // ========================================================================

    @SuppressLint("MissingPermission")
    fun close() {

        Log.d(
            TAG,
            "Closing printer"
        )

        stopScan()

        printQueue.clear()

        isWriting = false
        isConnected = false
        isConnecting = false

        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()

        bluetoothGatt = null

        writeCharacteristic = null

        mtu = 23

        _printerState.value =
            PrinterState.Idle
    }
}