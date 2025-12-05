package com.lossabinos.domain.repositories

import com.lossabinos.domain.responses.AssignedServicesResponse
import com.lossabinos.domain.responses.DetailedServiceResponse
import com.lossabinos.domain.responses.InitialDataResponse

interface MechanicsRepository {

    suspend fun assignedServices(): AssignedServicesResponse

    suspend fun detailedService(idService:String): DetailedServiceResponse

    suspend fun syncInitialData(): InitialDataResponse

}