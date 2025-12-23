package com.lossabinos.domain.usecases.mechanics

import com.lossabinos.domain.entities.Vehicle
import com.lossabinos.domain.repositories.MechanicsRepository

class GetQRVehicleUseCase(
    private val mechanicsRepository: MechanicsRepository
) {
    suspend operator fun invoke(
        veicleId: String
    ) : Vehicle{
        return mechanicsRepository.getQRVehicle(
            vehicleId = veicleId
        )
    }
}