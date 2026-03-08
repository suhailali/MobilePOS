package com.skegworks.mobilepos.di

import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import com.skegworks.mobilepos.utils.files.CouponFileHandler
import com.skegworks.mobilepos.utils.files.FileHandler
import com.skegworks.mobilepos.utils.files.PDFFileHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FileHandlerModule {

    @Binds
    abstract fun bindPdfFileHandler(
        impl: PDFFileHandler
    ): FileHandler<PdfDocument>

    @Binds
    abstract fun bindPngFileHandler(
        impl: CouponFileHandler
    ): FileHandler<Bitmap>
}