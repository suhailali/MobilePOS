package com.skegworks.mobilepos.pos

import android.app.Application
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.customer.CustomerRepository
import com.skegworks.mobilepos.data.domain.Business
import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.data.domain.Product
import com.skegworks.mobilepos.data.mapper.toInvoiceItem
import com.skegworks.mobilepos.invoice.GenerateInvoicePdfUseCase
import com.skegworks.mobilepos.invoice.SyncInvoiceUseCase
import com.skegworks.mobilepos.print.SeznikPrinterManager
import com.skegworks.mobilepos.utils.UUIDGenerator
import com.skegworks.mobilepos.utils.files.FileHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class POSViewModel @Inject constructor(
    application: Application,
    private val getProductFromBarcodeUseCase: GetProductFromBarcodeUseCase,
    private val generateInvoicePdfUseCase: GenerateInvoicePdfUseCase,
    private val customerRepository: CustomerRepository,
    private val fileHandler: FileHandler,
    private val uuidGenerator: UUIDGenerator,
    private val syncInvoiceUseCase: SyncInvoiceUseCase
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
            product?.let {
                println("Product found for barcode $barcode")
                addOrUpdateItem(it)
                calculateTotalPrice()
            }
        }
    }

    private fun addOrUpdateItem(product: Product) {
        val existingItems = state.value.invoiceItems.toMutableList()
        val newItem = product.toInvoiceItem(uuidGenerator.generateUUID())

        val existingIndex = existingItems.indexOfFirst { it.sku == newItem.sku }

        if (existingIndex != -1) {
            // Item exists → update quantity
            val updatedItem = existingItems[existingIndex].copy(
                quantity = existingItems[existingIndex].quantity + newItem.quantity
            )
            existingItems[existingIndex] = updatedItem
        } else {
            // Item not found → add new one
            existingItems.add(newItem)
        }
        _state.update {
            it.copy(
                searchingProduct = false,
                productFound = true,
                product = product,
                invoiceItems = existingItems,
            )
        }
    }

    private fun calculateTotalPrice() {
        var totalPrice = 0
        var discounts = 0.0
        _state.value.invoiceItems.forEach { item ->
            totalPrice = totalPrice + (item.finalRoundedOffPrice * item.quantity)
            discounts = discounts + (item.discountAmount * item.quantity)
        }

        _state.update {
            it.copy(
                totalPrice = totalPrice.toDouble(),
                totalDiscount = discounts,
                finalPriceToPay = totalPrice.toDouble()
            )
        }
    }

    fun handleIntent(intent: POSIntent) {
        when (intent) {
            is POSIntent.AddProduct -> {
                getProductForBarcode("ONEHITPROREDXL000001")
                getProductForBarcode("UNSTHEUNSBLUNO000001")
                getProductForBarcode("UNSTHEUNSPURNO000001")
            }

            is POSIntent.UpdateScanState -> {
                _state.update { it.copy(productFound = false) }
            }

            is POSIntent.PrintInvoice -> {
                createInvoice()
            }

            is POSIntent.Payment -> {
//                viewModelScope.launch(Dispatchers.IO) {
//                    _state.value.invoice?.let {
//                        syncInvoiceUseCase.invoke(it)
//                    }
//                }
                getProductForBarcode("UNSTHEUNSBLUNO000001")
            }

            is POSIntent.AddCustomer -> {
                _state.update {
                    it.copy(
                        customer = intent.customer
                    )
                }
            }

            is POSIntent.RemoveItem -> {
                removeOrUpdateItem(intent.invoiceItem)
                calculateTotalPrice()
            }
        }
    }

    private fun removeOrUpdateItem(invoiceItem: InvoiceItem) {
        val existingItems = state.value.invoiceItems.toMutableList()

        val existingIndex = existingItems.indexOfFirst { it.sku == invoiceItem.sku }

        if (existingIndex != -1 && existingItems[existingIndex].quantity > 1) {
            // Item exists → update quantity
            val updatedItem = existingItems[existingIndex].copy(
                quantity = existingItems[existingIndex].quantity - 1
            )
            existingItems[existingIndex] = updatedItem
        } else {
            // Item not found → add new one
            existingItems.remove(invoiceItem)
        }
        _state.update {
            it.copy(
                invoiceItems = existingItems
            )
        }
    }

    private fun createInvoice() {
        viewModelScope.launch(Dispatchers.IO) {
            val customer = customerRepository.getAllCustomers().first()
            val business = Business(
                id = uuidGenerator.generateUUID(),
                name = "Jyothika",
                mobile = "9995 42 9878",
                email = "nadhik@gmail.com",
                gstNumber = "32Ox44hsjjsoosjsjj",
                address = "AbcdEfghihs, jjs, sjks, djhjdk, 676567"
            )
            val invoice = Invoice(
                id = uuidGenerator.generateUUID(),
                business = business,
                customer = customer,
                invoiceNumber = "123456789",
                invoiceDate = "12-12-2023",
                items = _state.value.invoiceItems,
                totalPrice = _state.value.totalPrice,
                totalDiscount = _state.value.totalDiscount,
                finalPrice = _state.value.finalPriceToPay,
                isSynced = false
            )
            val pdf = generateInvoicePdfUseCase.generatePdf(invoice)
            _state.update {
                it.copy(
                    invoice = invoice,
                    invoicePDF = pdf,
                    pdfGenerated = true,
                )
            }
            //printPdf(application, pdf, "Invoice")
            //fileHandler.writePdfDocument(pdf)
        }
    }

    // Print helper
    fun printPdf(context: Context, pdfDocument: PdfDocument, jobName: String) {
        _state.update {
            it.copy(
                pdfGenerated = false,
            )
        }
        // Convert PdfDocument to ByteArray
        val outStream = java.io.ByteArrayOutputStream()
        try {
            pdfDocument.writeTo(outStream)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            pdfDocument.close()
        }

        val pdfBytes = outStream.toByteArray()

        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager

        val printAttributes = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.ISO_A5)      // ✅ Page size
            .setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME)  // ✅ Black & white
            .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
            .build()
        val adapter = object : PrintDocumentAdapter() {
            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes?,
                cancellationSignal: CancellationSignal?,
                callback: LayoutResultCallback?,
                extras: Bundle?
            ) {
// Respond that layout is finished
                val info = PrintDocumentInfo
                    .Builder("$jobName.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .build()
                callback?.onLayoutFinished(info, true)
            }

            override fun onWrite(
                pages: Array<out PageRange>?,
                destination: ParcelFileDescriptor?,
                cancellationSignal: CancellationSignal?,
                callback: WriteResultCallback?
            ) {
                try {
                    destination?.let { pfd ->
                        FileOutputStream(pfd.fileDescriptor).use { out ->
                            out.write(pdfBytes)
                        }
                    }
                    callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                } catch (e: Exception) {
                    callback?.onWriteFailed(e.message)
                }
            }
        }

        printManager.print(jobName, adapter, printAttributes)
    }
}