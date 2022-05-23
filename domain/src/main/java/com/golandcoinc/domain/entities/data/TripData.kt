package com.golandcoinc.domain.entities.data

data class TripData(
    val time: Long,
    val speed: Double,
    val time_interval: String? = "Нет данных",
    val average_speed: String? = "Нет данных",
    var distance: String? = "Нет данных"
)
