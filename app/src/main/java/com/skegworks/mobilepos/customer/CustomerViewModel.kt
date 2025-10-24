package com.skegworks.mobilepos.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.data.Customer
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CustomerViewModel @Inject constructor(private val syncCustomerUseCase: SyncCustomerUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow(AddCustomerState())
    val state: StateFlow<AddCustomerState> = _state.asStateFlow()

    fun handleIntent(intent: AddCustomerIntent) {
        when (intent) {
            is AddCustomerIntent.UpdateName -> _state.update {
                it.copy(
                    textStateName = intent.name,
                    isSaved = false
                )
            }

            is AddCustomerIntent.UpdateAddress -> _state.update {
                it.copy(
                    textStateAddress = intent.address,
                    isSaved = false
                )
            }

            is AddCustomerIntent.UpdateEmail -> _state.update {
                it.copy(
                    textStateEmail = intent.email,
                    isSaved = false
                )
            }

            is AddCustomerIntent.UpdatePhone -> _state.update {
                it.copy(
                    textStatePhone = intent.phone,
                    isSaved = false
                )
            }

            is AddCustomerIntent.UpdateCreatedAt -> _state.update {
                it.copy(
                    textStateCreatedAt = intent.createdAt,
                    isSaved = false
                )
            }

            is AddCustomerIntent.UpdateUpdatedAt -> _state.update {
                it.copy(
                    textStateUpdatedAt = intent.updatedAt,
                    isSaved = false
                )
            }

            is AddCustomerIntent.Save -> {
                val customer = Customer(
                    name = _state.value.textStateName,
                    email = _state.value.textStateEmail,
                    phone = _state.value.textStatePhone,
                    address = _state.value.textStateAddress,
                    isActive = _state.value.textStateIsActive,
                    createdAt = _state.value.textStateCreatedAt,
                    updatedAt = _state.value.textStateUpdatedAt
                )

                viewModelScope.launch(Dispatchers.IO) {
                    syncCustomerUseCase(customer)
                    _state.update {
                        it.copy(
                            isSaved = true
                        )
                    }
                    delay(1000)
                    _state.update {
                        it.clearState()
                    }
                }
            }
        }
    }
}