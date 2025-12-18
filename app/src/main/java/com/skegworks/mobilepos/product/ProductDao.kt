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

    @Query("SELECT * FROM products WHERE sku LIKE '%' || :sku || '%' AND is_deleted = false AND is_active = true ORDER by updated_at DESC LIMIT 100")
    suspend fun getProductBySku(sku: String): List<ProductEntity>

    @Query("SELECT * FROM products WHERE is_deleted = false AND is_active = true ORDER by updated_at DESC LIMIT 100")
    suspend fun getAllProducts(): List<ProductEntity>

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE products SET quantity = quantity - :amount, is_synced = false, updated_at = :updatedAt WHERE id = :productId")
    suspend fun subtractFromQuantity(productId: String, amount: Int, updatedAt: Long = System.currentTimeMillis())

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("SELECT Count(*) FROM products")
    suspend fun getMaxId(): Int?

    @Query("SELECT * FROM products WHERE is_synced = 0")
    suspend fun getUnsynced(): List<ProductEntity>

    @Query("SELECT Count(*) FROM products WHERE is_active = :isActive")
    suspend fun getTotalProductCount(isActive: Boolean = true): Int

    @Query("SELECT SUM(quantity) FROM products WHERE is_active = :isActive")
    suspend fun getTotalProductQuantity(isActive: Boolean = true): Int

    @Query("SELECT Count(*) FROM products WHERE quantity = 0 AND is_active = :isActive")
    suspend fun getTotalOutOfStockProductCount(isActive: Boolean = true): Int

    @Query("SELECT SUM(cost) FROM products WHERE quantity > 0 AND is_active = :isActive")
    suspend fun getTotalCostOfProductsInStockWithOutGST(isActive: Boolean = true): Double

    @Query("SELECT SUM(input_gst) FROM products WHERE quantity > 0 AND is_active = :isActive")
    suspend fun getTotalInputGSTOfProductsInStock(isActive: Boolean = true): Double

    @Query("SELECT SUM(cost) FROM products")
    suspend fun getTotalCostOfProductsWithOutGST(): Double

    @Query("SELECT SUM(input_gst) FROM products")
    suspend fun getTotalInputGSTOfProducts(): Double

    @Query("SELECT SUM(final_rounded_off_price) FROM products WHERE quantity > 0 AND is_active =:isActive")
    suspend fun getTotalPriceOfProductsInStockWithOutGST(isActive: Boolean = true): Double

    @Query("SELECT SUM(output_gst) FROM products WHERE quantity > 0 AND is_active = :isActive")
    suspend fun getTotalOutputGSTOfProductsInStock(isActive: Boolean = true): Double
}