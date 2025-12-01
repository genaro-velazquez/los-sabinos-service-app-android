package com.lossabinos.domain.repositories

import com.lossabinos.domain.responses.AssignedServicesResponse

interface MechanicsRepository {

    suspend fun assignedServices(): AssignedServicesResponse

}