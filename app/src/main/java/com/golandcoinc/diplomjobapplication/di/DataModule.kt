package com.golandcoinc.diplomjobapplication.di

import com.golandcoinc.data.commands.AppCommandsImpl
import com.golandcoinc.data.journals.RefuelingIntervalJournal
import com.golandcoinc.data.journals.StatsJournal
import com.golandcoinc.data.journals.TripJournal
import com.golandcoinc.data.journals.impl.RefuelingIntervalJournalImpl
import com.golandcoinc.data.journals.impl.StatsJournalImpl
import com.golandcoinc.data.journals.impl.TripJournalImpl
import com.golandcoinc.diplomjobapplication.app.App
import com.golandcoinc.domain.commands.AppCommands
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {

    single<AppCommands> {
        AppCommandsImpl(
            tripJournal = get(),
            refuelingIntervalJournal = get(),
            statsJournal = get(),
            db = (androidApplication().applicationContext as App).db.getDao()
        )
    }

    single<TripJournal> {
        TripJournalImpl(
            context = get(),
            db = (androidApplication().applicationContext as App).db.getDao())
    }

    single<RefuelingIntervalJournal> {
        RefuelingIntervalJournalImpl(
            db = (androidApplication().applicationContext as App).db.getDao()
        )
    }

    single<StatsJournal> {
        StatsJournalImpl(
            androidApplication(),
            db = (androidApplication().applicationContext as App).db.getDao()
        )
    }
}

