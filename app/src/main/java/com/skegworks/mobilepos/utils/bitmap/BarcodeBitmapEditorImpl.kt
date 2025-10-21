package com.skegworks.mobilepos.utils.bitmap

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.graphics.createBitmap

class BarcodeBitmapEditorImpl(
    private val title: String,
    private val sku: String,
    private val price: String
) : BitmapEditor {
    override fun editBitmap(
        toWidth: Int,
        toHeight: Int,
        bitmap: Bitmap
    ): Bitmap {



        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            this.textSize = textSize
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        // 🔹 Dynamically scale text size based on bitmap width
        val maxTextWidth = bitmap.width * 0.9f // leave 10% margin
        var textSize =36f  // start big
        paint.textSize = textSize

        // shrink text if it exceeds the width
        while (paint.measureText(title)
                .coerceAtLeast(paint.measureText(price)) > maxTextWidth && textSize > 12f
        ) {
            textSize -= 2f
            paint.textSize = textSize
        }

        // Measure text height for spacing
        val padding = 40f
        val fm = paint.fontMetrics
        val textHeight = fm.bottom - fm.top + padding

        // New bitmap height = original + text above + text below
        val newHeight = (bitmap.height + (textHeight * 2)).toInt()

        val newBitmap = createBitmap(bitmap.width, newHeight)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)

        // Draw top text
        canvas.drawText(
            title,
            (bitmap.width/5).toFloat(),
            textHeight - fm.bottom,
            paint
        )

        // Draw top text
        canvas.drawText(
            "Rs. $price/-",
            (bitmap.width - bitmap.width/5).toFloat(),
            textHeight - fm.bottom,
            paint
        )

        // Draw original bitmap in the center
        canvas.drawBitmap(bitmap, 0f, textHeight + 10, null)

        // Draw bottom text
        canvas.drawText(
            sku,
            (bitmap.width / 2).toFloat(),
            (textHeight + bitmap.height + textHeight / 2) - fm.top / 2,
            paint
        )

        return newBitmap
    }
}