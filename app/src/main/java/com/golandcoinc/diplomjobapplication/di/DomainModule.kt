package com.golandcoinc.diplomjobapplication.di

import com.golandcoinc.domain.usecases.*
import org.koin.dsl.module

val domainModule = module {

    single {
        StartTrip(appCommands = get())
    }

    single {
        StopTrip(appCommands = get())
    }

    single {
        FillFuelTank(appCommands = get())
    }

    single {
        ListenSpeed(appCommands = get())
    }

    single {
        SetSpeed(appCommands = get())
    }

    single {
        GetRecommendSpeed(appCommands = get())
    }

    single {
        GetStatsJournal(appCommands = get())
    }

    single {
        DeleteStatsJournal(appCommands = get())
    }

    single {
        IsHaveNotesTripJournal(appCommands = get())
    }
}