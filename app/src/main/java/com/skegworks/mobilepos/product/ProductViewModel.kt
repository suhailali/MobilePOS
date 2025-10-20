package com.skegworks.mobilepos.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.data.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {

    private val _state = MutableStateFlow(AddProductState())
    val state: StateFlow<AddProductState> = _state.asStateFlow()


    fun handleIntent(intent: AddProductIntent) {
        when (intent) {
            is AddProductIntent.UpdateCategory -> _state.update {
                it.copy(
                    textStateCategoryName = intent.category,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateSku -> _state.update {
                it.copy(
                    textStateSku = intent.sku,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateCost -> _state.update {
                it.copy(
                    textStateCost = intent.cost,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateSize -> _state.update {
                it.copy(
                    textStateSize = intent.size,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateColor -> _state.update {
                it.copy(
                    textStateColor = intent.color,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateTitle -> _state.update {
                it.copy(
                    textStateTitle = intent.title,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateVendor -> _state.update {
                it.copy(
                    textStateVendorName = intent.vendor,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateHsnCode -> _state.update {
                it.copy(
                    textStateHsnCode = intent.hsnCode,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateSalePrice -> _state.update {
                it.copy(
                    textStateSalePrice = intent.salePrice,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateQuantity -> _state.update {
                it.copy(
                    textStateQuantity = intent.quantity,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateAlertQuantity -> _state.update {
                it.copy(
                    textStateAlertQuantity = intent.alertQuantity,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateDescription -> _state.update {
                it.copy(
                    textStateDescription = intent.description,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateImageUrl -> _state.update {
                it.copy(
                    textStateImageUrl = intent.imageUrl,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateGstPercentage -> _state.update {
                it.copy(
                    textStateGstPercentage = intent.gstPercentage,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateIsActive -> _state.update {
                it.copy(
                    textStateIsActive = intent.isActive,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateDiscountPercentage -> _state.update {
                it.copy(
                    textStateDiscountPercentage = intent.discountPercentage,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateCreatedAt -> _state.update {
                it.copy(
                    textStateCreatedAt = intent.createdAt,
                    isSaved = false
                )
            }
            is AddProductIntent.UpdateUpdatedAt -> _state.update {
                it.copy(
                    textStateUpdatedAt = intent.updatedAt,
                    isSaved = false
                )
            }
            is AddProductIntent.Save -> {
                _state.update {
                    it.copy(
                        isSaved = true
                    )
                }

                viewModelScope.launch(Dispatchers.IO) {
                    productRepository.insertProduct(
                        Product(
                            vendorName = state.value.textStateVendorName,
                            vendorId = state.value.textStateVendorId,
                            categoryId = state.value.textStateCategoryId,
                            hsnCode = state.value.textStateHsnCode,
                            title = state.value.textStateTitle,
                            categoryName = state.value.textStateCategoryName,
                            sku = state.value.textStateSku,
                            size = state.value.textStateSize,
                            color = state.value.textStateColor,
                            cost = state.value.textStateCost,
                            salePrice = state.value.textStateSalePrice,
                            quantity = state.value.textStateQuantity,
                            alertQuantity = state.value.textStateAlertQuantity,
                            description = state.value.textStateDescription,
                            imageUrl = state.value.textStateImageUrl,
                            gstPercentage = state.value.textStateGstPercentage,
                            isActive = state.value.textStateIsActive,
                            discountPercentage = state.value.textStateDiscountPercentage,
                            createdAt = state.value.textStateCreatedAt,
                            updatedAt = state.value.textStateUpdatedAt
                        )
                    )
                }
            }
        }
    }
}