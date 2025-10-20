package com.skegworks.mobilepos.category

sealed class AddCategoryIntent {
    data class UpdateDescription(val description: String): AddCategoryIntent()
    data class UpdateCategory(val category: String) : AddCategoryIntent()
    data class UpdateCreatedAt(val createdAt: Long) : AddCategoryIntent()
    data class UpdateUpdatedAt(val updatedAt: Long) : AddCategoryIntent()

    object Save : AddCategoryIntent()
}