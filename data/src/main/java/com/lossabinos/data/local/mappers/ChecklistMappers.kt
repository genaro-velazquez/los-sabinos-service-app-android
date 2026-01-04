package com.lossabinos.data.local.mappers

import com.lossabinos.data.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.local.database.entities.ActivityProgressEntity
import com.lossabinos.data.local.database.entities.AssignedServiceWithProgressEntity
import com.lossabinos.data.local.database.entities.ObservationResponseEntity
import com.lossabinos.data.local.database.entities.ServiceFieldValueEntity
import com.lossabinos.domain.entities.ActivityEvidence
import com.lossabinos.domain.entities.ActivityProgress
import com.lossabinos.domain.entities.ObservationAnswer
import com.lossabinos.domain.entities.ServiceFieldValue
import com.lossabinos.domain.valueobjects.AssignedServiceProgress
import com.lossabinos.domain.valueobjects.ChecklistTemplate
import com.lossabinos.domain.valueobjects.FieldType
import com.lossabinos.domain.valueobjects.Template
import kotlinx.serialization.json.Json


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

fun ObservationAnswer.toEntity(): ObservationResponseEntity {
    return ObservationResponseEntity(
        id = id,
        assignedServiceId = assignedServiceId,
        sectionIndex = sectionIndex,
        observationIndex = observationIndex,
        observationDescription = observationDescription,
        response = answer,
        timestamp = timestamp
    )
}

//++++++++++++++++++++++++++++
// ActivityProgressActivity
//****************************

fun ActivityProgressEntity.toDomain() : ActivityProgress{
    return ActivityProgress(
        assignedServiceId = assignedServiceId,
        sectionIndex = sectionIndex,
        activityIndex = activityIndex,
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

//*************************
// AssignedServiceProgress
//*************************
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

    // Calcular porcentaje
    val completedPercentage = if (totalActivities > 0) {
        (completedActivities * 100) / totalActivities
    } else {
        0
    }

    // Determinar estado actual
    val currentStatus = when {
        totalActivities == 0 -> assignedService.status
        completedActivities == totalActivities && totalActivities > 0 -> "completed"
        completedActivities > 0 -> "in_progress"
        else -> "pending"
    }

    return AssignedServiceProgress(
        assignedService = assignedService,
        totalActivities = totalActivities,
        completedActivities = completedActivities,
        completedPercentage = completedPercentage,
        currentStatus = currentStatus
    )

}