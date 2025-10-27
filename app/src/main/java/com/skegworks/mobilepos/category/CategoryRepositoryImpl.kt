package com.skegworks.mobilepos.category

import com.skegworks.mobilepos.data.domain.Category
import com.skegworks.mobilepos.data.mapper.toDomain
import com.skegworks.mobilepos.data.mapper.toEntity
import com.skegworks.mobilepos.data.mapper.toFirestoreDto
import com.skegworks.mobilepos.sync.SyncData

class CategoryRepositoryImpl(private val categoryDao: CategoryDao, private val syncData: SyncData): CategoryRepository {
    override suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category.toEntity())
    }

    override suspend fun getCategoryById(id: String): Category? {
        return categoryDao.getCategoryById(id)?.toDomain()
    }

    override suspend fun getAllCategories(): List<Category> {
        return categoryDao.getAllCategories().map {
            it.toDomain()
        }
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category.toEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category.toEntity())
    }

    override suspend fun deleteAllCategories() {
        categoryDao.deleteAllCategories()
    }

    override suspend fun syncCategory(category: Category, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        syncData.uploadData(
            name = "categories",
            id = category.id,
            data = category.toFirestoreDto(),
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override suspend fun syncAllCategories(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        //TODO Transform model if required
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