package com.skegworks.mobilepos.coupon

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import com.skegworks.mobilepos.utils.bitmap.LoadBitmap
import com.skegworks.mobilepos.utils.files.FileHandler

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

        // Todo Pass in the QR Code
        val qrBitmap = generateCouponQRCode.generateQR(400, couponCode)


        val qrLeft = couponBitmap.width/2 + 120f
        val qrTop = couponBitmap.height - qrBitmap.height - 350f

        val canvas = Canvas(couponBitmap)
        canvas.drawBitmap(qrBitmap, qrLeft, qrTop, null)

        val namePaint = Paint().apply {
            color = Color.BLACK
            textSize = 64f
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        // Todo Pass in the Customer Details
        canvas.drawText(customerName, 1700f, 800f, namePaint)

        namePaint.textSize = 48f
        // Todo Pass in the Customer Details
        canvas.drawText(couponCode, 1725f, 1420f, namePaint)

        namePaint.textSize = 64f
        namePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))

        canvas.drawText(discount, 210f, 1350f, namePaint)

        canvas.drawText(validFrom, 550f, 1440f, namePaint)

        canvas.drawText(validTill, 550f, 1525f, namePaint)


        fileHandler.writeDocument(couponBitmap, "$customerName-$couponCode")

        return couponBitmap
    }
}
