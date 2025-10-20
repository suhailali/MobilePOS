package com.skegworks.mobilepos.category

data class AddCategoryState(

    val textStateDescription: String = "",
    val textStateCategoryName: String = "",
    val textStateIsActive: Boolean = true,
    val textStateCreatedAt: Long = System.currentTimeMillis(),
    val textStateUpdatedAt: Long = System.currentTimeMillis(),
    val isSaved: Boolean = false,
)
