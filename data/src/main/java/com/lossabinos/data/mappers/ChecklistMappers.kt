package com.lossabinos.data.mappers

import com.lossabinos.data.datasource.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.datasource.local.database.entities.ActivityProgressEntity
import com.lossabinos.data.datasource.local.database.entities.AssignedServiceWithProgressEntity
import com.lossabinos.data.datasource.local.database.entities.ExtraCostEntity
import com.lossabinos.data.datasource.local.database.entities.ObservationResponseEntity
import com.lossabinos.data.datasource.local.database.entities.ServiceFieldValueEntity
import com.lossabinos.domain.entities.ActivityEvidence
import com.lossabinos.domain.entities.ActivityProgress
import com.lossabinos.domain.entities.ExtraCost
import com.lossabinos.domain.entities.ObservationAnswer
import com.lossabinos.domain.entities.ServiceFieldValue
import com.lossabinos.domain.valueobjects.AssignedServiceProgress
import com.lossabinos.domain.valueobjects.ChecklistTemplate
import com.lossabinos.domain.valueobjects.FieldType
import com.lossabinos.domain.enums.ServiceStatus
import com.lossabinos.domain.enums.SyncStatus
import com.lossabinos.domain.valueobjects.Template
import kotlinx.serialization.json.Json

fun ExtraCostEntity.toDomain(): ExtraCost {
    return ExtraCost(
        id = id,
        assignedServiceId = assignedServiceId,
        quantity = quantity,
        category = category,
        description = description,
        notes = notes,
        createdAt = createdAt,
        syncStatus = syncStatus,
        timestamp = timestamp
    )
}


//++++++++++++++++++++++++++++
// ObservationResponseEntity
//****************************

fun ObservationResponseEntity.toDomain(): ObservationAnswer{
    return ObservationAnswer(
        id = id,
        assignedServiceId = assignedServiceId,
        sectionIndex = sectionIndex,
        observationIndex = observationIndex,
        observationDescription = observationDescription,
        answer = response,
        timestamp = timestamp
    )
}
/*
fun ObservationAnswer.toEntity(): ObservationResponseEntity {
    return ObservationResponseEntity(
        id = id,
        assignedServiceId = assignedServiceId,
        sectionIndex = sectionIndex,
        observationIndex = observationIndex,
        observationId = ob,
        observationDescription = observationDescription,
        response = answer,
        timestamp = timestamp
    )
}
*/
//++++++++++++++++++++++++++++
// ActivityProgressActivity
//****************************

fun ActivityProgressEntity.toDomain() : ActivityProgress{
    return ActivityProgress(
        assignedServiceId = assignedServiceId,
        sectionIndex = sectionIndex,
        activityIndex = activityIndex,
        activityId = activityId,
        activityDescription = activityDescription,
        requiresEvidence = requiresEvidence,
        completed = completed,
        completedAt = completedAt,
        id = id
    )
}

fun ActivityProgress.toEntity() : ActivityProgressEntity{
    return ActivityProgressEntity(
        assignedServiceId = assignedServiceId,
        sectionIndex = sectionIndex,
        activityIndex = activityIndex,
        activityId = activityId,
        activityDescription = activityDescription,
        requiresEvidence = requiresEvidence,
        completed = completed,
        completedAt = completedAt
    )
}

//*************************
// ActivityEvidenceEntity
//*************************

fun ActivityEvidenceEntity.toDomain() : ActivityEvidence{
    return ActivityEvidence(
        id = id,
        activityProgressId = activityProgressId,
        filePath = filePath,
        fileType = filePath,
        timestamp = timestamp
    )
}

fun ActivityEvidence.toEntity() : ActivityEvidenceEntity {
    return ActivityEvidenceEntity(
        id = id,
        activityProgressId = activityProgressId,
        filePath = filePath,
        fileType = filePath,
        timestamp = timestamp
    )
}

//*************************
// ServiceFieldValueEntity
//*************************

fun ServiceFieldValueEntity.toDomain() : ServiceFieldValue{
    return ServiceFieldValue(
        id                  = id.toString(),
        assignedServiceId   = assignedServiceId,
        fieldIndex          = fieldIndex,
        fieldLabel          = fieldLabel,
        fieldType           = fieldType.toFieldType(),
        required            = required,
        value               = value,
        timestamp           = timestamp
    )
}

fun ServiceFieldValue.toEntity() : ServiceFieldValueEntity{
    return ServiceFieldValueEntity(
        id = id.toLong(),
        assignedServiceId = assignedServiceId,
        fieldIndex = fieldIndex,
        fieldLabel= fieldLabel,
        fieldType = fieldType.name,
        required = false,
        value = null,
        timestamp = timestamp
    )
}

private fun String.toFieldType(): FieldType {
    return try {
        FieldType.valueOf(this)
    } catch (e: IllegalArgumentException) {
        FieldType.TEXT_INPUT // default seguro
    }
}

//**********************************
// AssignedServiceProgress (Domain)
//***********************************
fun AssignedServiceWithProgressEntity.toDomain(): AssignedServiceProgress{
    // Convertir entity a domain model
    val assignedService = this.assignedService.toDomain()

    // Deserializar Template desde JSON
    val checklistTemplate = this.assignedService.checklistTemplateJson?.let {
        try {
            Json.decodeFromString<ChecklistTemplate>(it)
        } catch (e: Exception) {
            println("❌ Error deserializando template: ${e.message}")
            ChecklistTemplate(name = "", version = "0.0", template = Template(name = "", sections = emptyList(), serviceFields = emptyList()))
        }
    } ?: ChecklistTemplate(name = "", version = "0.0", template = Template(name = "", sections = emptyList(), serviceFields = emptyList()))

    // Obtener total de actividades
    val totalActivities = checklistTemplate.template.sections.sumOf { it.activities.size }
    val completedActivities = this.completedCount

    println("\n📊 ═══════════════════════════════════════════════════")
    println("📊 [MAPPER] Servicio: ${assignedService.id}")
    println("   - Estado servidor: ${assignedService.status}")
    println("   - totalActivities: $totalActivities")
    println("   - completedActivities (completedCount): $completedActivities")
    println("   - completedCount value: ${this.completedCount}")
    println("   - activityProgressCount: ${this.activityProgressCount}")
    println("   - service_progress syncStatus: ${this.syncStatus}")
    println("📊 ═══════════════════════════════════════════════════\n")


    // Calcular porcentaje
    val completedPercentage = if (totalActivities > 0) {
        (completedActivities * 100) / totalActivities
    } else {
        0
    }

    // ✅ CONVERTIR STRING → ENUM para serviceStatus
    val serviceStatusEnum = when {
        totalActivities == 0 -> ServiceStatus.PENDING
        completedActivities == totalActivities && totalActivities > 0 -> ServiceStatus.COMPLETED
        completedActivities > 0 -> ServiceStatus.IN_PROGRESS
        else -> ServiceStatus.PENDING
    }

    // ✅ CONVERTIR STRING → ENUM para syncStatus
    val syncStatusEnum = when (this.syncStatus) {
        "SYNCED" -> SyncStatus.SYNCED
        "PENDING" -> SyncStatus.PENDING
        //"ERROR" -> SyncStatus.ERROR
        else -> SyncStatus.PENDING
    }


    val serviceStatus = when {
        totalActivities > 0 -> {
            when {
                completedActivities == totalActivities -> {
                    println("   → COMPLETED (${completedActivities} == ${totalActivities})")
                    ServiceStatus.COMPLETED
                }
                completedActivities > 0 -> {
                    println("   → IN_PROGRESS (${completedActivities} > 0)")
                    ServiceStatus.IN_PROGRESS
                }
                // ← CAMBIAR: Si no hay completadas pero hay checklist,
                // usar estado del servidor
                else -> {
                    println("   ⚠️ Sin datos locales, usando servidor: ${assignedService.status}")
                    when (assignedService.status.lowercase()) {
                        "in_progress" -> ServiceStatus.IN_PROGRESS  // ← AQUÍ
                        "completed" -> ServiceStatus.COMPLETED
                        else -> ServiceStatus.PENDING
                    }
                }
            }
        }

        else -> {
            println("   👉 RAMA 2: totalActivities == 0, usando servidor")
            when (assignedService.status.lowercase()) {
                "in_progress" -> ServiceStatus.IN_PROGRESS
                "completed" -> ServiceStatus.COMPLETED
                "pending_approval" -> ServiceStatus.PENDING_APPROVAL
                else -> ServiceStatus.PENDING
            }
        }
    }


    return AssignedServiceProgress(
        assignedService = assignedService,
        totalActivities = totalActivities,
        completedActivities = completedActivities,
        completedPercentage = completedPercentage,
        serviceStatus = serviceStatus,
        syncStatus = syncStatusEnum
    )

}