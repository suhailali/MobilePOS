package com.skegworks.mobilepos.utils.bitmap

import android.graphics.Bitmap

interface LoadBitmap {
    fun loadBitmap(filename: String): Bitmap?
}