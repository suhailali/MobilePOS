package com.skegworks.mobilepos.pos

import com.skegworks.mobilepos.data.domain.Product

data class POSState(
    val invoiceNumber: String = "",
    val product: Product? = null,
    val addedProducts: List<Product> = emptyList(),

    val productFound: Boolean = false,
    val searchingProduct: Boolean = false
)
