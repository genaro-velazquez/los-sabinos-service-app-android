package com.lossabinos.serviceapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.lossabinos.domain.usecases.ConnectLocationSocketUseCase
import com.lossabinos.domain.usecases.DisconnectLocationSocketUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WebSocketService : Service() {

    @Inject
    lateinit var connectLocationSocket: ConnectLocationSocketUseCase

    @Inject
    lateinit var disconnectLocationSocket: DisconnectLocationSocketUseCase

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        connectLocationSocket()
        // If the service is killed, it will be automatically restarted.
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectLocationSocket()
    }

    private fun createNotification(): Notification {
        val channelId = "websocket_service_channel"
        val channelName = "Location Tracking Service"

        // Create notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        // TODO: Replace with your app's icon
        // val notificationIcon = R.drawable.ic_notification
        val notificationIcon = android.R.drawable.ic_menu_mylocation

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Location Tracking Active")
            .setContentText("Your location is being shared in real-time.")
            .setSmallIcon(notificationIcon)
            .setOngoing(true) // Makes the notification non-dismissable
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    companion object {
        private const val NOTIFICATION_ID = 1
    }
}