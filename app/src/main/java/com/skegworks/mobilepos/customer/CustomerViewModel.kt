package com.skegworks.mobilepos.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.utils.UUIDGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val syncCustomerUseCase: SyncCustomerUseCase,
    private val fetchCustomerUseCase: FetchCustomerUseCase,
    private val uuidGenerator: UUIDGenerator
) : ViewModel() {

    private val _state = MutableStateFlow(AddCustomerState())
    val state: StateFlow<AddCustomerState> = _state.asStateFlow()

    private val _stateSearch = MutableStateFlow(SearchCustomerState())
    val stateSearch: StateFlow<SearchCustomerState> = _stateSearch.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            stateSearch.map {
                it.textStatePhone
            }.debounce(400)
                .distinctUntilChanged()
                .collectLatest { phone ->
                    fetchCustomers(phone)
                }
        }
    }

    @OptIn(FlowPreview::class)
    fun handleSearchIntent(intent: SearchCustomerIntent) {
        when (intent) {
            is SearchCustomerIntent.SearchPhone -> {
                _stateSearch.update {
                    it.copy(
                        textStatePhone = intent.phone
                    )
                }
            }
        }
    }


    fun handleIntent(intent: AddCustomerIntent) {
        when (intent) {
            is AddCustomerIntent.SetData -> {
                _state.update {
                    it.copy(
                        textStatePhone = stateSearch.value.textStatePhone
                    )
                }
            }

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
                if (isCustomerNameValid().not()) {
                    _state.update {
                        it.copy(
                            error = "Enter Valid Name!"
                        )
                    }
                    return
                }

                if (isCustomerPhoneValid().not()) {
                    _state.update {
                        it.copy(
                            error = "Enter Valid Phone  Number!"
                        )
                    }
                    return
                }
                val customer = Customer(
                    name = state.value.textStateName,
                    email = state.value.textStateEmail,
                    phone = state.value.textStatePhone,
                    address = state.value.textStateAddress,
                    isActive = state.value.textStateIsActive,
                    createdAt = state.value.textStateCreatedAt,
                    updatedAt = state.value.textStateUpdatedAt,
                    isSynced = true,
                    id = uuidGenerator.generateUUID()
                )

                viewModelScope.launch(Dispatchers.IO) {
                    syncCustomerUseCase(customer)
                    _state.update {
                        it.copy(
                            isSaved = true
                        )
                    }
                    _stateSearch.update {
                        it.copy(
                            textStatePhone = state.value.textStatePhone
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

    private fun isCustomerNameValid(): Boolean {
        return state.value.textStateName.isNotEmpty() && state.value.textStateName.length > 2
    }

    private fun isCustomerPhoneValid(): Boolean {
        //TODO phone number length should be configurable and based on country
        return state.value.textStatePhone.isNotEmpty() && state.value.textStatePhone.length == 10
    }

    fun fetchCustomers(phone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = fetchCustomerUseCase.invoke(phone)
            if (list == null) {
                _stateSearch.update {
                    it.copy(
                        error = "No Data Found"
                    )
                }
            } else {
                _stateSearch.update {
                    it.copy(
                        customers = list
                    )
                }
            }
        }
    }
}