package com.skegworks.mobilepos.customer

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.coupon.GenerateCouponImageUseCase
import com.skegworks.mobilepos.coupon.UploadCouponForCustomerUseCase
import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.utils.DateUtility
import com.skegworks.mobilepos.utils.UUIDGenerator
import com.skegworks.mobilepos.utils.preferences.UserPreferenceHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
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
    private val fetchCustomerInvoiceUseCase: FetchCustomerInvoiceUseCase,
    private val fetchCustomerCouponUseCase: FetchCustomerCouponUseCase,
    private val generateCouponImageUseCase: GenerateCouponImageUseCase,
    private val uploadCouponForCustomerUseCase: UploadCouponForCustomerUseCase,
    private val uuidGenerator: UUIDGenerator,
    private val dateUtility: DateUtility,
    private val preferenceHandler: UserPreferenceHandler,
    private val updateCustomerUseCase: UpdateCustomerUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AddCustomerState())
    val state: StateFlow<AddCustomerState> = _state.asStateFlow()

    private val _stateSearch = MutableStateFlow(SearchCustomerState())
    val stateSearch: StateFlow<SearchCustomerState> = _stateSearch.asStateFlow()


    private val _stateDetails = MutableStateFlow(CustomerDetailState())
    val stateDetails: StateFlow<CustomerDetailState> = _stateDetails.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            stateSearch.map {
                it.textStatePhone
            }.debounce(400)
                .distinctUntilChanged()
                .collectLatest { value ->
                    fetchCustomers(value)
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

    fun handleCustomerDetailsIntent(intent: CustomerDetailsIntent) {
        when (intent) {
            CustomerDetailsIntent.ClearCoupon -> {
                _stateDetails.update {
                    it.copy(
                        generatedCoupon = null
                    )
                }
            }
            is CustomerDetailsIntent.CreateNewCoupon -> {
                createCoupon(intent.customer)
            }

            is CustomerDetailsIntent.SetCustomerDetail -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val customer = fetchCustomerUseCase.getById(intent.customerId)
                    println("VM " + customer?.name)
                    customer?.let {
                        _state.update {
                            it.copy(
                                textStateName = customer.name,
                                textStatePhone = customer.phone,
                                textStateAddress = customer.address,
                                textStateEmail = customer.email
                            )
                        }
                    }
                    val invoices = fetchCustomerInvoiceUseCase.invoke(intent.customerId)
                    println("VM1 " + customer?.name)
                    val coupons = fetchCustomerCouponUseCase.invoke(intent.customerId)
                    println("VM inv size" + invoices.size)
                    println("VM2 " + customer?.name)
                    println("VM coupon size" + coupons.size)
                    _stateDetails.update {
                        it.copy(
                            customer = customer,
                            invoiceList = invoices,
                            couponList = coupons
                        )
                    }
                    println("VM3 " + customer?.name)
                }
            }

            is CustomerDetailsIntent.Update -> {
                if (stateDetails.value.customer ==null || stateDetails.value.customer?.id == null) {
                    return
                }
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
                    isSynced = false,
                    id = stateDetails.value.customer?.id ?: ""
                )

                viewModelScope.launch(Dispatchers.IO) {
                    updateCustomerUseCase(customer)
                    _state.update {
                        it.copy(
                            isSaved = true
                        )
                    }
                    _stateDetails.update {
                        it.copy(
                            customer = customer
                        )
                    }
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

    fun fetchCustomers(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = fetchCustomerUseCase(value)
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

    private fun createCoupon(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            val couponCode = generateRandomLetterString(12).toUpperCase(Locale.current)
            val coupon = Coupon(
                id = uuidGenerator.generateUUID(),
                title = "Thank You Coupon",
                discountCode = couponCode,
                description = "Coupon for Purchase",
                discountPercentage = 10.0,
                discountGivenToName = customer.name,
                discountGivenToId = customer.id,
                discountGivenToNumber = customer.phone,
                discountType = "PERCENTAGE",
                discountValidTill = dateUtility.daysAfterInMillis(30),
                discountAvailedBy = "",
                discountAvailedOn = "",
                discountedAmount = 0.0,
                invoiceNumber = "",
                invoiceId = "",
                isActive = true,
                isDeleted = false,
                createdAt = dateUtility.getCurrentTimeStamp(),
                createdBy = preferenceHandler.getUserEmail() ?: "",
                updatedAt = dateUtility.getCurrentTimeStamp(),
                updatedBy = preferenceHandler.getUserEmail() ?: "",
                isSynced = false
            )

            val couponGenerated = generateCouponImageUseCase(
                "Coupon_On_The_Go_Three.png",
                couponCode,
                customer.name,
                "10%",
                dateUtility.daysAfterInDateString(10, "MMM dd, yyyy"),
                dateUtility.daysAfterInDateString(30, "MMM dd, yyyy"),
            )

            if (couponGenerated != null) {
                _stateDetails.update {
                    it.copy(
                        generatedCoupon = couponGenerated
                    )
                }
                uploadCouponForCustomerUseCase(coupon)
                val coupons = fetchCustomerCouponUseCase.invoke(customer.id)
                _stateDetails.update {
                    it.copy(
                        couponList = coupons
                    )
                }
            }
        }
    }

    private fun generateRandomLetterString(length: Int = 10): String {
        val allowedChars = ('a'..'z') + ('A'..'Z')
        return buildString(length) {
            repeat(length) {
                append(allowedChars.random())
            }
        }.toUpperCase(Locale.current)
    }
}