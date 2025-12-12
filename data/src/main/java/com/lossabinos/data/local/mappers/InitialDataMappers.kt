package com.lossabinos.data.local.mappers

import com.lossabinos.data.local.database.entities.AssignedServiceEntity
import com.lossabinos.data.local.database.entities.MechanicEntity
import com.lossabinos.data.local.database.entities.ServiceTypeEntity
import com.lossabinos.data.local.database.entities.SyncMetadataEntity
import com.lossabinos.domain.entities.AssignedService
import com.lossabinos.domain.entities.Mechanic
import com.lossabinos.domain.entities.ServiceType
import com.lossabinos.domain.entities.Vehicle
import com.lossabinos.domain.valueobjects.ChecklistTemplate
import com.lossabinos.domain.valueobjects.SyncMetadata
import com.lossabinos.domain.valueobjects.Template
import com.lossabinos.domain.valueobjects.VehicleVersion
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
fun MechanicEntity.toDomain() : Mechanic{
    return Mechanic(
        id = this.id,
        name = this.name,
        email  = this.email,
        zoneId = this.zoneId,
        zoneName = this.zoneName,
        fullName = "",
        rol = ""
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

fun ServiceTypeEntity.toDomain(): ServiceType {
    return ServiceType(
        id = this.id,
        name = this.name,
        code = this.code,
        category = this.category,
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

// ============ AssignedService: Room → Domain ============
fun AssignedServiceEntity.toDomain(): AssignedService {
    // Deserializar Template desde JSON
    val checklistTemplate = checklistTemplateJson?.let {
        try {
            Json.decodeFromString<ChecklistTemplate>(it)
        } catch (e: Exception) {
            println("❌ Error deserializando template: ${e.message}")
            ChecklistTemplate(name = "", version = "0.0", template = Template(name = "", sections = emptyList(), serviceFields = emptyList()))
        }
    } ?: ChecklistTemplate(name = "", version = "0.0", template = Template(name = "", sections = emptyList(), serviceFields = emptyList()))

    return AssignedService(
        id = this.id,
        workOrderId = this.workOrderId,
        workOrderNumber = this.workOrderNumber,
        serviceTypeId = this.serviceTypeId,
        serviceTypeName = this.serviceTypeName,
        vehicle = Vehicle(
            id = this.vehicleId,
            vin = this.vehicleVin ?: "",
            economicNumber = this.vehicleEconomicNumber ?: "",
            modelName = this.vehicleModelName ?: "",
            vehicleNumber = "",
            licensePlate = "",
            vehicleVersion = VehicleVersion(make = "", model = "", year = 0),
            currentOdometerKm = 0
        ),
        status = this.status,
        priority = this.priority,
        scheduledStart = this.scheduledStart ?: "",
        scheduledEnd = this.scheduledEnd ?: "",
        checklistTemplate = ChecklistTemplate(
            name = checklistTemplate.name, version = checklistTemplate.version, template = Template(name = checklistTemplate.template.name, sections = checklistTemplate.template.sections, serviceFields = checklistTemplate.template.serviceFields)
        )
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

// ============ SyncMetadata: Room → Domain ============
fun SyncMetadataEntity.toDomain(): SyncMetadata {
    return SyncMetadata(
        totalServices = this.totalServices,
        pendingServices = this.pendingServices,
        inProgressServices = this.inProgressServices,
        serverTimestamp = this.serverTimestamp,
        lastSync = this.lastSync?:""
    )
}
