package com.skegworks.mobilepos.product

import com.skegworks.mobilepos.data.Product

interface ProductRepository {
    suspend fun insertProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    suspend fun getProductById(id: String): Product?
    suspend fun getAllProducts(): List<Product>

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