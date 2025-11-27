package com.skegworks.mobilepos.product

sealed class ProductDetailIntent {
    object DeleteProduct: ProductDetailIntent()
    object UpdateProduct: ProductDetailIntent()
    data class FetchProduct(val id: String): ProductDetailIntent()
}