package com.golandcoinc.data.gps

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.golandcoinc.data.dto.TripDto
import com.golandcoinc.data.utils.DataUtils

object AppLocationManager {
    private var _locationListener: LocationListener? = null
    private val locationListener: LocationListener
    get() = _locationListener ?: throw NullPointerException("Сначала надо начать поездку, прежде чем её останавливать")

    var tripDataListener: ((TripDto) -> Unit)? = null

    @SuppressLint("MissingPermission")
    fun startGPSLocator(locationManager: LocationManager) {
        var tripData: TripDto

        _locationListener = GPSLocator().apply {

            myLocationListener = {
                    tripData = TripDto(
                        time = it.time,
                        speed = it.speed
                    )
                    tripDataListener?.invoke(tripData)

            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2L, 1F, this)
        }
    }

    fun stopGPSLocator(locationManager: LocationManager) {
        if (_locationListener != null) {
            locationManager.removeUpdates(locationListener)
        }
    }
}