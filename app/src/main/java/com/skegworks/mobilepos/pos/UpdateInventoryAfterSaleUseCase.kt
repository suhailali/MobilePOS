package com.skegworks.mobilepos.pos

import com.skegworks.mobilepos.data.domain.Invoice

interface UpdateInventoryAfterSaleUseCase {
    suspend operator fun invoke(invoice: Invoice)
}