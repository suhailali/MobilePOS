package com.skegworks.mobilepos.invoice

import android.content.Context
import android.graphics.pdf.PdfDocument
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.BuildConfig
import com.skegworks.mobilepos.customer.SearchCustomerIntent
import com.skegworks.mobilepos.print.PrintPdfUseCase
import com.skegworks.mobilepos.sync.LoadInvoicesFromFireStoreUseCase
import com.skegworks.mobilepos.sync.SyncPendingInvoicesUseCase
import com.skegworks.mobilepos.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase,
    private val getInvoiceDetailUseCase: GetInvoiceDetailUseCase,
    private val loadInvoicesFromFireStoreUseCase: LoadInvoicesFromFireStoreUseCase,
    private val generateNewCreditNoteUseCase: GenerateNewCreditNoteUseCase,
    private val updateInventoryAfterCreditNoteUseCase: UpdateInventoryAfterCreditNoteUseCase,
    private val printPdfUseCase: PrintPdfUseCase,
    private val generateInvoicePdfUseCase: GenerateInvoicePdfUseCase,
    private val syncPendingInvoicesUseCase: SyncPendingInvoicesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(InvoiceState())
    val state: StateFlow<InvoiceState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<InvoiceEvents>()
    val events = _events.asSharedFlow()

    private val pageSize = 30

    fun handleIntent(intent: InvoiceIntent) {
        when (intent) {
            InvoiceIntent.PrintInvoice -> {
                state.value.selectedInvoice?.let {
                    val customerInfo = if (BuildConfig.VENDOR_NAME == "Salwariya") {
                        Constants.Invoice.infoForCustomerPremium
                    } else {
                        Constants.Invoice.infoForCustomerDefault
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        val pdfDocument = generateInvoicePdfUseCase.generatePdf(it, customerInfo)
                        _state.update { invoiceState ->
                            invoiceState.copy(
                                invoicePDF = pdfDocument,
                                pdfGenerated = true
                            )
                        }
                    }
                }
            }
            InvoiceIntent.LoadInvoices -> loadInvoices(state.value.textStatePhone)
            InvoiceIntent.LoadNextPage -> loadNextPage()
            is InvoiceIntent.SelectInvoice -> selectInvoice(intent.id)
            InvoiceIntent.SyncInvoices -> {
                _state.update {
                    it.copy(
                        isSyncing = true,
                        errorMessage = null
                    )
                }
                syncInvoices()
            }

            is InvoiceIntent.ItemForPartialQuantityUpdate -> {
                _state.update {
                    it.copy(
                        invoiceItemForPartialQuantityUpdate = intent.invoiceItem
                    )
                }
            }

            is InvoiceIntent.CreditNoteInvoiceItem -> {
                var creditNoteInvoiceItems = _state.value.creditNoteInvoiceItems?.toMutableList()
                val invoiceItem = creditNoteInvoiceItems?.find { it.id == intent.invoiceItem.id }
                if (creditNoteInvoiceItems == null) {
                    creditNoteInvoiceItems = mutableListOf()
                }
                if (intent.addItem) {
                    // Didn't find the item in list - add to the list
                    if (invoiceItem == null) {
                        creditNoteInvoiceItems.add(intent.invoiceItem.copy(
                            quantity = intent.quantity
                        ))
                    }
                } else {
                    creditNoteInvoiceItems.remove(invoiceItem)
                }
                _state.update {
                    it.copy(
                        creditNoteInvoiceItems = creditNoteInvoiceItems
                    )
                }
            }

            is InvoiceIntent.ConfirmCreditNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.selectedInvoice?.let { invoice ->
                        state.value.creditNoteInvoiceItems?.let { items ->
                            generateNewCreditNoteUseCase.invoke(
                                invoice,
                                items,
                                intent.returnDescription
                            )
                            updateInventoryAfterCreditNoteUseCase.invoke(items)
                        }
                    }
                    _state.update {
                        it.copy(
                            creditNoteInvoiceItems = null,
                            invoiceItemForPartialQuantityUpdate = null,
                        )
                    }
                    _events.emit(InvoiceEvents.NAVIGATE_BACK)
                }
            }

            is InvoiceIntent.SearchInvoices -> {
                _state.update {
                    it.copy(
                        textStatePhone = intent.searchValue
                    )
                }
                loadInvoices(searchValue = state.value.textStatePhone)
            }
        }
    }

    private fun syncInvoices() {
        // First push all the unsynced invoices
        if (state.value.unsyncedInvoices > 0) {
            viewModelScope.launch(Dispatchers.IO) {
                // may be added for delay as timestamp is used
                delay(2000.milliseconds)
                syncPendingInvoicesUseCase.invoke()
            }
        }

        // Pull all invoices from Firebase
        loadInvoicesFromFireStoreUseCase.invoke {
            _state.update {
                it.copy(
                    isSyncing = false,
                    errorMessage = null,
                )
            }
            // state update invoices to load ui
            loadInvoices(state.value.textStatePhone)
        }
    }


    private fun selectInvoice(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getInvoiceDetailUseCase.invoke(id)
            _state.update {
                it.copy(
                    selectedInvoice = result,
                    selectedInvoiceItems = result?.items,
                    creditNoteInvoiceItems = null
                )
            }
        }
    }

    private fun loadInvoices(searchValue: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true, currentPage = 0, isLastPage = false) }
            val result = getInvoicesUseCase.invoke(limit = pageSize, offset = 0, searchValue = searchValue)
            _state.update {
                it.copy(
                    invoices = result,
                    isLoading = false,
                    isLastPage = result.size < pageSize
                )
            }
        }
    }

    private fun loadNextPage() {
        if (state.value.isLoading || state.value.isLastPage) return

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            val nextPage = state.value.currentPage + 1
            val result = getInvoicesUseCase.invoke(limit = pageSize, offset = nextPage * pageSize, searchValue = state.value.textStatePhone)
            _state.update {
                it.copy(
                    invoices = it.invoices + result,
                    isLoading = false,
                    currentPage = nextPage,
                    isLastPage = result.size < pageSize
                )
            }
        }
    }

    fun printPdf(context: Context, pdfDocument: PdfDocument, jobName: String) {
        _state.update {
            it.copy(
                pdfGenerated = false
            )
        }
        printPdfUseCase.invoke(context, pdfDocument, jobName)
    }
}
