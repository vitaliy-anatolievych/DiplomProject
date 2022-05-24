package com.golandcoinc.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SpeedValue")
data class SpeedDBModel(
    @PrimaryKey
    val id: Int = 0,
    val speed: Float
)
