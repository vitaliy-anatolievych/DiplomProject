package com.golandcoinc.diplomjobapplication.di

import com.golandcoinc.diplomjobapplication.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        MainViewModel(
            startTripUseCase = get(),
            stopTripUseCase = get(),
            fillFuelTankUseCase = get(),
            listenSpeedUseCase = get(),
            setSpeedUseCase = get(),
            getRecommendSpeedUseCase = get(),
            getStatsJournalUseCase = get(),
            deleteStatsJournalUseCase = get(),
            isHaveNotesTripJournalUseCase = get()
        )
    }
}