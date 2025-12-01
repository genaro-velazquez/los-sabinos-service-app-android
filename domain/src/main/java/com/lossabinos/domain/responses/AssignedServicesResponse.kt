package com.lossabinos.domain.responses

import com.lossabinos.domain.entities.DomainEntity
import com.lossabinos.domain.entities.Mechanic
import com.lossabinos.domain.entities.WorkOrder
import com.lossabinos.domain.valueobjects.Pagination
import com.lossabinos.domain.valueobjects.Summary

class AssignedServicesResponse(
    val mechanic: Mechanic,
    val summary: Summary,
    val pagination: Pagination,
    val workOrder: List<WorkOrder>
): DomainEntity()