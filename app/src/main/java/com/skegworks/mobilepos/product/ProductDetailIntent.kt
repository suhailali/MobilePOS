package com.skegworks.mobilepos.product

sealed class ProductDetailIntent {
    data class DeleteProduct(val id: String): ProductDetailIntent()
    object UpdateProduct: ProductDetailIntent()
    object AddAnotherProduct: ProductDetailIntent()
    data class FetchProduct(val id: String): ProductDetailIntent()
}