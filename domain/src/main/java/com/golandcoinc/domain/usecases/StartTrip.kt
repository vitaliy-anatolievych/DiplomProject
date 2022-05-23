package com.golandcoinc.domain.usecases

import com.golandcoinc.domain.commands.AppCommands

class StartTrip(private val appCommands: AppCommands) {

    suspend operator fun invoke() = appCommands.startTrip()
}