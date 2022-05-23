package com.golandcoinc.domain.usecases

import com.golandcoinc.domain.commands.AppCommands

class StopTrip(private val appCommands: AppCommands) {

    suspend operator fun invoke() = appCommands.stopTrip()
}