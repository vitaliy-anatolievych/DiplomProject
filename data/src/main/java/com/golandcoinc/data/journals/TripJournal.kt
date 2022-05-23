package com.golandcoinc.data.journals

interface TripJournal {

    suspend fun startTrip()
    suspend fun stopTrip()
    suspend fun getSpeed(listener: ((Int) -> Unit))
    suspend fun isHaveNotes(): Boolean
}