package com.golandcoinc.data.journals.impl

import com.golandcoinc.data.db.dao.AppDao
import com.golandcoinc.data.db.models.TripDataDBEntity
import com.golandcoinc.data.journals.RefuelingIntervalJournal
import com.golandcoinc.data.utils.DataUtils
import com.golandcoinc.data.utils.Mapper
import com.golandcoinc.domain.entities.data.RefuelingIntervalData
import kotlin.math.roundToInt

class RefuelingIntervalJournalImpl(
    private val db: AppDao
) : RefuelingIntervalJournal {

    override suspend fun calculateTripData() {
        val tripData = db.getTripJournal()

        if (tripData.isNotEmpty() && tripData.size >= 3) {
            saveJournal(tripData)
            db.clearTripJournal()
        }
    }

    private fun saveJournal(tripData: List<TripDataDBEntity>) {
        db.saveRefuelingJournal(
            Mapper.mapRefuelingDataToDBModel(
                RefuelingIntervalData(
                    medianSpeedForTravel = calculateMedianSpeed(tripData),
                    totalDistanceTraveled = calculateTotalDistance(tripData)
                )
            )
        )
    }

    private fun calculateMedianSpeed(tripData: List<TripDataDBEntity>): Double {
        val listOfAverageSpeed = mutableListOf<Double>()
        tripData.forEach {
            if (it.average_speed != null && it.average_speed != "Нет данных") {
                listOfAverageSpeed.add(it.average_speed.toDouble())
            }
        }

        listOfAverageSpeed.sortBy { it }

        return DataUtils.calculateMedian(
            mutableListOf<Double>().apply {
                listOfAverageSpeed.map {
                    this.add(it)
                }
            }
        )
    }

    private fun calculateTotalDistance(tripData: List<TripDataDBEntity>): Double {
        var resultCalculate = 0.0
        if (tripData.size > 1) {
            tripData.forEachIndexed { index, tripData ->
                if (index >= 1) {
                    resultCalculate += tripData.distance?.toDouble()!!
                }
            }
        }

        return ((resultCalculate * 100).roundToInt() / 100.0)
    }
}