package com.lossabinos.domain.repositories

import com.lossabinos.domain.entities.AssignedService
import com.lossabinos.domain.entities.Mechanic
import com.lossabinos.domain.entities.ServiceType
import com.lossabinos.domain.responses.AssignedServicesResponse
import com.lossabinos.domain.responses.DetailedServiceResponse
import com.lossabinos.domain.responses.InitialDataResponse

interface MechanicsRepository {

    suspend fun assignedServices(): AssignedServicesResponse

    suspend fun detailedService(idService:String): DetailedServiceResponse

    suspend fun syncInitialData(): InitialDataResponse

    // esto se me hace que no se usará
    suspend fun getLocalMechanic(): Mechanic?
    suspend fun getLocalAssignedServices(): List<AssignedService>
    suspend fun getLocalServiceTypes(): List<ServiceType>

    suspend fun getLocalInitialData(): InitialDataResponse

}