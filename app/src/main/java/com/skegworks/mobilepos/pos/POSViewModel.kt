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
import com.skegworks.mobilepos.appsettings.SyncAppSettingsUseCase
import com.skegworks.mobilepos.business.BusinessRepository
import com.skegworks.mobilepos.customer.CustomerRepository
import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.data.domain.InvoiceState
import com.skegworks.mobilepos.data.domain.PriceInput
import com.skegworks.mobilepos.data.domain.Product
import com.skegworks.mobilepos.data.mapper.toInvoiceItem
import com.skegworks.mobilepos.invoice.GenerateInvoicePdfUseCase
import com.skegworks.mobilepos.invoice.GenerateNewInvoiceNumberUseCase
import com.skegworks.mobilepos.invoice.SyncInvoiceUseCase
import com.skegworks.mobilepos.invoice.UpdateInvoiceNumberUseCase
import com.skegworks.mobilepos.print.SeznikPrinterManager
import com.skegworks.mobilepos.product.CalculateProductPriceUseCase
import com.skegworks.mobilepos.utils.DateUtility
import com.skegworks.mobilepos.utils.UUIDGenerator
import com.skegworks.mobilepos.utils.files.FileHandler
import com.skegworks.mobilepos.utils.preferences.UserPreferenceHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class POSViewModel @Inject constructor(
    application: Application,
    private val getProductFromBarcodeUseCase: GetProductFromBarcodeUseCase,
    private val getCouponFromBarcodeUseCase: GetCouponFromBarcodeUseCase,
    private val generateInvoicePdfUseCase: GenerateInvoicePdfUseCase,
    private val customerRepository: CustomerRepository,
    private val fileHandler: FileHandler,
    private val uuidGenerator: UUIDGenerator,
    private val syncInvoiceUseCase: SyncInvoiceUseCase,
    private val syncAppSettingsUseCase: SyncAppSettingsUseCase,
    private val generateNewInvoiceNumberUseCase: GenerateNewInvoiceNumberUseCase,
    private val updateInvoiceNumberUseCase: UpdateInvoiceNumberUseCase,
    private val dateUtility: DateUtility,
    private val calculatePriceUseCase: CalculateProductPriceUseCase,
    private val businessRepository: BusinessRepository,
    private val updateInventoryAfterSaleUseCase: UpdateInventoryAfterSaleUseCase,
    private val userPreferenceHandler: UserPreferenceHandler
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
            val productsFound = getProductFromBarcodeUseCase.invoke(barcode)
            productsFound?.let { products ->
                println("Product found for barcode $barcode")
                _state.update {
                    it.copy(
                        searchResultProduct = products
                    )
                }
            }
        }
    }

    fun getCouponForBarcode(barcode: String) {
        println("get Coupon for barcode $barcode")
        viewModelScope.launch(Dispatchers.IO) {
            val couponsFound = getCouponFromBarcodeUseCase.invoke(barcode)
            couponsFound?.let { coupons ->
                _state.update {
                    it.copy(
                        searchResultCoupon = coupons
                    )
                }
            }
        }
    }

    private fun addOrUpdateProduct(product: Product) {
        val existingItems = state.value.invoiceItems.toMutableList()
        val newItem = product.toInvoiceItem(uuidGenerator.generateUUID())
        if (product.quantity != 0) {
            val existingIndex = existingItems.indexOfFirst { it.productId == newItem.productId }

            // -1 means, it is an existing item
            if (existingIndex != -1) {
                //Check if invoice item quantity exceeds product available quantity
                if (existingItems[existingIndex].quantity < product.quantity) {
                    // Item exists → update quantity
                    val updatedItem = existingItems[existingIndex].copy(
                        quantity = existingItems[existingIndex].quantity + newItem.quantity
                    )
                    existingItems[existingIndex] = updatedItem
                    _state.update {
                        it.copy(
                            product = product,
                            invoiceItems = existingItems,
                            productFound = true,
                            errorBarcodeScreen = null
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            errorBarcodeScreen = "You can not add this product. Already ${product.quantity} quantity added."
                        )
                    }
                }
            } else {
                // Item not found → add new one
                existingItems.add(newItem)
                _state.update {
                    it.copy(
                        product = product,
                        invoiceItems = existingItems,
                        productFound = true,
                        errorBarcodeScreen = null
                    )
                }
            }
        } else {
            _state.update {
                it.copy(
                    errorBarcodeScreen = "Product Quantity is Zero. You can not add this product."
                )
            }
        }

    }

    private fun addOrUpdateCoupon(coupon: Coupon) {
        applyCouponToInvoiceItems(coupon)
        calculateTotalPrice()
        _state.update {
            it.copy(
                coupon = coupon,
                productFound = true
            )
        }
    }

    private fun calculateTotalPrice() {
        var totalPrice = 0
        var totalPriceBeforeDiscount = 0.0
        state.value.invoiceItems.forEach { item ->
            totalPrice = totalPrice + (item.finalRoundedOffPrice * item.quantity)
            totalPriceBeforeDiscount =
                totalPriceBeforeDiscount + (item.salePriceWithoutDiscount * item.quantity)
        }

        val discount = totalPriceBeforeDiscount - totalPrice
        val toPay = totalPrice - state.value.cashDiscount

        _state.update {
            it.copy(
                totalPrice = totalPrice.toDouble(),
                totalDiscount = discount,
                finalPriceToPay = toPay,
            )
        }
    }

    fun handleIntent(intent: POSIntent) {
        when (intent) {
            is POSIntent.ResetError -> {
                _state.update {
                    it.copy(
                        errorBarcodeScreen = null
                    )
                }
            }
            is POSIntent.RemoveCashDiscount -> {
                _state.update {
                    it.copy(
                        cashDiscount = 0.0
                    )
                }
                calculateTotalPrice()
            }

            is POSIntent.RemoveCoupon -> {
                removeCouponForInvoiceItems()
                _state.update {
                    it.copy(
                        coupon = null
                    )
                }
            }

            is POSIntent.AddProduct -> {
                addOrUpdateProduct(intent.product)
                calculateTotalPrice()
            }

            is POSIntent.UpdateScanState -> {
                _state.update {
                    it.copy(
                        productFound = false
                    )
                }
            }

            is POSIntent.PrintInvoice -> {
                createInvoice()
            }

            is POSIntent.Payment -> {
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.invoice?.let {
                        syncInvoiceUseCase.invoke(it)
                    }
                }
                //update invoice counter and date
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.invoice?.let {
                        updateInventoryAfterSaleUseCase.invoke(it)
                    }
                    updateInvoiceNumberUseCase.invoke()
                    syncAppSettingsUseCase.invoke()
                    _state.update {
                        it.copy(
                            paymentComplete = true
                        )
                    }
                }
            }

            is POSIntent.AddCustomer -> {
                _state.update {
                    it.copy(
                        customer = intent.customer,
                    )
                }
            }

            is POSIntent.RemoveItem -> {
                removeOrUpdateItem(intent.invoiceItem)
                calculateTotalPrice()
            }

            is POSIntent.AddCoupon -> {
                addOrUpdateCoupon(intent.coupon)
                calculateTotalPrice()
            }

            is POSIntent.SearchItem -> {
                when (intent.scanType) {
                    BarcodeScanType.PRODUCT ->
                        if (state.value.searchTerm.isNotEmpty() &&
                            state.value.searchTerm.length > 2
                        ) {
                            getProductForBarcode(state.value.searchTerm)
                        }

                    BarcodeScanType.COUPON ->
                        getCouponForBarcode(state.value.searchTerm)
                }
            }

            is POSIntent.UpdateSearchTerm -> {
                _state.update {
                    it.copy(
                        searchTerm = intent.term
                    )
                }
            }

            is POSIntent.AddCashDiscount -> {
                _state.update {
                    it.copy(
                        cashDiscount = intent.cashDiscount
                    )
                }
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
                invoiceItems = existingItems,
            )
        }
    }

    private fun createInvoice() {
        viewModelScope.launch(Dispatchers.IO) {
            val customer = state.value.customer
            val business = state.value.business
            if (customer != null && business != null) {
                val invoice = Invoice(
                    id = uuidGenerator.generateUUID(),
                    business = business,
                    customer = customer,
                    invoiceNumber = state.value.invoiceNumber,
                    invoiceDate = state.value.invoiceDate,
                    items = state.value.invoiceItems,
                    totalPrice = state.value.totalPrice,
                    totalDiscount = state.value.totalDiscount,
                    finalPrice = state.value.finalPriceToPay,
                    isSynced = false,
                    cashDiscount = state.value.cashDiscount,
                    invoiceState = InvoiceState.PRINT
                )
                invoice.items.forEach { invoiceItem ->
                    invoiceItem.apply {
                        invoiceId = invoice.id
                        invoiceNumber = invoice.invoiceNumber
                        invoiceDate = System.currentTimeMillis()
                        isSynced = false
                        updatedBy = userPreferenceHandler.getUserEmail() ?: ""
                        updatedAt = System.currentTimeMillis()
                    }
                }
                val pdf = generateInvoicePdfUseCase.generatePdf(invoice)
                _state.update {
                    it.copy(
                        invoicePDF = pdf,
                        pdfGenerated = true,
                        invoice = invoice
                    )
                }
            }
            //printPdf(application, pdf, "Invoice")
            //fileHandler.writePdfDocument(pdf)
        }
    }

    // Print helper
    fun printPdf(context: Context, pdfDocument: PdfDocument, jobName: String) {
        _state.update {
            it.copy(
                pdfGenerated = false
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

    fun getInvoiceNumber() {
        viewModelScope.launch(Dispatchers.IO) {
            val invoiceNumber = generateNewInvoiceNumberUseCase.invoke()
            _state.update {
                it.copy(
                    invoiceNumber = invoiceNumber,
                    invoiceDate = dateUtility.getDateTime()
                )
            }
        }
    }

    fun getBusiness() {
        viewModelScope.launch(Dispatchers.IO) {
            val business = businessRepository.getBusiness()
            _state.update {
                it.copy(
                    business = business
                )
            }
        }
    }

    private fun removeCouponForInvoiceItems() {
        val existingItems = state.value.invoiceItems.toMutableList()
        val newList = mutableListOf<InvoiceItem>()
        viewModelScope.launch(Dispatchers.IO) {
            for (invoiceItem in existingItems) {
                val product = getProductFromBarcodeUseCase.invoke(invoiceItem.sku)?.first()
                product?.let {
                    val inputPrice = PriceInput(
                        itemPrice = product.itemPrice,
                        inputGstPercentage = product.inputGstPercentage,
                        outputGstPercentage = product.outputGstPercentage,
                        saleMargin = product.saleMargin,
                        discountPercentage = product.discountPercentage,
                        additionalDiscountPercentage = 0.0
                    )
                    val newInvoiceItem = invoiceItem.copy()
                    setOutputPriceForInvoiceItem(newInvoiceItem, inputPrice)
                    newList.add(newInvoiceItem)
                }
            }
            withContext(Dispatchers.Main) {
                _state.update {
                    it.copy(
                        invoiceItems = newList
                    )
                }
                calculateTotalPrice()
            }
        }
    }

    private fun applyCouponToInvoiceItems(coupon: Coupon) {
        val existingItems = state.value.invoiceItems.toMutableList()
        for (invoiceItem in existingItems) {
            val inputPrice = PriceInput(
                itemPrice = invoiceItem.itemPrice,
                inputGstPercentage = invoiceItem.inputGstPercentage,
                outputGstPercentage = invoiceItem.outputGstPercentage,
                saleMargin = invoiceItem.saleMargin,
                discountPercentage = invoiceItem.discountPercentage,
                additionalDiscountPercentage = coupon.discountPercentage
            )
            setOutputPriceForInvoiceItem(invoiceItem, inputPrice)
        }
    }

    private fun setOutputPriceForInvoiceItem(invoiceItem: InvoiceItem, inputPrice: PriceInput) {
        val outputPrice = calculatePriceUseCase.invoke(inputPrice)

        invoiceItem.inputGst = outputPrice.inputGst
        invoiceItem.cost = outputPrice.cost
        invoiceItem.salePriceWithoutGst = outputPrice.salePriceBeforeGst
        invoiceItem.outputGst = outputPrice.outputGst
        invoiceItem.discountAmount = outputPrice.discountAmount
        invoiceItem.salePriceWithoutGst = outputPrice.priceAfterDiscountWithoutGst
        invoiceItem.salePrice = outputPrice.salePrice
        invoiceItem.salePriceWithoutDiscount = outputPrice.priceWithoutDiscount
        invoiceItem.finalRoundedOffPrice = outputPrice.finalRoundedOffPrice
    }
}