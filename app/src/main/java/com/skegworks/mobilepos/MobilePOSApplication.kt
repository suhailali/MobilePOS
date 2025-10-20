package com.skegworks.mobilepos

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MobilePOSApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
//        Toast.makeText(this, "Application", Toast.LENGTH_SHORT).show()
    }
}