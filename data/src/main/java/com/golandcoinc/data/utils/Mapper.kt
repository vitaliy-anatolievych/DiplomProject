package com.golandcoinc.data.utils

import com.golandcoinc.data.db.models.RefuelingIntervalDBEntity
import com.golandcoinc.domain.entities.data.RefuelingIntervalData

object Mapper {

    fun mapRefuelingDataToDBModel(data: RefuelingIntervalData): RefuelingIntervalDBEntity =
        RefuelingIntervalDBEntity(
            medianSpeedForTravel = data.medianSpeedForTravel,
            totalDistanceTraveled = data.totalDistanceTraveled
        )
}