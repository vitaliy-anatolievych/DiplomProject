package com.golandcoinc.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StatsJournal")
data class StatsJournalDBEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val medianSpeedForTravel: Double,
    val totalDistanceTraveled: Double,
    val fuelVolume: Double,
    val fuelConsumption: Double
)
