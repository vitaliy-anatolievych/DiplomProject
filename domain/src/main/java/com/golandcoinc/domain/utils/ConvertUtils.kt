package com.golandcoinc.domain.utils

import java.text.SimpleDateFormat
import java.util.*

object ConvertUtils {
    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)

    fun timeToString(time: Long): String {
        return dateFormat.format(time)
    }

    fun timeToLong(timeString: String): Long {
        return dateFormat.parse(timeString).time
    }

    fun meterOnSecInKmPerHour(speed: Float): Double {
        return ((speed / 1000) * 3600).toDouble()
    }

    fun kmPerHourImMeterOnSec(speed: Double): Double {
        return (speed * 1000) / 3600
    }

    fun calculateMedian(list: List<Double>): Double {
        val sorted = list.sortedBy { it }
        return if (sorted.size % 2 == 0) {
            ((sorted[sorted.size / 2] + sorted[sorted.size / 2 - 1]) / 2)
        } else {
            sorted[sorted.size / 2]
        }
    }
}