package com.skegworks.mobilepos.product

import com.skegworks.mobilepos.data.domain.Product

interface ProductRepository {
    suspend fun insertProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun increaseProductQuantity(productId: String, quantity: Int)
    suspend fun decreaseProductQuantity(productId: String, quantity: Int)
    suspend fun deleteProduct(product: Product)
    suspend fun getProductById(id: String): Product?
    suspend fun getProductForSku(sku: String): List<Product>
    suspend fun getAllProducts(): List<Product>
    suspend fun getLastUpdatedProducts(): List<Product>

    suspend fun getTotalProductCount(): Int

    suspend fun getTotalProductQuantity(): Int

    suspend fun getTotalOutOfStockProductCount(): Int

    suspend fun getTotalInactiveProductCount(): Int

    suspend fun getTotalCostOfProductsInStockWithOutGST(): Double

    suspend fun getTotalInputGSTOfProductsInStock(): Double

    suspend fun getTotalCostOfProductsWithOutGST(): Double

    suspend fun getTotalInputGSTOfProducts(): Double

    suspend fun getTotalPriceOfProductsInStockWithOutGST(): Double

    suspend fun getTotalOutputGSTOfProductsInStock(): Double

    suspend fun getProductForVendor(vendorId: String): List<Product>

    suspend fun getMaxId(): Int?

    suspend fun syncProduct(
        product: Product,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    )

    suspend fun syncAllProducts(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    )
}