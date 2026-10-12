package com.skegworks.mobilepos.invoice

import com.skegworks.mobilepos.data.domain.Invoice

class GenerateBLEInvoiceContentUseCaseImpl : GenerateBLEInvoiceContentUseCase {
    override suspend fun invoke(
        invoice: Invoice,
        infoForCustomer: Array<String>
    ): String {

        val hasGST = invoice.business.gstNumber.isNotEmpty()

//        // 80mm thermal printer
//        val width = 48

        // 58mm thermal printer
        val width = 32

        val out = StringBuilder()

        fun line(char: Char = '-') {
            out.append(char.toString().repeat(width))
            out.append('\n')
        }

        fun center(text: String) {
            val value = if (text.length > width) {
                text.take(width)
            } else {
                text
            }

            val leftPadding = (width - value.length) / 2

            out.append(" ".repeat(leftPadding))
            out.append(value)
            out.append('\n')
        }

        fun leftRight(
            left: String,
            right: String
        ) {
            val spaces = width - left.length - right.length

            if (spaces > 0) {
                out.append(left)
                out.append(" ".repeat(spaces))
                out.append(right)
            } else {
                out.append(left.take(width - right.length))
                out.append(right)
            }

            out.append('\n')
        }

        fun wrapText(
            text: String,
            maxWidth: Int
        ): List<String> {

            if (text.length <= maxWidth) {
                return listOf(text)
            }

            val result = mutableListOf<String>()

            var remaining = text.trim()

            while (remaining.length > maxWidth) {

                var breakAt =
                    remaining.lastIndexOf(
                        ' ',
                        maxWidth
                    )

                if (breakAt <= 0) {
                    breakAt = maxWidth
                }

                result.add(
                    remaining.substring(
                        0,
                        breakAt
                    ).trim()
                )

                remaining =
                    remaining.substring(breakAt).trim()
            }

            if (remaining.isNotEmpty()) {
                result.add(remaining)
            }

            return result
        }

        // ------------------------------------------------
        // HEADER
        // ------------------------------------------------

        center("INVOICE")
        out.append('\n')

        center(invoice.business.name)

        wrapText(
            invoice.business.address,
            width
        ).forEach {
            center(it)
        }

        center("Contact: ${invoice.business.mobile}")

        if (invoice.business.email.isNotEmpty()) {
            center(invoice.business.email)
        }

        if (hasGST) {
            center("GSTIN: ${invoice.business.gstNumber}")
        }

        line()

        // ------------------------------------------------
        // INVOICE DETAILS
        // ------------------------------------------------

        leftRight(
            "Invoice No:",
            invoice.invoiceNumber
        )

        leftRight(
            "Date:",
            invoice.invoiceDate
        )

        leftRight(
            "Customer:",
            invoice.customer.name
        )

        if (invoice.customer.phone.isNotEmpty()) {
            leftRight(
                "Phone:",
                invoice.customer.phone
            )
        }

        line()

        // ------------------------------------------------
// ITEMS
// ------------------------------------------------

        /*
         * 58mm thermal printer
         * 32 columns
         *
         * No Item                 Qty Amount
         * 1   Cotton Kurti         2 1798.00
         *
         * Column width:
         * No     = 2
         * Space  = 1
         * Item   = 17
         * Space  = 1
         * Qty    = 3
         * Space  = 1
         * Amount = 7
         *
         * Total = 2 + 1 + 17 + 1 + 3 + 1 + 7 = 32
         */

        val itemWidth = 17

        val rowFormat = "%-2s %-${itemWidth}s %3s %7s"

// ------------------------------------------------
// HEADER
// ------------------------------------------------

        val header = rowFormat.format(
            "No",
            "Item",
            "Qty",
            "Amount"
        )

        out.append(header)
        out.append('\n')

        line()

// ------------------------------------------------
// ITEMS
// ------------------------------------------------

        var index = 1

        for (item in invoice.items) {

            val amount =
                item.quantity *
                        item.finalRoundedOffPrice

            val itemName =
                item.title

            // Wrap item name to fit the 17-character column
            val itemLines =
                wrapText(
                    itemName,
                    itemWidth
                )

            // ------------------------------------------------
            // FIRST LINE
            // ------------------------------------------------

            val firstLine =
                rowFormat.format(
                    index.toString(),
                    itemLines.first(),
                    item.quantity.toString(),
                    formatAmount(amount)
                )

            out.append(
                firstLine.take(width)
            )

            out.append('\n')

            // ------------------------------------------------
            // ADDITIONAL ITEM NAME LINES
            // ------------------------------------------------

            for (i in 1 until itemLines.size) {

                out.append(
                    "    ${itemLines[i]}"
                        .take(width)
                )

                out.append('\n')
            }

            // ------------------------------------------------
            // PRICE / DISCOUNT
            // ------------------------------------------------

            val mrpText =
                "    MRP: ${formatAmount(item.salePriceWithoutDiscount)}"

            val discountText =
                " Disc: ${formatAmount(item.discountPercentage)}%"

            out.append(
                (mrpText + discountText)
                    .take(width)
            )

            out.append('\n')

            // ------------------------------------------------
            // GST
            // ------------------------------------------------

            if (hasGST) {

                val hsnText =
                    "    HSN: ${item.hsnCode}"

                val gstText =
                    " GST: ${formatAmount(item.outputGstPercentage)}%"

                out.append(
                    (hsnText + gstText)
                        .take(width)
                )

                out.append('\n')
            }

            // ------------------------------------------------
            // NEXT ITEM
            // ------------------------------------------------

            index++
        }
        line()

        // ------------------------------------------------
        // TOTALS
        // ------------------------------------------------

        leftRight(
            "Total:",
            formatAmount(invoice.totalPrice)
        )

        if (invoice.totalDiscount > 0) {

            leftRight(
                "Discount:",
                formatAmount(invoice.totalDiscount)
            )
        }

        if (invoice.couponDiscount > 0) {

            leftRight(
                "Coupon Discount:",
                formatAmount(invoice.couponDiscount)
            )
        }

        if (invoice.cashDiscount > 0) {

            leftRight(
                "Cash Discount:",
                formatAmount(invoice.cashDiscount)
            )
        }

        line()

        // ------------------------------------------------
        // FINAL PRICE
        // ------------------------------------------------

        leftRight(
            "FINAL PRICE:",
            "Rs. ${formatAmount(invoice.finalPrice)}"
        )

        line()

//        // ------------------------------------------------
//        // CUSTOMER INFORMATION
//        // ------------------------------------------------
//
//        if (infoForCustomer.isNotEmpty()) {
//
//            center("")
//
//            infoForCustomer.forEach { info ->
//
//                wrapText(
//                    info,
//                    width
//                ).forEach {
//                    center(it)
//                }
//            }
//        }

        center("")
        center("Thank You!")
        center("Visit Again")

        out.append("\n\n\n")

        return out.toString()
    }
}