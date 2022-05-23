package com.golandcoinc.domain.usecases

import com.golandcoinc.domain.commands.AppCommands

class SetSpeed(private val appCommands: AppCommands) {

    suspend operator fun invoke(speed: Int) = appCommands.setSpeed(speed)
}