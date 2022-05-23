package com.golandcoinc.data.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import com.golandcoinc.data.dto.TripDto
import com.golandcoinc.data.gps.AppLocationManager
import com.golandcoinc.data.utils.DataUtils
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*

class GPSWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            AppLocationManager.tripDataListener = { tripDto ->
                gpsWorkerTripDtoListener?.invoke(tripDto)
            }
            Result.success()
        } catch (e: IOException) {
            Result.retry()
        }
    }


    companion object {
        const val WORKER_NAME = "gps_worker"

        var gpsWorkerTripDtoListener: ((TripDto) -> Unit)? = null

        fun makeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<GPSWorker>()
                .setConstraints(makeConstraints())
                .build()
        }

        private fun makeConstraints(): Constraints {
            return Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        }
    }
}