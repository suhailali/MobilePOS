package com.skegworks.mobilepos.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.skegworks.mobilepos.R
import com.skegworks.mobilepos.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var fcmTokenRepository: FcmTokenRepository

    companion object {
        const val CHANNEL_ID = "Purchase"
    }

    @SuppressLint("HardwareIds")
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val mId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val deviceName = Build.MODEL

        // Save the refreshed token to Firestore.
        // This runs on a background thread.
        saveToken(token, mId, deviceName)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: "Document updated"
        val body = message.notification?.body ?: "A document has changed."

        showNotification(
            title = title,
            body = body,
            data = message.data
        )
    }

    private fun showNotification(
        title: String,
        body: String,
        data: Map<String, String>
    ) {
        createNotificationChannel()

        val intent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP

            putExtra("documentId", data["documentId"])
            putExtra("collection", data["collection"])
            putExtra("document", data["document"])
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            data["documentId"]?.hashCode() ?: 0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        try {
            NotificationManagerCompat.from(this).notify(
                System.currentTimeMillis().toInt(),
                notification
            )
        } catch (e: SecurityException) {
            // Notifications may be disabled by the user.
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Firestore updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications when Firestore documents change"
            }

            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    private fun saveToken(token: String, deviceId: String, deviceName: String) {
        fcmTokenRepository.saveToken(token, deviceId, deviceName)
    }

    private fun registerTokenInFirestore(token: String) {
        // Implemented in the next section.
    }
}