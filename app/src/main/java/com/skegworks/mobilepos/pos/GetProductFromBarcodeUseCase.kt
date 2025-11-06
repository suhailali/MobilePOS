package com.skegworks.mobilepos.pos

import com.skegworks.mobilepos.data.domain.Product

interface GetProductFromBarcodeUseCase {
    suspend operator fun invoke(barcode: String): List<Product>?
}