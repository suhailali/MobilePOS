package com.skegworks.mobilepos.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.sync.LoadInvoicesFromFireStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase,
    private val getInvoiceDetailUseCase: GetInvoiceDetailUseCase,
    private val loadInvoicesFromFireStoreUseCase: LoadInvoicesFromFireStoreUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(InvoiceState())
    val state: StateFlow<InvoiceState> = _state.asStateFlow()

    fun handleIntent(intent: InvoiceIntent) {
        when (intent) {
            InvoiceIntent.LoadInvoices -> loadInvoices()
            is InvoiceIntent.SelectInvoice -> selectInvoice(intent.id)
            InvoiceIntent.SyncInvoices -> {
                _state.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                }
                syncInvoices()
            }

            is InvoiceIntent.CreditNoteInvoiceItem -> {
                var creditNoteInvoiceItems = _state.value.creditNoteInvoiceItems?.toMutableList()
                val invoiceItem = creditNoteInvoiceItems?.find { it.id == intent.invoiceItem.id }
                if (creditNoteInvoiceItems == null) {
                    creditNoteInvoiceItems = mutableListOf()
                }
                //TODO Handle partial quantity returned
                if (intent.addItem) {
                    if (invoiceItem == null) {
                        creditNoteInvoiceItems.add(intent.invoiceItem)
                    }
                } else {
                    creditNoteInvoiceItems.remove(intent.invoiceItem)
                }
                _state.update {
                    it.copy(
                        creditNoteInvoiceItems = creditNoteInvoiceItems
                    )
                }
            }

            InvoiceIntent.ConfirmCreditNote -> {
                viewModelScope.launch(Dispatchers.IO) {

                }
            }
        }
    }

    private fun syncInvoices() {
        loadInvoicesFromFireStoreUseCase.invoke {
            _state.update {
                it.copy(
                    isLoading = false,
                    errorMessage = null,
                )
            }
            loadInvoices()
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

    private fun loadInvoices() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getInvoicesUseCase.invoke()
            _state.update {
                it.copy(
                    invoices = result
                )
            }
        }
    }
}