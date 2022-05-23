package com.golandcoinc.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FuelVolume")
data class FuelDBModel(
    @PrimaryKey
    val id: Int = 0,
    val fuelVolume: Double
)
