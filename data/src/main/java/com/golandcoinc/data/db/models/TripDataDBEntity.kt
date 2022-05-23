package com.golandcoinc.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_journal")
data class TripDataDBEntity(
    @PrimaryKey
    val time: String,
    val speed: Double,
    val time_interval: String? = "Нет данных",
    val average_speed: String? = "Нет данных",
    var distance: String? = "Нет данных"
)
