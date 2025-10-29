package com.skegworks.mobilepos.pos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.data.mapper.toInvoiceItem
import com.skegworks.mobilepos.print.SeznikPrinterManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class POSViewModel @Inject constructor(
    application: Application,
    private val getProductFromBarcodeUseCase: GetProductFromBarcodeUseCase
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(POSState())
    val state: StateFlow<POSState> = _state.asStateFlow()


    fun printLabel() {
        viewModelScope.launch(Dispatchers.IO) {
            val printerManager = SeznikPrinterManager(application.baseContext)
//                printerManager.printLabel("B8:50:44:10:3A:27", "nadhika")
            printerManager.connectAndPrintUsingSDK()

        }
    }

    fun getProductForBarcode(barcode: String) {
        println("get Product for barcode $barcode")
        viewModelScope.launch(Dispatchers.IO) {
            val product = getProductFromBarcodeUseCase.invoke(barcode)
            if (product != null) {
                println("Product found for barcode $barcode")
                _state.update {
                    it.copy(
                        searchingProduct = false,
                        productFound = true,
                        product = product,
                        invoiceItems = it.invoiceItems + product.toInvoiceItem()
                    )
                }

                var totalPrice = 0
                var discounts = 0.0
                _state.value.invoiceItems.forEach{ item ->
                    totalPrice = totalPrice + item.finalRoundedOffPrice
                    discounts = discounts + item.discountedAmount
                }

                val finalPriceToPay = totalPrice - discounts
                _state.update {
                    it.copy(
                        totalPrice = totalPrice.toDouble(),
                        totalDiscount = discounts,
                        finalPriceToPay = finalPriceToPay
                    )
                }
            }
        }
    }

    fun handleIntent(intent: POSIntent) {
        when (intent) {
            is POSIntent.UpdateScanState -> {
                _state.update { it.copy(productFound = false) }
            }
        }
    }
}