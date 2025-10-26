package com.skegworks.mobilepos.product

import com.skegworks.mobilepos.data.Product
import com.skegworks.mobilepos.sync.SyncData
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val syncData: SyncData
) : ProductRepository {
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
        return productDao.getAllProducts()
    }

    override suspend fun getMaxId(): Int? {
        return productDao.getMaxId()
    }

    override suspend fun syncProduct(
        product: Product,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        syncData.uploadData(
            name = "products",
            id = product.id,
            data = product,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override suspend fun syncAllProducts(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val products = productDao.getAllProducts()
        for (product in products) {
            syncData.uploadData(
                name = "products",
                id = product.id,
                data = product,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }
}
