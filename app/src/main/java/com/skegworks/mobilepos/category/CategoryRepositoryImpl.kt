package com.skegworks.mobilepos.category

import com.skegworks.mobilepos.data.Category
import com.skegworks.mobilepos.sync.SyncData

class CategoryRepositoryImpl(private val categoryDao: CategoryDao, private val syncData: SyncData): CategoryRepository {
    override suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    override suspend fun getCategoryById(id: String): Category? {
        return categoryDao.getCategoryById(id)
    }

    override suspend fun getAllCategories(): List<Category> {
        return categoryDao.getAllCategories()
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    override suspend fun deleteAllCategories() {
        categoryDao.deleteAllCategories()
    }

    override suspend fun syncCategory(category: Category, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        syncData.uploadData(
            name = "categories",
            id = category.id,
            data = category,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override suspend fun syncAllCategories(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val categories = categoryDao.getAllCategories()
        for (category in categories) {
            syncData.uploadData(
                name = "categories",
                id = category.id,
                data = category,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }

}