package com.skegworks.mobilepos.barcode

interface BarcodeReader {
    suspend fun readBarcode(): String
}