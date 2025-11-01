package com.skegworks.mobilepos.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.data.domain.UserRole
import com.skegworks.mobilepos.utils.preferences.UserPreferenceHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userPreferenceHandler: UserPreferenceHandler) :
    ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun getUserRole() {
        viewModelScope.launch {
            val role = userPreferenceHandler.getUserRole()
            val features = getFeatures(role)
            println("Userrole ${role?.name}")
            _state.update {
                it.copy(
                    userRole = role,
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
}