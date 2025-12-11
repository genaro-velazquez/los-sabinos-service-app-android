package com.lossabinos.data.local.mappers

import com.lossabinos.data.local.database.entities.AssignedServiceEntity
import com.lossabinos.data.local.database.entities.MechanicEntity
import com.lossabinos.data.local.database.entities.ServiceTypeEntity
import com.lossabinos.domain.entities.AssignedService
import com.lossabinos.domain.entities.Mechanic
import com.lossabinos.domain.entities.ServiceType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
        workOrderNumber = this.workOrderNumber,
        serviceTypeId = this.serviceTypeId,
        serviceTypeName = this.serviceTypeName,
        vehicleId = this.vehicle.id,
        vehicleVin = this.vehicle.vin,
        vehicleEconomicNumber = this.vehicle.economicNumber,
        vehicleModelName = this.vehicle.modelName,
        status = this.status,
        priority = this.priority,
        notes = null,  // Por ahora, no viene en el DTO
        scheduledStart = this.scheduledStart,
        scheduledEnd = this.scheduledEnd,
        checklistTemplateJson = Json.encodeToString(this.checklistTemplate),
        progressPercentage = 0
    )
}
