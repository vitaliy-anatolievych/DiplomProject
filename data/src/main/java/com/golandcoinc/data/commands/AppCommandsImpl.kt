package com.golandcoinc.data.commands

import com.golandcoinc.data.db.dao.AppDao
import com.golandcoinc.data.db.models.FuelDBModel
import com.golandcoinc.data.db.models.SpeedDBModel
import com.golandcoinc.data.journals.RefuelingIntervalJournal
import com.golandcoinc.data.journals.StatsJournal
import com.golandcoinc.data.journals.TripJournal
import com.golandcoinc.domain.commands.AppCommands
import com.golandcoinc.domain.entities.StatsJournalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppCommandsImpl(
    private val tripJournal: TripJournal,
    private val refuelingIntervalJournal: RefuelingIntervalJournal,
    private val statsJournal: StatsJournal,
    private val db: AppDao
) : AppCommands {

    override suspend fun startTrip() {
        withContext(Dispatchers.Main) {
            tripJournal.startTrip()
        }
    }

    override suspend fun stopTrip() {
        tripJournal.stopTrip()
        refuelingIntervalJournal.calculateTripData()
    }

    override suspend fun fillFuelTank(fuelTankVolume: Double) {
        db.fillFuelTank(FuelDBModel(fuelVolume = fuelTankVolume))
        val refuelingJournal = db.getRefuelingJournal()
        if (refuelingJournal.isNotEmpty()) {
            val fuel = db.getFuelVolume()
            statsJournal.calculateData(fuel!!)
            db.clearRefuelingJournal()
        }
    }

    override suspend fun listenSpeed(listener: ((Int) -> Unit)) {
        tripJournal.getSpeed(listener)
    }

    override suspend fun setSpeed(speed: Int) {
        db.setRecommendedSpeed(SpeedDBModel(speed = speed))
    }

    override suspend fun getRecommendedSpeed(): Int? = db.getRecommendedSpeed()

    override suspend fun getStatsJournal(): StatsJournalModel =
        statsJournal.getStatsJournal()

    override suspend fun deleteStatsJournal() {
        db.clearStatsJournal()
        db.clearRecommendedSpeed()
        db.clearFuelVolume()
    }

    override suspend fun isHaveNotesOnTripJournal() {
        val isHaveNotes = tripJournal.isHaveNotes()
        if (isHaveNotes) {
            refuelingIntervalJournal.calculateTripData()
            db.clearTripJournal()
        }
    }
}