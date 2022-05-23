package com.golandcoinc.domain.usecases

import com.golandcoinc.domain.commands.AppCommands

class FillFuelTank(private val appCommands: AppCommands) {

    suspend operator fun invoke(fuelTankVolume: Double) = appCommands.fillFuelTank(fuelTankVolume)
}