package com.skegworks.mobilepos.utils.bitmap

import android.graphics.Bitmap

interface BitmapEditor {
    fun editBitmap(toWidth: Int, toHeight: Int, bitmap: Bitmap): Bitmap
}