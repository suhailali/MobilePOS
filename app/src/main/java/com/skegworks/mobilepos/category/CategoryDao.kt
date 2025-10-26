package com.skegworks.mobilepos.category
import androidx.room.*
import com.skegworks.mobilepos.data.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: String): Category?

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<Category>

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    suspend fun deleteAllCategories() {
        val categories = getAllCategories()
        for (category in categories) {
            deleteCategory(category)
        }
    }
}
