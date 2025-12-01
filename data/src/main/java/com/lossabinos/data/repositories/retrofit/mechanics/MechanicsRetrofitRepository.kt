package com.lossabinos.data.repositories.retrofit.mechanics

import com.lossabinos.data.dto.repositories.retrofit.RetrofitResponseValidator
import com.lossabinos.data.dto.responses.AssignedServicesResponseDTO
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.domain.repositories.MechanicsRepository
import com.lossabinos.domain.responses.AssignedServicesResponse

class MechanicsRetrofitRepository(
    private val assignedServices: MechanicsServices,
    private val headersMaker: HeadersMaker
) : MechanicsRepository {

    override suspend fun assignedServices(): AssignedServicesResponse {
        val response = assignedServices.assignedServices(headers = headersMaker.build())
        val json = RetrofitResponseValidator.validate(response = response)
        val dto  = AssignedServicesResponseDTO(json = json)
        return dto.toEntity()
    }

}