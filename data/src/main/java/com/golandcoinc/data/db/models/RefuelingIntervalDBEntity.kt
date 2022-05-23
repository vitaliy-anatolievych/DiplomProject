package com.golandcoinc.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RefuelingJournal")
data class RefuelingIntervalDBEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val medianSpeedForTravel: Double,
    val totalDistanceTraveled: Double
)
