package com.golandcoinc.data.gps.google

import android.annotation.SuppressLint
import android.content.Context
import com.golandcoinc.data.dto.TripDto
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import java.lang.NullPointerException

object GoogleLocation {
    private var _locationListener: LocationCallback? = null
    private val locationListener: LocationCallback
        get() = _locationListener ?: throw NullPointerException("Поездка не начата")

    var tripDataListener: ((TripDto) -> Unit)? = null

    fun startLocationUpdates(context: Context) {
        _locationListener = GLocationListener().apply {
            myLocationListener = {
                tripDataListener?.invoke(
                    TripDto(
                        time = it.time,
                        speed = it.speed
                    )
                )
            }
        }
        locationSettingsRequest(context)
    }

    @SuppressLint("MissingPermission")
    private fun locationSettingsRequest(context: Context) {

        val locationRequest =  LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 2000
        }


        val locationSettings = LocationSettingsRequest.Builder().apply {
            addLocationRequest(locationRequest)
        }.build()

        val settingsClient = LocationServices.getSettingsClient(context)
        settingsClient.checkLocationSettings(locationSettings)

        LocationServices.getFusedLocationProviderClient(context)
            .requestLocationUpdates(locationRequest, locationListener, context.mainLooper)
    }

    fun stopLocationUpdates(context: Context) {
        LocationServices.getFusedLocationProviderClient(context)
            .removeLocationUpdates(locationListener)
    }
}


