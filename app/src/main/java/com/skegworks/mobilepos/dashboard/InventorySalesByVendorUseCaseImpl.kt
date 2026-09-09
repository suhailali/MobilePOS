package com.skegworks.mobilepos.dashboard

import com.skegworks.mobilepos.invoice.InvoiceRepository
import com.skegworks.mobilepos.product.ProductRepository
import javax.inject.Inject

class InventorySalesByVendorUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository,
    private val invoiceRepository: InvoiceRepository
) : InventorySalesByVendorUseCase {
    override suspend fun invoke(): List<InventorySaleByVendor> {

        var products = productRepository.getAllProducts().sortedBy { it.vendorName }

        val vendorMap = mutableMapOf<String, InventorySaleByVendor>()

        products.forEach {
            val value = vendorMap[it.vendorId]
            if (value != null) {
                vendorMap[it.vendorId] = value.copy(
                    stockQuantity = value.stockQuantity + it.quantity,
                    unsoldAmount = value.unsoldAmount + (it.quantity * it.finalRoundedOffPrice),
                    unsoldCost = value.unsoldCost + (it.quantity * it.cost)
                )
            } else {
                vendorMap[it.vendorId] =
                    InventorySaleByVendor(
                        it.vendorId,
                        it.vendorName,
                        it.quantity,
                        0,
                        0.0,
                        (it.quantity * it.finalRoundedOffPrice).toDouble(),
                        (it.quantity * it.cost),
                        0.0
                    )
            }
        }

        products = listOf()

        var invoices = invoiceRepository.getAllInvoices()

        invoices.forEach { invoice ->
            invoice.items.forEach { item ->
                val value = vendorMap[item.vendorId]
                if (value != null) {
                    vendorMap[item.vendorId] = value.copy(
                        soldQuantity = value.soldQuantity + item.quantity,
                        salesAmount = value.salesAmount + (item.quantity * item.finalRoundedOffPrice),
                        profit = value.profit + (item.quantity * (item.finalRoundedOffPrice - item.cost))
                    )
                } else {
                    vendorMap[item.vendorId] =
                        InventorySaleByVendor(
                            item.vendorId,
                            item.vendorName,
                            0,
                            item.quantity,
                            (item.quantity * item.finalRoundedOffPrice).toDouble(),
                            0.0,
                            0.0,
                            (item.quantity * (item.finalRoundedOffPrice - item.cost))
                        )
                }
            }
        }

        invoices = listOf()

        return vendorMap.values.toList().sortedBy { it.vendorName }
    }
}