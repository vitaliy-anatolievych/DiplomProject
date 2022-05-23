package com.golandcoinc.diplomjobapplication.app

import android.app.Application
import com.golandcoinc.data.db.AppDataBase
import com.golandcoinc.diplomjobapplication.di.appModule
import com.golandcoinc.diplomjobapplication.di.dataModule
import com.golandcoinc.diplomjobapplication.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    val db by lazy { AppDataBase.getInstance(context = this) }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(
                appModule,
                domainModule,
                dataModule
            ))
        }
    }
}