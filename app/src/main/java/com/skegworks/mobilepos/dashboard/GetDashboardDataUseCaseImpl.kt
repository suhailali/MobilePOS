package com.skegworks.mobilepos.dashboard

import com.skegworks.mobilepos.customer.CustomerRepository
import com.skegworks.mobilepos.data.domain.Dashboard
import com.skegworks.mobilepos.invoice.InvoiceRepository
import com.skegworks.mobilepos.product.ProductRepository
import javax.inject.Inject

class GetDashboardDataUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository,
    private val customerRepository: CustomerRepository,
    private val invoiceRepository: InvoiceRepository,
) : GetDashboardDataUseCase {
    override suspend fun invoke(): Dashboard {
        val dashboard = Dashboard(
            totalNoOfProducts = productRepository.getTotalProductCount(),
            totalQuantityOfProducts = productRepository.getTotalProductQuantity(),
            outOfStockProducts = productRepository.getTotalOutOfStockProductCount(),
            totalInactiveProduct = productRepository.getTotalInactiveProductCount(),
            totalCostOfProductsInStockWithOutGST = productRepository.getTotalCostOfProductsInStockWithOutGST(),
            totalInputGSTOfProductsInStock = productRepository.getTotalInputGSTOfProductsInStock(),
            totalCostOfProductsWithOutGST = productRepository.getTotalCostOfProductsWithOutGST(),
            totalInputGSTOfProducts = productRepository.getTotalInputGSTOfProducts(),
            totalPriceOfProductsInStockWithOutGST = productRepository.getTotalPriceOfProductsInStockWithOutGST(),
            totalOutputGSTOfProductsInStock = productRepository.getTotalOutputGSTOfProductsInStock(),
            totalNoOfSale = 0,
            totalSaleAmountWithoutGST = 0.0,
            totalSaleAmountWithGst = 0.0,
            totalNoOfReturn = 0,
            totalAmountOfReturnWithoutGST = 0.0,
            totalAmountOfReturnWithGST = 0.0,
            totalNumberOfCustomer = 0,
            totalNumberOfRepeatedCustomer = 0,
            totalCouponDiscountGiven = 0.0,
            totalExpense = 0.0
        )
        return dashboard
    }
}