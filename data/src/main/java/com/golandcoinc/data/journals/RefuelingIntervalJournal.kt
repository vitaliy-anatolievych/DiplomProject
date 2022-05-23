package com.golandcoinc.data.journals

interface RefuelingIntervalJournal {
    suspend fun calculateTripData()
}