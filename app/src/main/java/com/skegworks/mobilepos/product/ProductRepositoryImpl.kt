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
        return productDao.getAllProducts()
    }

    override suspend fun getMaxId(): Int? {
        return productDao.getMaxId()
    }
}

//class ProductRepository(
//    private val dao: ProductDao,
//    private val firestore: FirebaseFirestore
//) {
//
//    suspend fun addProduct(product: ProductEntity) {
//        dao.insert(product.copy(isSynced = false))
//    }
//
//    suspend fun syncWithFirestore() {
//        val unsynced = dao.getUnsynced()
//        for (item in unsynced) {
//            try {
//                firestore.collection("products").document(item.id)
//                    .set(item)
//                    .await()
//                dao.insert(item.copy(isSynced = true))
//            } catch (e: Exception) {
//                Log.e("Sync", "Failed to sync ${item.id}: ${e.message}")
//            }
//        }
//    }
//}
