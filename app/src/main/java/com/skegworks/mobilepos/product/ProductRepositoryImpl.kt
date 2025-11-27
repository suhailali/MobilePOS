package com.skegworks.mobilepos.product

import com.skegworks.mobilepos.data.domain.Product
import com.skegworks.mobilepos.data.mapper.toDomain
import com.skegworks.mobilepos.data.mapper.toEntity
import com.skegworks.mobilepos.data.mapper.toFirestoreDto
import com.skegworks.mobilepos.sync.SyncData
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val syncData: SyncData
) : ProductRepository {
    override suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product.toEntity())
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product.toEntity())
    }

    override suspend fun updateProductQuantity(productId: String, quantity: Int){
        productDao.subtractFromQuantity(productId, quantity)
    }

    override suspend fun deleteProduct(product: Product) {
        // Implementation here
    }

    override suspend fun getProductById(id: String): Product? {
        return productDao.getProductById(id)?.toDomain()
    }

    override suspend fun getProductForSku(sku: String): List<Product>? {
        return productDao.getProductBySku(sku)?.map {
            it.toDomain()
        }
    }

    override suspend fun getAllProducts(): List<Product> {
        // Implementation here
        return productDao.getAllProducts().map {
            it.toDomain()
        }
    }

    override suspend fun getTotalProductCount(): Int {
        return productDao.getTotalProductCount()
    }

    override suspend fun getTotalProductQuantity(): Int {
        return productDao.getTotalProductQuantity()
    }

    override suspend fun getTotalOutOfStockProductCount(): Int {
        return productDao.getTotalOutOfStockProductCount()
    }

    override suspend fun getTotalInactiveProductCount(): Int {
        return productDao.getTotalProductCount(isActive = false)
    }

    override suspend fun getTotalCostOfProductsInStockWithOutGST(): Double {
        return productDao.getTotalCostOfProductsInStockWithOutGST()
    }

    override suspend fun getTotalInputGSTOfProductsInStock(): Double {
        return productDao.getTotalInputGSTOfProductsInStock()
    }

    override suspend fun getTotalCostOfProductsWithOutGST(): Double {
        return productDao.getTotalCostOfProductsWithOutGST()
    }

    override suspend fun getTotalInputGSTOfProducts(): Double {
        return productDao.getTotalInputGSTOfProducts()
    }

    override suspend fun getTotalPriceOfProductsInStockWithOutGST(): Double {
        return productDao.getTotalPriceOfProductsInStockWithOutGST()
    }

    override suspend fun getTotalOutputGSTOfProductsInStock(): Double {
        return productDao.getTotalOutputGSTOfProductsInStock()
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
            data = product.toFirestoreDto(),
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override suspend fun syncAllProducts(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        //TODO do transformation if required
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
