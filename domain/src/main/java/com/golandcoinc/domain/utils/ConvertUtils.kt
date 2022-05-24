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
        return if (list.size % 2 == 0) {
            ((list[list.size / 2] + list[list.size / 2 - 1]) / 2)
        } else {
            list[list.size / 2]
        }
    }
}