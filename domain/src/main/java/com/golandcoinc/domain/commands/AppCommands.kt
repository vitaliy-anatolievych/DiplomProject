package com.golandcoinc.domain.commands

import com.golandcoinc.domain.entities.StatsJournalModel


interface AppCommands {
    suspend fun startTrip()
    suspend fun stopTrip()
    suspend fun fillFuelTank(fuelTankVolume: Double)
    suspend fun listenSpeed(listener: ((Int) -> Unit))
    suspend fun setSpeed(speed: Int)
    suspend fun getRecommendedSpeed(): Int?
    suspend fun getStatsJournal(): StatsJournalModel
    suspend fun deleteStatsJournal()
    suspend fun isHaveNotesOnTripJournal()
}