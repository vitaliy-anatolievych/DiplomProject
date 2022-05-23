package com.golandcoinc.data.gps

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log

class GPSLocator: LocationListener {
    var myLocationListener: ((Location) -> Unit)? = null

    override fun onLocationChanged(location: Location) {
        myLocationListener?.invoke(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.e("onStatusChanged", "onStatusChanged")
    }

    override fun onProviderEnabled(provider: String) {
        Log.e("onProviderEnabled", "onProviderEnabled")
    }

    override fun onProviderDisabled(provider: String) {
        Log.e("onProviderDisabled", "onProviderDisabled")
    }

    override fun onLocationChanged(locations: MutableList<Location>) {
        Log.e("onProviderDisabled", "onLocationChanged")
        super.onLocationChanged(locations)
    }

    override fun onFlushComplete(requestCode: Int) {
        Log.e("onProviderEnabled", "onFlushComplete")
        super.onFlushComplete(requestCode)
    }
}