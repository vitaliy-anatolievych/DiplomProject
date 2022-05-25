package com.golandcoinc.diplomjobapplication.utils

import android.content.Context
import android.os.PowerManager
import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON

class AppPowerManager(private val context: Context) {
    private var _wakeLock : PowerManager.WakeLock? = null
    private val wakeLock: PowerManager.WakeLock
        get() = _wakeLock ?: throw NullPointerException("Сначала дайте команду не спать")

    fun stayWake() {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

        _wakeLock = powerManager.newWakeLock(FLAG_KEEP_SCREEN_ON, "myapp:wakeLogTag")

        wakeLock.acquire()
    }

    fun releaseWake() {
        wakeLock.release()
    }
}