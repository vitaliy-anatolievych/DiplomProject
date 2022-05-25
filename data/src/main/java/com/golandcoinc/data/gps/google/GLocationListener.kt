package com.golandcoinc.data.gps.google

import android.location.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

class GLocationListener: LocationCallback() {
    var myLocationListener: ((Location) -> Unit)? = null

    override fun onLocationResult(locationResult: LocationResult) {
        myLocationListener?.invoke(locationResult.lastLocation)
    }
}