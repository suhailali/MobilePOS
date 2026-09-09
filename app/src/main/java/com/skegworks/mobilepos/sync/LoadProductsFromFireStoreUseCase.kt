package com.skegworks.mobilepos.sync

import com.skegworks.mobilepos.data.domain.Product

interface LoadProductsFromFireStoreUseCase {
    operator fun invoke(): List<Product>
}