package com.lossabinos.domain.responses

import com.lossabinos.domain.entities.AssignedService
import com.lossabinos.domain.entities.DomainEntity
import com.lossabinos.domain.entities.Mechanic
import com.lossabinos.domain.entities.ServiceType
import com.lossabinos.domain.valueobjects.SyncMetadata

class InitialDataResponse(
    val mechanic: Mechanic,
    val assignedServices: List<AssignedService>,
    val serviceTypes: List<ServiceType>,
    val syncMetadata: SyncMetadata
) : DomainEntity()