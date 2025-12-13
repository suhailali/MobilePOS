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
                    selectedInvoiceItems = result?.items
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