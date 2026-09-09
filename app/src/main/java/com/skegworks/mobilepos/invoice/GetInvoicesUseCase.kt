package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.Invoice

interface GetInvoicesUseCase {
    suspend operator fun invoke(limit: Int = 30, offset: Int = 0, searchValue: String): List<Invoice>
}
