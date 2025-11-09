package com.skegworks.mobilepos.product

import com.skegworks.mobilepos.data.domain.PriceInput
import com.skegworks.mobilepos.data.domain.PriceOutput

class CalculateProductPriceUseCaseImpl : CalculateProductPriceUseCase {
    override fun invoke(input: PriceInput): PriceOutput {
        // calculate input gst amount
        val inputGst = (input.itemPrice * input.inputGstPercentage) / 100
        // item price is price with out gst and it is the cost
        val cost = input.itemPrice
        // adds sale margin percentage to the cost - output gst should be calculated after adding margin
        val salePriceBeforeGst = cost + (cost * input.saleMargin / 100)

        //once margin added we need to deduct the discount before calculation output gst
        val priceAfterDiscount =
            salePriceBeforeGst - (salePriceBeforeGst * input.discountPercentage / 100)
        val discountAmount = salePriceBeforeGst - priceAfterDiscount

        // if discount not applied what would be the price. This is for billing purpose and display tag
        // this includes output gst as well
        val priceWithoutDiscount =
            salePriceBeforeGst + (salePriceBeforeGst * input.outputGstPercentage) / 100

        // output gst should be calculated on cost + margin - discount(if any)
        val outputGst = (priceAfterDiscount * input.outputGstPercentage) / 100
        // sale price is final price with decimals
        val salePrice = priceAfterDiscount + outputGst
        // round off sale price to avoid decimals to display on tag
        val finalRoundedOffPrice = salePrice.toInt()

        return PriceOutput(
            inputGst = inputGst,
            cost = cost,
            salePriceBeforeGst = salePriceBeforeGst,
            outputGst = outputGst,
            priceAfterDiscountWithoutGst = priceAfterDiscount,
            discountAmount = discountAmount,
            salePrice = salePrice,
            priceWithoutDiscount = priceWithoutDiscount.toInt(),
            finalRoundedOffPrice = finalRoundedOffPrice
        )
    }
}