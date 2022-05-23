package com.golandcoinc.diplomjobapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.golandcoinc.diplomjobapplication.utils.SpeedStatus
import com.golandcoinc.domain.entities.StatsJournalModel
import com.golandcoinc.domain.usecases.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainViewModel(
    private val startTripUseCase: StartTrip,
    private val stopTripUseCase: StopTrip,
    private val fillFuelTankUseCase: FillFuelTank,
    private val listenSpeedUseCase: ListenSpeed,
    private val setSpeedUseCase: SetSpeed,
    private val getRecommendSpeedUseCase: GetRecommendSpeed,
    private val getStatsJournalUseCase: GetStatsJournal,
    private val deleteStatsJournalUseCase: DeleteStatsJournal,
    private val isHaveNotesTripJournalUseCase: IsHaveNotesTripJournal
) : ViewModel() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private var _currentSpeed = MutableLiveData<Int>()
    val currentSpeed: LiveData<Int>
        get() = _currentSpeed

    private var _recommendedSpeed = MutableLiveData<Int?>()
    val recommendedSpeed: LiveData<Int?>
        get() = _recommendedSpeed

    private var _statsJournal = MutableLiveData<StatsJournalModel>()
    val statsJournalModel: LiveData<StatsJournalModel>
        get() = _statsJournal



    private var _speedStatus = MutableLiveData<SpeedStatus>()
    val speedStatus: LiveData<SpeedStatus>
        get() = _speedStatus

    fun startTrip() {
        coroutineScope.launch {
            startTripUseCase()
        }
    }

    fun stopTrip() {
        coroutineScope.launch {
            stopTripUseCase()
        }
    }

    fun fillFuelTank(fuelTankVolume: String): Boolean =
        if (validInputFuelTank(fuelTankVolume)) {
            coroutineScope.launch {
                fillFuelTankUseCase(fuelTankVolume.toDouble())
                getRecommendedSpeed()
            }
            true
        } else {
            false
        }

    fun setSpeed(speed: String): Boolean =
        if (validInputSpeed(speed)) {
            coroutineScope.launch {
                setSpeedUseCase(speed.toInt())
            }
            true
        } else {
            false
        }

    fun isHaveNotesTripJournal() {
        coroutineScope.launch {
            isHaveNotesTripJournalUseCase()
        }
    }

    fun getRecommendedSpeed() {
        coroutineScope.launch {
            val speed = getRecommendSpeedUseCase()
            withContext(Dispatchers.Main) {
                _recommendedSpeed.value = speed
            }
        }
    }

    fun listenSpeed() {
        coroutineScope.launch {
            withContext(Dispatchers.Default) {
                listenSpeedUseCase {
                    _currentSpeed.value = it
                }
            }
        }
    }

    fun listenSpeedStatus(currentSpeed: Int) {
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                if (_recommendedSpeed.value != null) {
                    checkStatusSpeed(currentSpeed)
                }
            }
        }
    }

    fun getStatsJournal() {
        coroutineScope.launch {
            val journal = getStatsJournalUseCase()
            withContext(Dispatchers.Main) {
                _statsJournal.value = journal
            }
        }
    }

    fun deleteStatsJournal() {
        coroutineScope.launch {
            deleteStatsJournalUseCase()
            withContext(Dispatchers.Main) {
                _statsJournal.value = StatsJournalModel(listOf())
            }
        }
    }

    private fun checkStatusSpeed(speed: Int) {
        when {
            speed > recommendedSpeed.value!! -> {
                _speedStatus.value = SpeedStatus.LOWER
            }
            speed < recommendedSpeed.value!! -> {
                _speedStatus.value = SpeedStatus.FASTER
            }
            speed == recommendedSpeed.value!! -> {
                _speedStatus.value = SpeedStatus.NORMAL
            }
        }
    }

    private fun validInputFuelTank(fuelTankVolume: String): Boolean {
        return try {
            when {
                fuelTankVolume.isBlank() -> false
                fuelTankVolume.toDouble() <= 0.0 -> false
                else -> true
            }
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun validInputSpeed(speed: String): Boolean {
        return when {
            speed.isBlank() -> false
            speed.toInt() <= 0 -> false
            speed.toInt() > 300 -> false
            else -> true
        }
    }
}


