package com.golandcoinc.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_journal")
data class TripDataDBEntity(
    @PrimaryKey
    val time: Long,
    val speed: Double,
    val time_interval: Long?,
    val average_speed: Double? = 0.0,
    var distance: Double? = 0.0
)
