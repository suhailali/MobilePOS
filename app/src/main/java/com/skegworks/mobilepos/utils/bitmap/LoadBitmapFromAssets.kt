package com.skegworks.mobilepos.utils.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import okio.IOException

class LoadBitmapFromAssets(private val context: Context) : LoadBitmap {
    override fun loadBitmap(filename: String): Bitmap? {
        val assetManager = context.assets
        try {
            val inputStream = assetManager.open(filename)
            return BitmapFactory.decodeStream(inputStream)
        } catch (exception: IOException) {
            Log.d("File Not Found", exception.message ?: "")
            return null
        }
    }
}