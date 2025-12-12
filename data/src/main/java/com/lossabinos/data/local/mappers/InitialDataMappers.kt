package com.lossabinos.data.local.mappers

import com.lossabinos.data.local.database.entities.AssignedServiceEntity
import com.lossabinos.data.local.database.entities.MechanicEntity
import com.lossabinos.data.local.database.entities.ServiceTypeEntity
import com.lossabinos.data.local.database.entities.SyncMetadataEntity
import com.lossabinos.domain.entities.AssignedService
import com.lossabinos.domain.entities.Mechanic
import com.lossabinos.domain.entities.ServiceType
import com.lossabinos.domain.valueobjects.SyncMetadata
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// ============ Mechanic: Domain → Room ============
fun Mechanic.toRoomEntity(): MechanicEntity {
    return MechanicEntity(
        id = this.id,
        name = this.name,
        email = this.email,
        zoneId = this.zoneId,
        zoneName = this.zoneName
    )
}


// ============ ServiceType: Domain → Room ============
fun ServiceType.toRoomEntity(): ServiceTypeEntity {
    return ServiceTypeEntity(
        id = this.id,
        name = this.name,
        estimatedDurationMinutes = this.estimatedDurationMinutes,
        code = this.code,
        category = this.category
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

// ============ SyncMetadata: Domain → Room ============
fun SyncMetadata.toRoomEntity(): SyncMetadataEntity {
    return SyncMetadataEntity(
        id = "sync_metadata",  // ID fijo
        serverTimestamp = serverTimestamp,
        totalServices = totalServices,
        pendingServices = pendingServices,
        inProgressServices = inProgressServices,
        lastSync = lastSync,
        updatedAt = java.time.Instant.now().toString()  // Timestamp actual
    )
}

