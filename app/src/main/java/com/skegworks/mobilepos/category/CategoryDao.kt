package com.skegworks.mobilepos.category
import androidx.room.*
import com.skegworks.mobilepos.data.domain.Category
import com.skegworks.mobilepos.data.local.CategoryEntity

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: String): CategoryEntity?

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryEntity>

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    suspend fun deleteAllCategories() {
        val categories = getAllCategories()
        for (category in categories) {
            deleteCategory(category)
        }
    }
}
