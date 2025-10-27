package com.skegworks.mobilepos.vendors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.data.domain.Vendor
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
class VendorViewModel @Inject constructor(private val syncVendorUseCase: SyncVendorUseCase) : ViewModel() {

    private val _state = MutableStateFlow(VendorDetailsState())
    val state: StateFlow<VendorDetailsState> = _state.asStateFlow()

    fun handleIntent(intent: VendorDetailsIntent) {
        when (intent) {
            is VendorDetailsIntent.UpdateName -> _state.update {
                it.copy(
                    textStateName = intent.value,
                    isSaved = false
                )
            }

            is VendorDetailsIntent.UpdateAddress -> _state.update {
                it.copy(
                    textStateAddress = intent.value,
                    isSaved = false
                )
            }

            is VendorDetailsIntent.UpdateCity -> _state.update {
                it.copy(
                    textStateCity = intent.value,
                    isSaved = false
                )
            }

            is VendorDetailsIntent.UpdateState -> _state.update {
                it.copy(
                    textStateState = intent.value,
                    isSaved = false
                )
            }

            is VendorDetailsIntent.UpdateCountry -> _state.update {
                it.copy(
                    textStateCountry = intent.value,
                    isSaved = false
                )
            }

            is VendorDetailsIntent.UpdatePhone -> _state.update {
                it.copy(
                    textStatePhone = intent.value,
                    isSaved = false
                )
            }

            is VendorDetailsIntent.UpdateEmail -> _state.update {
                it.copy(
                    textStateEmail = intent.value,
                    isSaved = false
                )
            }

            is VendorDetailsIntent.UpdateZipCode -> _state.update {
                it.copy(
                    textStateZipCode = intent.value,
                    isSaved = false
                )
            }

            is VendorDetailsIntent.UpdateGST -> _state.update {
                it.copy(
                    textStateGST = intent.value,
                    isSaved = false
                )
            }

            is VendorDetailsIntent.UpdateGSTPercentage -> _state.update {
                it.copy(
                    textStateGSTPercentage = intent.value,
                    isSaved = false
                )
            }

            is VendorDetailsIntent.UpdateCurrency -> _state.update {
                it.copy(
                    textStateCurrency = intent.value,
                    isSaved = false
                )
            }

            VendorDetailsIntent.Save -> {
                val currentState = _state.value
                val vendor = Vendor(
                    name = currentState.textStateName,
                    address = currentState.textStateAddress,
                    city = currentState.textStateCity,
                    state = currentState.textStateState,
                    country = currentState.textStateCountry,
                    phone = currentState.textStatePhone,
                    email = currentState.textStateEmail,
                    zipCode = currentState.textStateZipCode,
                    gst = currentState.textStateGST,
                    gstPercentage = currentState.textStateGSTPercentage,
                    currency = currentState.textStateCurrency,
                )

                viewModelScope.launch(Dispatchers.IO) {
                    syncVendorUseCase(vendor)
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

    private fun saveData(currentState: VendorDetailsState) {
        // do the operation
        val vendorDetails = mapOf(
            "Name" to currentState.textStateName,
            "Address" to currentState.textStateAddress,
            "City" to currentState.textStateCity,
            "State" to currentState.textStateState,
            "Phone" to currentState.textStatePhone,
            "Email" to currentState.textStateEmail,
            "ZipCode" to currentState.textStateZipCode,
            "GST" to currentState.textStateGST,
            "GST" to currentState.textStateGSTPercentage,
            "Currency" to currentState.textStateCurrency,
            )


//        addVendorToFirebase(vendorDetails)
    }

//    private fun addVendorToFirebase(vendor: Map<String, String>) {
//        val firestore = FirestoreHelper()
//        firestore.addDocument("vendors", vendor,
//            onSuccess = { id -> Log.d("Firestore", "Added with ID: $id") },
//            onFailure = { e -> Log.e("Firestore", "Error: $e") })
//    }
}


