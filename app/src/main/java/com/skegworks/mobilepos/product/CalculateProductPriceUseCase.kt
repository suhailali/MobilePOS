package com.skegworks.mobilepos.product

import com.skegworks.mobilepos.data.domain.PriceInput
import com.skegworks.mobilepos.data.domain.PriceOutput

interface CalculateProductPriceUseCase {
    operator fun invoke(input: PriceInput): PriceOutput
}