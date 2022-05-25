package com.golandcoinc.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class GForegroundService: Service() {


    override fun onCreate() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        super.onCreate()
    }

    // На главном потоке
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun createNotification() =
        NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Titile")
            .setContentText("Text")
            .build()

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    companion object {
        private const val CHANNEL_ID = "gservice_1"
        private const val CHANNEL_NAME = "gservice"
        private const val NOTIFICATION_ID = 10
        fun newIntent(context: Context) = Intent(context, GForegroundService::class.java)
    }
}