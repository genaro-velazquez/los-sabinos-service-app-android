package com.lossabinos.data.datasource.remoto

import com.lossabinos.data.dto.entities.VehicleDTO
import com.lossabinos.data.dto.repositories.retrofit.RetrofitResponseValidator
import com.lossabinos.data.dto.responses.AssignedServicesResponseDTO
import com.lossabinos.data.dto.responses.DetailedServiceResponseDTO
import com.lossabinos.data.dto.responses.InitialDataResponseDTO
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.retrofit.MechanicsServices
import javax.inject.Inject

class MechanicsRemoteDataSource @Inject constructor(
    private val service : MechanicsServices,
    private val assignedServices: MechanicsServices,
    private val headersMaker: HeadersMaker
){

    suspend fun assignedServices(): AssignedServicesResponseDTO {
        val response = assignedServices.assignedServices(headers = headersMaker.build())
        val json = RetrofitResponseValidator.Companion.validate(response = response)
        return AssignedServicesResponseDTO(json = json)
    }

    suspend fun detailedService(idService: String): DetailedServiceResponseDTO {
        val response = assignedServices.detailedService(headers = headersMaker.build(), idService = idService)
        val json = RetrofitResponseValidator.Companion.validate(response = response)
        return DetailedServiceResponseDTO(json = json)
    }

    suspend fun syncInitialData(): InitialDataResponseDTO {
        val response = service.syncInitialData(headers = headersMaker.build())
        val json = RetrofitResponseValidator.Companion.validate(response)
        return InitialDataResponseDTO(json)
    }

    suspend fun getQRVehicle(
        vehicleId: String
    ): VehicleDTO {
        val response = assignedServices.getVehicleByQR(headers = headersMaker.build(), idVehicle = vehicleId)
        val json = RetrofitResponseValidator.Companion.validate(response = response)
        return VehicleDTO(json = json)
    }
}