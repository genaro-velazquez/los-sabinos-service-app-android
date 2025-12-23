package com.lossabinos.data.local.mappers

import com.lossabinos.data.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.local.database.entities.ActivityProgressEntity
import com.lossabinos.data.local.database.entities.ObservationResponseEntity
import com.lossabinos.data.local.database.entities.ServiceFieldValueEntity
import com.lossabinos.domain.entities.ActivityEvidence
import com.lossabinos.domain.entities.ActivityProgress
import com.lossabinos.domain.entities.ObservationAnswer
import com.lossabinos.domain.entities.ServiceFieldValue
import com.lossabinos.domain.valueobjects.FieldType


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

