package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.AssignedService
import com.lossabinos.domain.entities.DomainEntity

class AssignedServiceProgress(
    val assignedService: AssignedService,
    val totalActivities: Int = 0,
    val completedActivities: Int = 0,
    val completedPercentage: Int = 0,
    val serviceStatus: ServiceStatus = ServiceStatus.PENDING,
    val syncStatus: SyncStatus = SyncStatus.PENDING
): DomainEntity()