package com.golandcoinc.data.workers

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import java.lang.NullPointerException

object AppWorkerManager {
    private var _workManager: WorkManager? = null
    private val workManager: WorkManager
    get() = _workManager ?: throw NullPointerException("Вызван метод до того как был создан WorkManager")

    fun createWorkManager(context: Context) {
        _workManager = WorkManager.getInstance(context)
        workManager.enqueueUniqueWork(
            GPSWorker.WORKER_NAME,
            ExistingWorkPolicy.REPLACE,
            GPSWorker.makeRequest()
        )
    }

    fun stopAllWork() {
        workManager.cancelAllWork()
    }
}