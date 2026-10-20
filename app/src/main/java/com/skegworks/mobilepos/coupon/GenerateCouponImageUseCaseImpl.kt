package com.skegworks.mobilepos.coupon

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import com.skegworks.mobilepos.utils.bitmap.LoadBitmap
import com.skegworks.mobilepos.utils.files.FileHandler

// Salwariya Template
class GenerateCouponImageUseCaseImpl(
    private val loadBitmap: LoadBitmap,
    private val generateCouponQRCode: GenerateCouponQRCode,
    private val fileHandler: FileHandler<Bitmap>
) : GenerateCouponImageUseCase {
    override suspend fun invoke(
        inputPath: String,
        couponCode: String,
        customerName: String,
        discount: String,
        validFrom: String,
        validTill: String,
    ): Bitmap? {
        val loadedBitmap = loadBitmap.loadBitmap(inputPath) ?: return null
        // Create a mutable copy of the bitmap to allow drawing on it
        val couponBitmap = loadedBitmap.copy(Bitmap.Config.ARGB_8888, true)

        val width = loadedBitmap.width
        val height = loadedBitmap.height

        // Todo Pass in the QR Code
        val qrBitmap = generateCouponQRCode.generateQR(220, couponCode)


        val qrLeft = (width/2).toFloat()
        val qrTop = (height/2).toFloat()

        val canvas = Canvas(couponBitmap)
        canvas.drawBitmap(qrBitmap, qrLeft- qrBitmap.width/2, qrTop - qrBitmap.height/2, null)

        val namePaint = Paint().apply {
            color = Color.BLACK
            textSize = 64f
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        // Todo Pass in the Customer Details
        val nameTextLength = namePaint.measureText(customerName)
        canvas.drawText(customerName, (width - nameTextLength)/2, 450f, namePaint)

        namePaint.textSize = 48f
        // Todo Pass in the Customer Details
        val couponCodeWidth = namePaint.measureText(couponCode)
        canvas.drawText(couponCode, (width - couponCodeWidth)/2, 900f, namePaint)

        namePaint.textSize = 64f
        namePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))

        // canvas.drawText(discount, width/2.toFloat() - discount.length * 16 - 30, 1050f, namePaint)

        namePaint.textSize = 36f

        val validFromText = "valid from $validFrom"
        val fromTextWidth = width - namePaint.measureText(validFromText)
        canvas.drawText(validFromText, fromTextWidth/2, 1340f, namePaint)

        val validTillText = "valid till $validTill"
        val tillTextWidth = width - namePaint.measureText(validTillText)
        canvas.drawText(validTillText, tillTextWidth/2, 1400f, namePaint)


        fileHandler.writeDocument(couponBitmap, "$customerName-$couponCode")

        return couponBitmap
    }
}
