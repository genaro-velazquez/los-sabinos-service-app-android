package com.lossabinos.data.repositories.retrofit.mechanics

import com.lossabinos.data.dto.repositories.retrofit.RetrofitResponseValidator
import com.lossabinos.data.dto.responses.AssignedServicesResponseDTO
import com.lossabinos.data.dto.responses.DetailedServiceResponseDTO
import com.lossabinos.data.dto.responses.InitialDataResponseDTO
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.domain.repositories.MechanicsRepository
import com.lossabinos.domain.responses.AssignedServicesResponse
import com.lossabinos.domain.responses.DetailedServiceResponse
import com.lossabinos.domain.responses.InitialDataResponse

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

    override suspend fun detailedService(idService: String): DetailedServiceResponse {
        val response = assignedServices.detailedService(headers = headersMaker.build(), idService = idService)
        val json = RetrofitResponseValidator.validate(response = response)
        val dto = DetailedServiceResponseDTO(json = json)
        return dto.toEntity()
    }

    override suspend fun syncInitialData(): InitialDataResponse {
        val response = assignedServices.syncInitialData(headers = headersMaker.build())
        val json = RetrofitResponseValidator.validate(response = response)
        val dto = InitialDataResponseDTO(json = json)
        return dto.toEntity()
    }

}