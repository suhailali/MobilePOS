package com.skegworks.mobilepos.product

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.skegworks.mobilepos.data.local.ProductEntity

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: String): ProductEntity?

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<ProductEntity>

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("SELECT MAX(id) FROM products")
    suspend fun getMaxId(): Int?

    @Query("SELECT * FROM products WHERE is_synced = 0")
    suspend fun getUnsynced(): List<ProductEntity>
}