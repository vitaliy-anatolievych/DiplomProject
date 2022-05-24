package com.golandcoinc.domain.entities.data

data class TripData(
    val time: Long,
    val speed: Double,
    val time_interval: Long? = 0,
    val average_speed: Double? = 0.0,
    var distance: Double? = 0.0
)
