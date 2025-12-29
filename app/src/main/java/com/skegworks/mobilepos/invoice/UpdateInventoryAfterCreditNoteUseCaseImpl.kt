package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.product.ProductRepository
import com.skegworks.mobilepos.product.SyncProductUseCase

class UpdateInventoryAfterCreditNoteUseCaseImpl(private val productRepository: ProductRepository, private val syncProductUseCase: SyncProductUseCase):
    UpdateInventoryAfterCreditNoteUseCase {
    override suspend fun invoke(items: List<InvoiceItem>) {
        items.forEach { item ->
            productRepository.increaseProductQuantity(item.productId, item.quantity)
            productRepository.getProductById(item.productId)?.let {
                syncProductUseCase.invoke(it)
            }
        }
    }
}