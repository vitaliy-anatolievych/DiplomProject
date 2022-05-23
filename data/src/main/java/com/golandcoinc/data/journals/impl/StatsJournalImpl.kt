package com.golandcoinc.data.journals.impl

import android.content.Context
import android.widget.Toast
import com.golandcoinc.data.R
import com.golandcoinc.data.db.dao.AppDao
import com.golandcoinc.data.db.models.FuelDBModel
import com.golandcoinc.data.db.models.RefuelingIntervalDBEntity
import com.golandcoinc.data.db.models.SpeedDBModel
import com.golandcoinc.data.db.models.StatsJournalDBEntity
import com.golandcoinc.data.journals.StatsJournal
import com.golandcoinc.data.utils.DataUtils
import com.golandcoinc.domain.entities.StatsJournalModel
import com.golandcoinc.domain.entities.data.StatsData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatsJournalImpl(
    private val context: Context,
    private val db: AppDao
) : StatsJournal {

    override suspend fun calculateData(fuel: FuelDBModel) {
        val refuelingJournal = db.getRefuelingJournal()

        if (refuelingJournal.isNotEmpty()) {
            saveData(refuelingJournal, fuel)
        }
    }

    override suspend fun getStatsJournal(): StatsJournalModel {
        val dbEntity = db.getStatsJournal()
        val statsJournal = StatsJournalModel(listOf())

        if (dbEntity.isNotEmpty()) {
            val mapList = mutableListOf<StatsData>()
            dbEntity.map {
                mapList.add(
                    StatsData(
                        medianSpeedForTravel = it.medianSpeedForTravel,
                        totalDistanceTraveled = it.totalDistanceTraveled,
                        fuelVolume = it.fuelVolume,
                        fuelConsumption = it.fuelConsumption
                    )
                )
            }
            statsJournal.statsList = mapList
        }
        return statsJournal
    }

    private fun saveData(refuelingJournal: List<RefuelingIntervalDBEntity>, fuel: FuelDBModel) {
        val medianSpeed = calculateMedianSpeed(refuelingJournal)
        val totalDistanceTraveled = calculateTotalDistance(refuelingJournal)
        val fuelConsumption = calculateFuelConsumption(fuel.fuelVolume, totalDistanceTraveled)

        if (fuelConsumption > 1000.0) {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(
                    context.applicationContext,
                    context.getString(R.string.fuel_alert),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        db.saveStatsJournal(
            StatsJournalDBEntity(
                medianSpeedForTravel = medianSpeed,
                totalDistanceTraveled = totalDistanceTraveled,
                fuelVolume = fuel.fuelVolume,
                fuelConsumption = fuelConsumption
            )
        )

        calculateRecommendedSpeed()
    }

    private fun calculateRecommendedSpeed() {
        val dbEntity = db.getStatsJournal()

        if (dbEntity.size >= 2) {
            CoroutineScope(Dispatchers.IO).launch {
                var fuel = dbEntity[0].fuelConsumption
                var speed = dbEntity[0].medianSpeedForTravel
                dbEntity.map {
                    if (it.fuelConsumption < fuel) {
                        fuel = it.fuelConsumption
                        speed = it.medianSpeedForTravel
                    }
                }

                db.setRecommendedSpeed(speed = SpeedDBModel(speed = speed.toInt()))
            }
        }
    }

    private fun calculateFuelConsumption(fuel: Double, totalDistanceTraveled: Double): Double {
        return if (totalDistanceTraveled != 0.0) {
//            (fuel * totalDistanceTraveled) / 100
            (fuel * 100) / totalDistanceTraveled
        } else 0.0
    }

    private fun calculateMedianSpeed(refuelingJournal: List<RefuelingIntervalDBEntity>): Double {
        return DataUtils.calculateMedian(
            mutableListOf<Double>().apply {
                refuelingJournal.map {
                    add(it.medianSpeedForTravel)
                }
            }
        )
    }

    private fun calculateTotalDistance(refuelingJournal: List<RefuelingIntervalDBEntity>): Double {
        var totalDistance = 0.0
        refuelingJournal.map {
            totalDistance += it.totalDistanceTraveled
        }
        return totalDistance
    }
}