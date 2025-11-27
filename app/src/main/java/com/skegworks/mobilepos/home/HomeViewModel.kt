package com.skegworks.mobilepos.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.cashcounter.CashCounterRepository
import com.skegworks.mobilepos.data.domain.CashCounterStatus
import com.skegworks.mobilepos.data.domain.UserRole
import com.skegworks.mobilepos.utils.Constants
import com.skegworks.mobilepos.utils.DateUtility
import com.skegworks.mobilepos.utils.preferences.UserPreferenceHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userPreferenceHandler: UserPreferenceHandler,
    private val cashCounterRepository: CashCounterRepository,
    private val dateUtility: DateUtility
) :
    ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvents>()
    val events = _events.asSharedFlow()

    fun setupUser() {
        viewModelScope.launch {
            val role = userPreferenceHandler.getUserRole()
            val email = userPreferenceHandler.getUserEmail()
            val features = getFeatures(role)
            println("Userrole ${role?.name}")
            _state.update {
                it.copy(
                    userRole = role,
                    userEmail = email,
                    features = features
                )
            }
        }
    }

    private fun getFeatures(role: UserRole?): List<String> {
        when (role) {
            UserRole.ROLE_ADMIN ->
                return listOf(
                    "Sale",
                    "Purchase",
                    "POS",
                    "Dashboard",
                    "Report",
                    "Ledger",
                    "Customers",
                    "Vendors",
                    "Expenses",
                    "Coupons",
                    "Product",
                    "Orders",
                    "Settings",
                    "Category",
                    "Login",
                    "CreateUser"
                )

            UserRole.ROLE_OWNER ->
                return listOf(
                    "POS",
                    "Customers",
                    "Vendors",
                    "Coupons",
                    "Product",
                    "Orders",
                    "Settings",
                    "Category",
                    "Dashboard"
                )

            UserRole.ROLE_STAFF ->
                return listOf(
                    "POS",
                    "Customers",
                    "Expenses",
                    "Coupons",
                )

            null -> return listOf()
        }
    }

    fun openCashCounter() {
        viewModelScope.launch {
            val cashCounter = cashCounterRepository.getLatestCashCounter()
            if (cashCounter == null) {
                _events.emit(HomeEvents.NAVIGATE_TO_CASH_COUNTER)
            } else {
                if (cashCounter.status == CashCounterStatus.CLOSED) {
                    _events.emit(HomeEvents.NAVIGATE_TO_CASH_COUNTER)
                } else {
                    if (dateUtility.isDateToday(cashCounter.date, Constants.DateFormat.DATE_TIME_FORMAT)) {
                        _events.emit(HomeEvents.NAVIGATE_TO_CUSTOMER)
                    } else {
                        _events.emit(HomeEvents.NAVIGATE_TO_CASH_COUNTER)
                    }
                }
            }
        }
    }
}

enum class HomeEvents {
    NAVIGATE_TO_CASH_COUNTER,
    NAVIGATE_TO_POS,
    NAVIGATE_TO_CUSTOMER
}