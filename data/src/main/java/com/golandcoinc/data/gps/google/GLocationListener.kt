package com.golandcoinc.data.gps.google

import android.location.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

class GLocationListener: LocationCallback() {
    var onLocationChanges: ((Location) -> Unit)? = null

    override fun onLocationResult(locationResult: LocationResult) {
        onLocationChanges?.invoke(locationResult.lastLocation)
    }
}