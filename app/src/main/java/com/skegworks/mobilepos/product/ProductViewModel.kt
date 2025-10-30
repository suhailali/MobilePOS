package com.skegworks.mobilepos.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.category.CategoryRepository
import com.skegworks.mobilepos.data.domain.Product
import com.skegworks.mobilepos.utils.UUIDGenerator
import com.skegworks.mobilepos.vendors.VendorRepository
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
class ProductViewModel @Inject constructor(
    private val syncProductUseCase: SyncProductUseCase,
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val vendorRepository: VendorRepository,
    private val generateSkuUseCase: GenerateSkuUseCase,
    private val generateBarCodeUseCase: GenerateBarCodeUseCase,
    private val uuidGenerator: UUIDGenerator
) : ViewModel() {

    private val _state = MutableStateFlow(AddProductState())
    val state: StateFlow<AddProductState> = _state.asStateFlow()

    fun handleIntent(intent: AddProductIntent) {
        when (intent) {
            is AddProductIntent.LoadCategories -> {
                loadCategories()
            }

            is AddProductIntent.LoadVendors -> {
                loadVendors()
            }

            is AddProductIntent.UpdateCategory -> _state.update {
                it.copy(
                    textStateCategoryName = intent.category.name,
                    textStateCategoryId = intent.category.id,
                    isSaved = false
                )
            }

            is AddProductIntent.UpdateSku -> _state.update {
                it.copy(
                    textStateSku = intent.sku,
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
                    textStateVendorName = intent.vendor.name,
                    textStateVendorId = intent.vendor.id,
                    isSaved = false
                )
            }

            is AddProductIntent.UpdateHsnCode -> _state.update {
                it.copy(
                    textStateHsnCode = intent.hsnCode,
                    isSaved = false
                )
            }

            is AddProductIntent.UpdateItemPrice -> _state.update {
                it.copy(
                    textStateItemPrice = intent.itemPrice,
                    isSaved = false
                )
            }

            is AddProductIntent.UpdateInputGstPercentage -> _state.update {
                it.copy(
                    textStateInputGstPercentage = intent.inputGstPercentage,
                    isSaved = false
                )
            }

            is AddProductIntent.UpdateOutputGstPercentage -> _state.update {
                it.copy(
                    textStateOutputGstPercentage = intent.outputGstPercentage,
                    isSaved = false
                )
            }

            is AddProductIntent.UpdateSaleMargin -> _state.update {
                it.copy(
                    textStateSaleMargin = intent.saleMargin,
                    isSaved = false
                )
            }

            is AddProductIntent.CalculatePricing -> {
                calculatePricing()
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

            is AddProductIntent.GenerateSku -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val sku = generateSku()
                    _state.update {
                        it.copy(
                            textStateSku = sku,
                            isSaved = false
                        )
                    }
                    generateBarcodeBitmap(sku)
                }
            }
            is AddProductIntent.AddAnother,
            is AddProductIntent.Save -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val product = Product(
                        vendorName = state.value.textStateVendorName,
                        vendorId = state.value.textStateVendorId,
                        categoryId = state.value.textStateCategoryId,
                        hsnCode = state.value.textStateHsnCode,
                        title = state.value.textStateTitle,
                        categoryName = state.value.textStateCategoryName,
                        sku = state.value.textStateSku,
                        size = state.value.textStateSize,
                        color = state.value.textStateColor,


                        quantity = state.value.textStateQuantity,
                        alertQuantity = state.value.textStateAlertQuantity,
                        description = state.value.textStateDescription,
                        imageUrl = state.value.textStateImageUrl,

                        itemPrice = state.value.textStateItemPrice,
                        inputGstPercentage = state.value.textStateInputGstPercentage,
                        inputGst = state.value.textStateInputGst,
                        outputGstPercentage = state.value.textStateOutputGstPercentage,
                        outputGst = state.value.textStateOutputGst,
                        saleMargin = state.value.textStateSaleMargin,
                        cost = state.value.textStateCost,
                        discountPercentage = state.value.textStateDiscountPercentage,
                        discountAmount = state.value.textStateDiscountAmount,
                        salePriceWithoutGst = state.value.textStateSalePriceWithoutGst,
                        salePrice = state.value.textStateSalePrice,
                        finalRoundedOffPrice = state.value.textStateFinalRoundedOffPrice,


                        isActive = state.value.textStateIsActive,
                        isSynced = true,
                        createdAt = state.value.textStateCreatedAt,
                        updatedAt = state.value.textStateUpdatedAt,
                        id = uuidGenerator.generateUUID(),
                        createdBy = "",
                        updatedBy = "",
                    )
                    // syncProductUseCase(product)
                    _state.update {
                        it.copy(
                            isSaved = true
                        )
                    }
                    delay(1000)
                    if (intent is AddProductIntent.AddAnother) {
                        _state.update {
                            it.copy(
                                textStateSize = "",
                                textStateColor = "",
                                textStateQuantity = 0,
                                textStateAlertQuantity = 0
                            )
                        }
                    } else {
                        _state.update {
                            it.clearState()
                        }
                    }
                }
            }
        }
    }

    fun loadCategories() = viewModelScope.launch(Dispatchers.IO) {
        _state.update { it.copy(isLoading = true) }
        try {
            val list = categoryRepository.getAllCategories()
            _state.update {
                it.copy(categories = list, isLoading = false)
            }
        } catch (e: Exception) {
            println("PVM loadCategories crash: ${e.localizedMessage}")
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun loadVendors() = viewModelScope.launch(Dispatchers.IO) {
        _state.update { it.copy(isLoading = true) }
        try {
            val list = vendorRepository.getAllVendors()
            _state.update {
                it.copy(vendors = list, isLoading = false)
            }
        } catch (e: Exception) {
            println("PVM loadVendors crash: ${e.localizedMessage}")
            _state.update { it.copy(isLoading = false) }
        }
    }

    suspend fun generateSku(): String {
        var maxProductId = productRepository.getMaxId()
        if (maxProductId == null) {
            maxProductId = 0
        }
        maxProductId = maxProductId + 1
        return generateSkuUseCase.invoke(
            category = state.value.textStateCategoryName,
            vendor = state.value.textStateVendorName,
            title = state.value.textStateTitle,
            color = state.value.textStateColor,
            size = state.value.textStateSize,
            sequence = maxProductId
        )
    }

    suspend fun generateBarcodeBitmap(sku: String) {
        val bitmap = generateBarCodeUseCase.invoke(sku)
        _state.update {
            it.copy(barcodeBitmap = bitmap)
        }
    }

    private fun calculatePricing() {
        val itemPrice = state.value.textStateItemPrice
        val inputGstPercentage = state.value.textStateInputGstPercentage
        val outputGstPercentage = state.value.textStateOutputGstPercentage
        val saleMargin = state.value.textStateSaleMargin
        val discountPercentage = state.value.textStateDiscountPercentage

        val inputGst = (itemPrice * inputGstPercentage) / 100
        val cost = itemPrice
        val salePriceBeforeGst = cost + (cost * saleMargin / 100)

        val priceAfterDiscount = salePriceBeforeGst - (salePriceBeforeGst * discountPercentage / 100)
        val discountAmount = salePriceBeforeGst - priceAfterDiscount

        val outputGst = (priceAfterDiscount * outputGstPercentage) / 100
        val salePrice = priceAfterDiscount + outputGst
        val finalRoundedOffPrice = salePrice.toInt()

        _state.update {
            it.copy(
                textStateInputGst = inputGst,
                textStateCost = cost,
                textStateSalePriceWithoutGst = salePriceBeforeGst,
                textStateOutputGst = outputGst,
                textStatePriceAfterDiscountWithoutGst = priceAfterDiscount,
                textStateDiscountAmount = discountAmount,
                textStateSalePrice = salePrice,
                textStateFinalRoundedOffPrice = finalRoundedOffPrice,
                isSaved = false
            )
        }
    }

    fun fetchProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                val productList = productRepository.getAllProducts()
                _state.update {
                    it.copy(
                        products = productList,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                println("PVM fetchProducts crash: ${e.localizedMessage}")
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}