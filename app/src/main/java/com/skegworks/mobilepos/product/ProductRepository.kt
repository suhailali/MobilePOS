package com.skegworks.mobilepos.product

import com.skegworks.mobilepos.data.Product

interface ProductRepository {
    suspend fun insertProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    suspend fun getProductById(id: String): Product?
    suspend fun getAllProducts(): List<Product>

    suspend fun getMaxId(): Int?
}