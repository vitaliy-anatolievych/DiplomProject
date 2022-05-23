package com.golandcoinc.domain.usecases

import com.golandcoinc.domain.commands.AppCommands

class ListenSpeed(private val appCommands: AppCommands) {

    suspend operator fun invoke(listener: ((Int) -> Unit)) = appCommands.listenSpeed(listener)
}