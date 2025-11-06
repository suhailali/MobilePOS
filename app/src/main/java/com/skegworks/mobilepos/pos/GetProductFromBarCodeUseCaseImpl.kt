package com.skegworks.mobilepos.pos

import com.skegworks.mobilepos.data.domain.Product
import com.skegworks.mobilepos.product.ProductRepository

class GetProductFromBarCodeUseCaseImpl(private val productRepository: ProductRepository): GetProductFromBarcodeUseCase {
    override suspend fun invoke(barcode: String): List<Product>? {
        return productRepository.getProductForSku(barcode)
    }
}