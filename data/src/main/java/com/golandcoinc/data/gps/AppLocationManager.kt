package com.golandcoinc.data.gps

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import com.golandcoinc.data.dto.TripDto
import com.golandcoinc.data.gps.lib.KalmanLocationManager
import com.golandcoinc.data.utils.DataUtils

object AppLocationManager {
    private var _locationListener: LocationListener? = null
    private val locationListener: LocationListener
    get() = _locationListener ?: throw NullPointerException("Поездка не начата")

    var tripDataListener: ((TripDto) -> Unit)? = null

    @SuppressLint("MissingPermission")
    fun startGPSLocator(locationManager: KalmanLocationManager) {
        var tripData: TripDto

        _locationListener = GPSLocator().apply {

            myLocationListener = {
                    tripData = TripDto(
                        time = it.time,
                        speed = it.speed
                    )
                    tripDataListener?.invoke(tripData)

            }
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2L, 1F, this)
            locationManager.requestLocationUpdates(
                KalmanLocationManager.UseProvider.GPS,
                FILTER_TIME,
                GPS_TIME,
                NET_TIME,
                this,
                true
            )
        }
    }

    fun stopGPSLocator(locationManager: KalmanLocationManager) {
        if (_locationListener != null) {
            locationManager.removeUpdates(locationListener)
        }
    }

    /**
     * Request location updates with the highest possible frequency on gps.
     * Typically, this means one update per second for gps.
     */
    private const val GPS_TIME = 1000L;

    /**
     * For the network provider, which gives locations with less accuracy (less reliable),
     * request updates every 5 seconds.
     */
    private const val NET_TIME = 5000L;

    /**
     * For the filter-time argument we use a "real" value: the predictions are triggered by a timer.
     * Lets say we want 5 updates (estimates) per second = update each 200 millis.
     */
    private const val FILTER_TIME = 200L;
}