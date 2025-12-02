package com.skegworks.mobilepos.pos

import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.product.ProductRepository
import com.skegworks.mobilepos.product.SyncProductUseCase

class UpdateInventoryAfterSaleUseCaseImpl(private val productRepository: ProductRepository, private val syncProductUseCase: SyncProductUseCase): UpdateInventoryAfterSaleUseCase {
    override suspend fun invoke(invoice: Invoice) {
        invoice.items.forEach { item ->
            productRepository.updateProductQuantity(item.productId, item.quantity)
            productRepository.getProductById(item.productId)?.let {
                syncProductUseCase.invoke(it)
            }
        }
    }
}