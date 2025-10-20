package com.skegworks.mobilepos.category

import com.skegworks.mobilepos.data.Category

interface CategoryRepository {
    suspend fun insertCategory(category: Category)

    suspend fun getCategoryById(id: String) : Category?

    suspend fun getAllCategories(): List<Category>

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(category: Category)
}