package com.skegworks.mobilepos.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.appsettings.GenerateNewProductCounterUseCase
import com.skegworks.mobilepos.appsettings.SyncAppSettingsUseCase
import com.skegworks.mobilepos.appsettings.UpdateProductCounterUseCase
import com.skegworks.mobilepos.category.CategoryRepository
import com.skegworks.mobilepos.data.domain.PriceInput
import com.skegworks.mobilepos.data.domain.PriceOutput
import com.skegworks.mobilepos.data.domain.Product
import com.skegworks.mobilepos.data.domain.UserRole
import com.skegworks.mobilepos.utils.UUIDGenerator
import com.skegworks.mobilepos.utils.preferences.UserPreferenceHandler
import com.skegworks.mobilepos.vendors.VendorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val uuidGenerator: UUIDGenerator,
    private val priceCalculationUseCase: CalculateProductPriceUseCase,
    private val userPreferenceHandler: UserPreferenceHandler,
    private val productCounterUseCase: GenerateNewProductCounterUseCase,
    private val updateProductCounterUseCase: UpdateProductCounterUseCase,
    private val syncAppSettingsUseCase: SyncAppSettingsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductState())
    val state: StateFlow<ProductState> = _state.asStateFlow()

    private val _navigateBack = MutableSharedFlow<Boolean>()
    val navigateBack = _navigateBack.asSharedFlow()

    fun handleProductDetailIntent(intent: ProductDetailIntent) {
        when (intent) {
            is ProductDetailIntent.DeleteProduct -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val product = createProductModel(true)
                    product.apply {
                        isDeleted = true
                        createdBy = state.value.productDetail?.createdBy ?: ""
                        updatedBy = userPreferenceHandler.getUserEmail() ?: ""
                        updatedAt = System.currentTimeMillis()
                    }
                    productRepository.updateProduct(product)
                    syncProductUseCase(product)
                    _state.update {
                        it.copy(
                            isSaved = true
                        )
                    }
                    _navigateBack.emit(true)
                }
            }

            is ProductDetailIntent.FetchProduct -> {
                fetchProduct(intent.id)
            }

            is ProductDetailIntent.UpdateProduct -> {
                if (!areFieldsValid()) return
                viewModelScope.launch(Dispatchers.IO) {
                    val product = createProductModel(true)
                    product.apply {
                        createdBy = state.value.productDetail?.createdBy ?: ""
                        updatedBy = userPreferenceHandler.getUserEmail() ?: ""
                        updatedAt = System.currentTimeMillis()
                    }
                    productRepository.updateProduct(product)
                    syncProductUseCase(product)
                    _state.update {
                        it.copy(
                            isSaved = true
                        )
                    }
                    _navigateBack.emit(true)
                }
            }

            is ProductDetailIntent.AddAnotherProduct -> {
                if (!areFieldsValid()) return
                viewModelScope.launch(Dispatchers.IO) {
                    val product = createProductModel()
                    productRepository.insertProduct(product)
                    syncProductUseCase(product)
                    _state.update {
                        it.copy(
                            isSaved = true
                        )
                    }
                    delay(1000)
                    _state.update {
                        it.copy(
                            textStateSize = "",
                        )
                    }
                    _navigateBack.emit(true)
                }
            }
        }
    }

    fun handleIntent(intent: AddProductIntent) {
        when (intent) {
            is AddProductIntent.SearchItem -> {
                fetchProducts(state.value.textStateProductSearchTerm)
            }

            is AddProductIntent.UpdateSearchTerm -> {
                _state.update {
                    it.copy(
                        textStateProductSearchTerm = intent.searchTerm
                    )
                }
            }

            is AddProductIntent.NavigateToAddProduct -> {
                _state.update {
                    it.clearState()
                }
            }

            is AddProductIntent.LoadCategories -> {
                loadCategories()
            }

            is AddProductIntent.LoadVendors -> {
                loadVendors()
            }

            is AddProductIntent.GetUserRole -> {
                viewModelScope.launch(Dispatchers.IO) {
                    userPreferenceHandler.getUserRole()?.let { role ->
                        if (role != UserRole.ROLE_STAFF) {
                            _state.update {
                                it.copy(isUserStaff = false)
                            }
                        }
                    }
                }
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
                val isValid = haveFieldForPriceCalculationValid()
                updatePriceCalculationValidationState(isValid)
                if (isValid.not()) return
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
                val isValid = haveFieldsForSkuValid()
                updateSkuValidationState(isValid)
                if (isValid.not()) return
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

                if (!areFieldsValid()) return

                viewModelScope.launch(Dispatchers.IO) {
                    val product = createProductModel()
                    productRepository.insertProduct(product)
                    syncProductUseCase(product)
                    if (intent is AddProductIntent.Save) {
                        updateProductCounterUseCase.invoke()
                        syncAppSettingsUseCase.invoke()
                        _state.update {
                            it.copy(
                                productSaved = true
                            )
                        }
                    }
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
                                textStateColor = ""
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

    private fun createProductModel(isForUpdate: Boolean = false): Product {
        val id = if (isForUpdate) {
            state.value.productDetail?.id ?: uuidGenerator.generateUUID()
        } else {
            uuidGenerator.generateUUID()
        }

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
            salePriceWithoutDiscount = state.value.textStateSalePriceWithoutDiscount,


            isActive = state.value.textStateIsActive,
            isDeleted = false,
            isSynced = false,
            createdAt = state.value.textStateCreatedAt,
            updatedAt = state.value.textStateUpdatedAt,
            id = id,
            createdBy = "",
            updatedBy = "",
        )
        return product
    }

    private fun areFieldsValid(): Boolean {
        val isSkuValid = haveFieldsForSkuValid()
        updateSkuValidationState(isSkuValid)

        val isPriceValid = haveFieldForPriceCalculationValid()
        updatePriceCalculationValidationState(isPriceValid)

        val isSaveValid = haveFieldForSaveProductValid()
        updateSaveProductValidationState(isSaveValid)

        return !(isSaveValid.not() || isPriceValid.not() || isSaveValid.not())
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
        val maxProductId = productCounterUseCase.invoke()
        return generateSkuUseCase.invoke(
            category = state.value.textStateCategoryName,
            vendor = state.value.textStateVendorName,
            title = state.value.textStateTitle,
            color = state.value.textStateColor,
            size = state.value.textStateSize,
            sequence = maxProductId.toInt()
        )
    }

    suspend fun generateBarcodeBitmap(sku: String) {
        val bitmap = generateBarCodeUseCase.invoke(sku)
        _state.update {
            it.copy(barcodeBitmap = bitmap)
        }
    }

    private fun calculatePricing() {

        val outputPrice = priceCalculationUseCase.invoke(
            readInputPrice()
        )

        updateOutputPrice(outputPrice)
    }

    private fun readInputPrice(): PriceInput {
        val itemPrice = state.value.textStateItemPrice
        val inputGstPercentage = state.value.textStateInputGstPercentage
        val outputGstPercentage = state.value.textStateOutputGstPercentage
        val saleMargin = state.value.textStateSaleMargin
        val discountPercentage = state.value.textStateDiscountPercentage

        val inputPrice = PriceInput(
            itemPrice = itemPrice,
            inputGstPercentage = inputGstPercentage,
            outputGstPercentage = outputGstPercentage,
            saleMargin = saleMargin,
            discountPercentage = discountPercentage,
            additionalDiscountPercentage = 0.0
        )
        return inputPrice
    }

    private fun updateOutputPrice(outputPrice: PriceOutput) {
        _state.update {
            it.copy(
                textStateInputGst = outputPrice.inputGst,
                textStateCost = outputPrice.cost,
                textStateSalePriceWithoutGst = outputPrice.salePriceBeforeGst,
                textStateOutputGst = outputPrice.outputGst,
                textStatePriceAfterDiscountWithoutGst = outputPrice.priceAfterDiscountWithoutGst,
                textStateDiscountAmount = outputPrice.discountAmount,
                textStateSalePrice = outputPrice.salePrice,
                textStateSalePriceWithoutDiscount = outputPrice.priceWithoutDiscount,
                textStateFinalRoundedOffPrice = outputPrice.finalRoundedOffPrice,
                isSaved = false
            )
        }
    }

    fun fetchProducts(sku: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                val productList =
                    if (sku.isEmpty()) {
                        productRepository.getAllProducts()
                    } else {
                        productRepository.getProductForSku(sku)
                    }
                _state.update {
                    it.copy(
                        products = productList,
                        isLoading = false,
                        productSaved = false,
                    )
                }
            } catch (e: Exception) {
                println("PVM fetchProducts crash: ${e.localizedMessage}")
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun fetchProduct(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val productFetched = productRepository.getProductById(id)
            productFetched?.let { product ->
                _state.update {
                    it.clearState()
                    it.productDetail = productFetched
                    it.copy(
                        textStateCategoryId = product.categoryId,
                        textStateCategoryName = product.categoryName,

                        textStateVendorId = product.vendorId,
                        textStateVendorName = product.vendorName,

                        textStateTitle = product.title,
                        // TODO Convert enum value
                        textStateSize = product.size,
                        textStateColor = product.color,
                        textStateSku = product.sku,

                        textStateItemPrice = product.itemPrice,
                        textStateInputGstPercentage = product.inputGstPercentage,
                        textStateOutputGstPercentage = product.outputGstPercentage,
                        textStateSaleMargin = product.saleMargin,
                        textStateInputGst = product.inputGst,
                        textStateCost = product.cost,
                        textStateSalePriceWithoutGst = product.salePriceWithoutGst,
                        textStateOutputGst = product.outputGst,
                        textStateSalePrice = product.salePrice,
                        textStateSalePriceWithoutDiscount = product.salePriceWithoutDiscount,
                        textStateFinalRoundedOffPrice = product.finalRoundedOffPrice,

                        textStateHsnCode = product.hsnCode,
                        textStateQuantity = product.quantity,
                        textStateAlertQuantity = product.alertQuantity,
                        textStateDescription = product.description,
                        textStateImageUrl = product.imageUrl,
                        textStateIsActive = product.isActive,
                        textStateDiscountPercentage = product.discountPercentage,
                        textStateDiscountAmount = product.discountAmount,
                        textStateCreatedAt = product.createdAt,
                    )

                }
                val inputPrice = readInputPrice()
                val outputPrice = priceCalculationUseCase.invoke(
                    inputPrice
                )
                _state.update {
                    it.copy(
                        textStatePriceAfterDiscountWithoutGst = outputPrice.priceAfterDiscountWithoutGst
                    )
                }
            }

        }
    }

    private fun haveFieldsForSkuValid(): Boolean {
        val title = state.value.textStateTitle
        val category = state.value.textStateCategoryName

        val isGood = title.isNotEmpty() && category.isNotEmpty()
        return isGood
    }

    private fun updateSkuValidationState(isValid: Boolean) {
        if (isValid) {
            _state.update {
                it.copy(
                    errorSku = false,
                    validationErrorMessageSku = "Title and Category are required"
                )
            }
        } else {
            _state.update {
                it.copy(
                    errorSku = true,
                    validationErrorMessageSku = "Title and Category are required",
                    textStateSku = ""
                )
            }
        }
    }

    private fun haveFieldForPriceCalculationValid(): Boolean {
        val itemPrice = state.value.textStateItemPrice
        val outputGstPercentage = state.value.textStateOutputGstPercentage

        val saleMargin = state.value.textStateSaleMargin

        val isValid = itemPrice != 0.0 && outputGstPercentage != 0.0 && saleMargin != 0

        return isValid
    }

    private fun updatePriceCalculationValidationState(isValid: Boolean) {
        if (isValid) {
            _state.update {
                it.copy(
                    errorCalculatePrice = false,
                )
            }
        } else {
            _state.update {
                it.copy(
                    errorCalculatePrice = true,
                    validationErrorMessageCalculatePrice = "Item Price, Output GST % and Sale Margin are required",
                    textStateInputGst = 0.0,
                    textStateCost = 0.0,
                    textStateSalePrice = 0.0,
                    textStateDiscountAmount = 0.0,
                    textStateSalePriceWithoutGst = 0.0,
                    textStateOutputGst = 0.0,
                    textStateFinalRoundedOffPrice = 0,
                    textStatePriceAfterDiscountWithoutGst = 0.0
                )
            }
        }
    }

    private fun haveFieldForSaveProductValid(): Boolean {
        val hsnCode = state.value.textStateHsnCode
        val quantity = state.value.textStateQuantity

        val size = state.value.textStateSize
        val color = state.value.textStateColor

        val sku = state.value.textStateSku

        val isValid = hsnCode.isNotEmpty() && quantity > 0
                && size.isNotEmpty() && color.isNotEmpty()
                && sku.isNotEmpty()
        return isValid
    }

    private fun updateSaveProductValidationState(isValid: Boolean) {
        if (isValid) {
            _state.update {
                it.copy(
                    errorSave = false,
                )
            }
        } else {
            _state.update {
                it.copy(
                    errorSave = true,
                    validationErrorMessageSave = "Make sure Size, Color, SKU, HSN Code and Quantity are added",
                )
            }
        }
    }
}