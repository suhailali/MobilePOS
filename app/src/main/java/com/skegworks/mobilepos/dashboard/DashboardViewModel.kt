package com.skegworks.mobilepos.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.invoice.InvoiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val invoiceRepository: InvoiceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    fun loadDashboardData() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getDashboardDataUseCase.invoke()

            val chartPoints = invoiceRepository.getInvoicesAmountByDate()

            _state.update {
                it.copy(
                    dashboardValue = result,
                    salesPerDay = chartPoints
                )
            }
        }
    }
}