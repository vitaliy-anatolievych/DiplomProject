package com.golandcoinc.data.journals.impl

import android.content.Context
import com.golandcoinc.data.db.dao.AppDao
import com.golandcoinc.data.gps.google.GoogleLocationService
import com.golandcoinc.data.journals.TripJournal
import com.golandcoinc.data.utils.TripDataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TripJournalImpl(
    private val context: Context,
    private val db: AppDao,
) : TripJournal {

    private val tripDataManager = TripDataManager()
    private val googleLocation = GoogleLocationService.newIntent(context, tripDataManager)

    override suspend fun startTrip() {
        createNewJournal()
        context.startForegroundService(googleLocation)
    }

    override suspend fun stopTrip() {
        context.stopService(googleLocation)
    }

    override suspend fun getSpeed(listener: ((Int) -> Unit)) {
        TripDataManager.speedListener = listener
    }

    override suspend fun isHaveNotes(): Boolean {
        return db.getTripJournal().isNotEmpty()
    }

    private suspend fun createNewJournal() {
        withContext(Dispatchers.IO) {
            db.clearTripJournal()
        }
    }
}