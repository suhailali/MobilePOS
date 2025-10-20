package com.skegworks.mobilepos.product

import com.skegworks.mobilepos.data.Product
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val productDao: ProductDao): ProductRepository {
    override suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    override suspend fun updateProduct(product: Product) {
        // Implementation here
    }

    override suspend fun deleteProduct(product: Product) {
        // Implementation here
    }

    override suspend fun getProductById(id: String): Product? {
        // Implementation here
        return null
    }

    override suspend fun getAllProducts(): List<Product> {
        // Implementation here
        return emptyList()
    }
}