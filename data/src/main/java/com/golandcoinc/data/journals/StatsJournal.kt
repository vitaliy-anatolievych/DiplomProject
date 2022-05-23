package com.golandcoinc.data.journals

import com.golandcoinc.data.db.models.FuelDBModel
import com.golandcoinc.domain.entities.StatsJournalModel

interface StatsJournal {
    suspend fun calculateData(fuel: FuelDBModel)
    suspend fun getStatsJournal(): StatsJournalModel
}