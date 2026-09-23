package com.skegworks.mobilepos.dashboard

interface TopCustomerUseCase {
    suspend operator fun invoke() : List<TopCustomer>
}