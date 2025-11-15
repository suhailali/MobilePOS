package com.skegworks.mobilepos.pos

import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.product.ProductRepository

class UpdateInventoryAfterSaleUseCaseImpl(private val productRepository: ProductRepository): UpdateInventoryAfterSaleUseCase {
    override suspend fun invoke(invoice: Invoice) {
        invoice.items.forEach { item ->
            productRepository.updateProductQuantity(item.productId, item.quantity)
        }
    }
}