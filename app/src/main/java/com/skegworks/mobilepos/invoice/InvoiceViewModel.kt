package com.skegworks.mobilepos.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
private val getInvoicesUseCase:GetInvoicesUseCase
): ViewModel() {

    private val _state = MutableStateFlow(InvoiceState())
    val state: StateFlow<InvoiceState> = _state.asStateFlow()

    fun handleIntent(intent: InvoiceIntent) {
        when (intent) {
            is InvoiceIntent.LoadInvoices -> loadInvoices()
            is InvoiceIntent.SelectInvoice -> selectInvoice(intent.id)
        }
    }

    private fun selectInvoice(id: String) {

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