package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.Invoice

interface GetInvoicesUseCase {
    suspend operator fun invoke(): List<Invoice>
}