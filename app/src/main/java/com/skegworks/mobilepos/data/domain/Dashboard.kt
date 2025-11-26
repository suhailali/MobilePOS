package com.skegworks.mobilepos.data.domain

data class Dashboard(
    // product number
    val totalNoOfProducts: Int,
    val totalQuantityOfProducts: Int,
    val outOfStockProducts: Int,
    val totalInactiveProduct: Int,

    // product price
    val totalCostOfProductsInStockWithOutGST: Double,
    val totalInputGSTOfProductsInStock: Double,
    val totalCostOfProductsWithOutGST: Double,
    val totalInputGSTOfProducts: Double,
    val totalPriceOfProductsInStockWithOutGST: Double,
    val totalOutputGSTOfProductsInStock: Double,

    // sold - invoice
    val totalNoOfSale: Int,
    val totalSaleAmountWithoutGST: Double,
    val totalSaleAmountWithGst: Double,

    // return
    val totalNoOfReturn: Int,
    val totalAmountOfReturnWithoutGST: Double,
    val totalAmountOfReturnWithGST: Double,

    // customer
    val totalNumberOfCustomer: Int,
    val totalNumberOfRepeatedCustomer: Int,

    // coupon
    val totalCouponDiscountGiven: Double,

    // expense
    val totalExpense: Double,
)
