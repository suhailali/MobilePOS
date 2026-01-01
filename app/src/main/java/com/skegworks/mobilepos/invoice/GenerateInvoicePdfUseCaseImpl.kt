package com.skegworks.mobilepos.invoice

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.skegworks.mobilepos.data.domain.Invoice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GenerateInvoicePdfUseCaseImpl : GenerateInvoicePdfUseCase {
    override suspend fun generatePdf(invoice: Invoice): PdfDocument =
        withContext(Dispatchers.Default) {
            // Create a PdfDocument and draw the invoice
            val mmToPt = 72.0 / 25.4 // 1 mm = 72/25.4 points
            val pageWidthPt = (148 * mmToPt).toInt() // A5 width in points
            val pageHeightPt = (210 * mmToPt).toInt() // A5 height in points

            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidthPt, pageHeightPt, 1).create()
            val page = document.startPage(pageInfo)
            val canvas: Canvas = page.canvas

// Painting setup
            val paint = Paint().apply {
                isAntiAlias = true
                textSize = 14f
            }
            val bold = Paint(paint).apply { textSize = 9f; isUnderlineText = true; isFakeBoldText = true }
            val small = Paint(paint).apply { textSize = 7f }
            val medium = Paint(paint).apply { textSize = 8f; isFakeBoldText =  true }

            var y = 30f

// Title centered
            val title = "INVOICE"
            val titleWidth = bold.measureText(title)
            canvas.drawText(title, (pageWidthPt - titleWidth) / 2f, y, bold)
            y += 20f

            val rightIndex = pageWidthPt - 100f

// Store details
            canvas.drawText(invoice.business.name, 10f, y, paint)
            canvas.drawText("Invoice No.: " + invoice.invoiceNumber, rightIndex - 20, y, bold)
            y += 14f
            drawMultilineText(canvas, invoice.business.address, 10f, y, small, pageWidthPt - 20)
            canvas.drawText("Date: " + invoice.invoiceDate, rightIndex, y, small)
            y += 30f
            canvas.drawText("Contact: ${invoice.business.mobile}", 10f, y, small)
            canvas.drawText("Customer: ${invoice.customer.name}", rightIndex, y, small)
            y += 12f
            canvas.drawText("GSTIN: ${invoice.business.gstNumber}", 10f, y, small)
            canvas.drawText("Phone: ${invoice.customer.phone}", rightIndex, y, small)
            y += 12f
            canvas.drawText("Email: ${invoice.business.email}", 10f, y, small)
            y += 18f

// Customer


// Table heading
            val startX = 10f
            val colSnoW = 20f
            val colItemW = 120f
            val colHsnW = 50f
            val colRateW = 50f
            val colQtyW = 30f
            val colDiscW = 40f
            val colGstW = 40f

            val totalW = 10 + 4 + 30 + 4 + 140 + 4 + 60 + 4 + 50 + 4 + 40 + 4 + 40 + 4 + 40 + 4 + 10

// Draw header background line
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 0.5f

            val headerY = y
            canvas.drawText("S.No", startX + 4, headerY + 12, small)
            canvas.drawText("Item", startX + colSnoW + 4, headerY + 12, small)
            canvas.drawText("HSN", startX + colSnoW + colItemW + 4, headerY + 12, small)
            canvas.drawText("MRP", startX + colSnoW + colItemW + colHsnW + 4, headerY + 12, small)
            canvas.drawText("Disc.%", startX + colSnoW + colItemW + colHsnW + colRateW + colQtyW + 4, headerY + 12, small)
            canvas.drawText("Tax%", startX + colSnoW + colItemW + colHsnW + colRateW + colQtyW + colDiscW + 4, headerY + 12, small)
            canvas.drawText("Qty", startX + colSnoW + colItemW + colHsnW + colRateW + 4, headerY + 12, small)
            canvas.drawText("Amount", startX + colSnoW + colItemW + colHsnW + colRateW + colQtyW + colDiscW + colGstW + 4, headerY + 12, small)

            y += 20f

// Items
            // Items
            var index = 1
            for (item in invoice.items) {
                val rowY = y
                canvas.drawText(index.toString(), startX + 4, rowY + 12, small)
// Wrap item name if too long
                val itemName = item.title
//                canvas.drawText(itemName + "afcgh uuytff jjuy", startX + colSnoW + 4, rowY + 12, small)
                drawMultilineText(canvas, itemName, startX + colSnoW + 4, rowY + 12, small, pageWidthPt/4  )
                canvas.drawText(item.hsnCode, startX + colSnoW + colItemW + 4, rowY + 12, small)
                canvas.drawText(formatAmount(item.salePriceWithoutDiscount), startX + colSnoW + colItemW + colHsnW + 4, rowY + 12, small)
                canvas.drawText(formatAmount(item.discountPercentage), startX + colSnoW + colItemW + colHsnW + colRateW + colQtyW +4, rowY + 12, small)
                canvas.drawText(formatAmount(item.outputGstPercentage), startX + colSnoW + colItemW + colHsnW + colRateW + colQtyW + colDiscW +4, rowY + 12, small)
                canvas.drawText(item.quantity.toString(), startX + colSnoW + colItemW + colHsnW + colRateW + 4, rowY + 12, small)
                canvas.drawText(formatAmount(item.quantity * item.finalRoundedOffPrice), startX + colSnoW + colItemW + colHsnW + colRateW + colQtyW + colDiscW + colGstW + 4, rowY + 12, small)

                y += 18f
                index++

// page break not handled in this simple example; ensure items fit into a page
            }

            y += 10f

// Totals
            val total = invoice.totalPrice
            val totalDiscount = invoice.totalDiscount
            val cashDiscount = invoice.cashDiscount
            val finalPrice = invoice.finalPrice

            val rightX = pageWidthPt - 10f
            val labelX = rightX - 140f

            canvas.drawText("Total:", labelX, y + 12f, small)
            canvas.drawText(formatAmount(total), rightX - 50f, y + 12f, small)
            if (totalDiscount > 0) {
                y += 14f
                canvas.drawText("Discount:", labelX, y + 12f, small)
                canvas.drawText(formatAmount(totalDiscount), rightX - 50f, y + 12f, small)
            }
            if(cashDiscount > 0) {
                y += 14f
                canvas.drawText("Cash Discount:", labelX, y + 12f, small)
                canvas.drawText(formatAmount(cashDiscount), rightX - 50f, y + 12f, medium)
            }
            y += 30f
            canvas.drawText("Final Price:", labelX, y + 12f, bold)
            canvas.drawText("Rs. " + formatAmount(finalPrice), rightX - 50f, y + 12f, bold)

            // TODO Change this to input param
            y += 130f
            canvas.drawText(" - Thank you for shopping with us!", 10f, y, small)
            y += 14f
            canvas.drawText(" - വിറ്റ സാധനങ്ങൾ 5 ദിവസത്തിനകം മാറ്റിയെടുക്കാവുന്നതാണ്, പണം തിരികെ നൽകുന്നതല്ല.", 10f, y, medium)
            y += 14f
            canvas.drawText(" - സാധനം കേടുപാട് കൂടാതെയും ഒറിജിനൽ ടാഗുമായും കൊണ്ടുവരണം.", 10f, y, medium)
            y += 14f
            canvas.drawText(" - വസ്ത്രങ്ങൾ ആദ്യപ്രാവശ്യം ഡ്രൈ വാഷ് ചെയുക.", 10f, y, medium)
            y += 14f
            canvas.drawText(" - കോട്ടൺ വസ്ത്രങ്ങൾ ചെറിയ രീതിയിൽ ചുരുങ്ങാനും കളർ മാങ്ങാനും സാധ്യതയുണ്ട്.", 10f, y, medium)
            y += 14f
            canvas.drawText(" - ഫാൻസി ഐറ്റംസ്, ചപ്പൽസ്, ഡിസ്കൗണ്ട് ഐറ്റംസ് തിരിച്ചെടുക്കുന്നതല്ല.", 10f, y, medium)


            document.finishPage(page)

            document

// Convert PdfDocument to ByteArray
//            val outStream = java.io.ByteArrayOutputStream()
//            try {
//                document.writeTo(outStream)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            } finally {
//                document.close()
//            }
//
//            outStream.toByteArray()
        }
}
    // helpers
    fun drawMultilineText(canvas: Canvas, text: String, x: Float, startY: Float, paint: Paint, maxWidth: Int) {
        val words = text.split(" ")
        var line = ""
        var y = startY
        for (w in words) {
            val test = if (line.isEmpty()) w else "$line $w"
            if (paint.measureText(test) > maxWidth) {
                canvas.drawText(line, x, y, paint)
                line = w
                y += paint.textSize + 4
            } else {
                line = test
            }
        }
        if (line.isNotEmpty()) canvas.drawText(line, x, y, paint)
    }

    fun formatAmount(value: Double): String = String.format("%.2f", value)
    fun formatAmount(value: Number): String = String.format("%.2f", value.toDouble())
