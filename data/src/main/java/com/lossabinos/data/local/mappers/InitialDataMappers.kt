package com.lossabinos.data.local.mappers

import com.lossabinos.data.local.database.entities.AssignedServiceEntity
import com.lossabinos.data.local.database.entities.MechanicEntity
import com.lossabinos.data.local.database.entities.ServiceTypeEntity
import com.lossabinos.domain.entities.AssignedService
import com.lossabinos.domain.entities.Mechanic
import com.lossabinos.domain.entities.ServiceType

// ============ Mechanic: Domain → Room ============
fun Mechanic.toRoomEntity(): MechanicEntity {
    return MechanicEntity(
        id = this.id,
        name = this.name,
        email = this.email
    )
}


// ============ ServiceType: Domain → Room ============
fun ServiceType.toRoomEntity(): ServiceTypeEntity {
    return ServiceTypeEntity(
        id = this.id,
        name = this.name,
        estimatedDurationMinutes = this.estimatedDurationMinutes
    )
}


// ============ AssignedService: Domain → Room ============
fun AssignedService.toRoomEntity(): AssignedServiceEntity {
    return AssignedServiceEntity(
        id = this.id,
        workOrderId = this.workOrderId,
        serviceTypeId = this.serviceTypeId,
        status = this.status,
        priority = this.priority,
        notes = null,  // Por ahora, no viene en el DTO
        scheduledStart = this.scheduledStart,
        scheduledEnd = this.scheduledEnd
    )
}
