package com.golandcoinc.data.journals.impl

import android.content.Context
import android.util.Log
import com.golandcoinc.data.db.dao.AppDao
import com.golandcoinc.data.db.models.TripDataDBEntity
import com.golandcoinc.data.dto.TripDto
import com.golandcoinc.data.gps.AppLocationManager
import com.golandcoinc.data.gps.lib.KalmanLocationManager
import com.golandcoinc.data.journals.TripJournal
import com.golandcoinc.domain.utils.ConvertUtils
import com.golandcoinc.data.workers.AppWorkerManager
import com.golandcoinc.data.workers.GPSWorker
import com.golandcoinc.domain.entities.data.TripData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TripJournalImpl(
    private val context: Context,
    private val db: AppDao,
) : TripJournal {

    private var _currentTripList: List<TripDataDBEntity>? = null
    private val currentTripList: List<TripDataDBEntity>
        get() = _currentTripList ?: throw NullPointerException("Поездка не была начата")

//    private val locationManager =
//        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val locationManager = KalmanLocationManager(context)

    var speedListener: ((Int) -> Unit)? = null

    override suspend fun startTrip() {
        createNewJournal()

        AppLocationManager.startGPSLocator(locationManager)
        AppWorkerManager.createWorkManager(context)

        GPSWorker.gpsWorkerTripDtoListener = { tripDto ->
//            Log.e("TripDto", "$tripDto")
            addTripData(tripDto)
        }
    }

    override suspend fun stopTrip() {
        AppLocationManager.stopGPSLocator(locationManager)
        AppWorkerManager.stopAllWork()
    }

    override suspend fun getSpeed(listener: ((Int) -> Unit)) {
        speedListener = listener
    }

    override suspend fun isHaveNotes(): Boolean {
        return db.getTripJournal().isNotEmpty()
    }

    private suspend fun createNewJournal() {
        withContext(Dispatchers.IO) {
            db.clearTripJournal()
        }
    }

    private fun addTripData(tripDto: TripDto) {
        val currentSpeed = ConvertUtils.meterOnSecInKmPerHour(tripDto.speed)
        CoroutineScope(Dispatchers.IO).launch {
            _currentTripList = db.getTripJournal()
            if (currentSpeed > 7) {
                if (currentTripList.isNotEmpty()) {
                    if (isCorrectTimeData(tripDto.time, currentTripList[currentTripList.size - 1].time)) {

                        val tripData = TripData(
                            time = tripDto.time,
                            speed = ConvertUtils.meterOnSecInKmPerHour(tripDto.speed),
                            time_interval = calculateTimeInterval(currentTripList.size, tripDto),
                            average_speed = calculateAverageSpeed(currentTripList.size, tripDto),
                        )

                        tripData.distance =
                            calculateDistance(tripData.time_interval!!, tripData.average_speed!!)


                        addDataToDb(tripData)
//                        Log.e("Trip", "$tripData  | ${DataUtils.timeToString(tripData.time)}")
                    }
                } else {
                    val tripData = TripData(
                        time = tripDto.time,
                        speed = ConvertUtils.meterOnSecInKmPerHour(tripDto.speed)
                    )

                    addDataToDb(tripData)
//                    Log.e("Trip", "$tripData")
                }
            }
        }
        speedListener?.invoke(currentSpeed.toInt())
    }

    private fun isCorrectTimeData(currentTime: Long, previousTime: Long): Boolean {
        return (currentTime - previousTime) >= 1000
    }

    private fun addDataToDb(tripData: TripData) {
        db.saveTripJournal(
            TripDataDBEntity(
                time = tripData.time,
                speed = tripData.speed,
                time_interval = tripData.time_interval,
                average_speed = tripData.average_speed,
                distance = tripData.distance
            )
        )
    }

    private fun calculateTimeInterval(index: Int, tripDto: TripDto): Long {
        val currentTime = tripDto.time
        val previousTime = currentTripList[index - 1].time

        Log.e("TIME", "$currentTime | $previousTime ||    ${ConvertUtils.timeToString(currentTime)}  | ${ConvertUtils.timeToString(previousTime)}")

        return currentTime - previousTime
    }

    private fun calculateAverageSpeed(index: Int, tripDto: TripDto): Double {
        val currentSpeed = ConvertUtils.meterOnSecInKmPerHour(tripDto.speed)
        val previousSpeed = currentTripList[index - 1].speed

        return (currentSpeed + previousSpeed) / 2
    }

    private fun calculateDistance(deltaTime: Long, averageSpeed: Double): Double {
        val convertDeltaTime = (deltaTime / 1000)

        return ((ConvertUtils.kmPerHourImMeterOnSec(averageSpeed) * convertDeltaTime) / 1000)

    }
}