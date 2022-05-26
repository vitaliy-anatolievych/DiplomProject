package com.golandcoinc.data.gps.google

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.golandcoinc.data.db.AppDataBase
import com.golandcoinc.data.dto.TripDto
import com.golandcoinc.data.utils.TripDataManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class GoogleLocationService : Service() {
    private var _locationListener: LocationCallback? = null
    private val locationListener: LocationCallback
        get() = _locationListener ?: throw NullPointerException("Поездка не начата")

    private var tripDataManager: TripDataManager? = null
    private val db by lazy { AppDataBase.getInstance(applicationContext) }
    private val dao by lazy { db.getDao() }

    private fun startLocationUpdates(context: Context) {
        isProgramStillWorking = true

        _locationListener = GLocationListener().apply {
            onLocationChanges = {
                tripDataManager?.addTripData(
                    tripDto = TripDto(
                        time = it.time,
                        speed = it.speed
                    ),
                    db = dao
                )
            }
        }
        locationSettingsRequest(context)
    }

    @SuppressLint("MissingPermission")
    private fun locationSettingsRequest(context: Context) {

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 2000
        }

        LocationServices.getFusedLocationProviderClient(context).apply {
            requestLocationUpdates(locationRequest, locationListener, context.mainLooper)
        }

    }

    private fun stopLocationUpdates(context: Context) {
        LocationServices.getFusedLocationProviderClient(context)
            .removeLocationUpdates(locationListener)
        isProgramStillWorking = false
    }

    private fun getDataManager(intent: Intent?) {
        tripDataManager = intent?.getParcelableExtra(DATA_MANAGER)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        startLocationUpdates(context = applicationContext)
        super.onCreate()
    }

    // На главном потоке
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getDataManager(intent)
        return START_NOT_STICKY
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
            .setContentTitle("GPSFuelEconomy")
            .setContentText("Still Working")
            .build()

    override fun onDestroy() {
        stopLocationUpdates(context = applicationContext)
        super.onDestroy()
    }

    companion object {
        private const val CHANNEL_ID = "gservice_1"
        private const val CHANNEL_NAME = "gservice"
        private const val DATA_MANAGER = "data_manger"
        private const val NOTIFICATION_ID = 10
        var isProgramStillWorking = false

        fun newIntent(context: Context, tripManager: TripDataManager) =
            Intent(context, GoogleLocationService::class.java).apply {
                putExtra(DATA_MANAGER, tripManager)
            }
    }
}