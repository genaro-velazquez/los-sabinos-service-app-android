package com.lossabinos.domain.repositories

import com.lossabinos.domain.entities.ActivityEvidence
import com.lossabinos.domain.entities.ActivityProgress
import com.lossabinos.domain.entities.ObservationAnswer
import com.lossabinos.domain.entities.ServiceFieldValue

interface ChecklistRepository {

    suspend fun saveActivityProgress(
        assignedServiceId: String,
        sectionIndex: Int,
        activityIndex: Int,
        description: String,
        requiresEvidence: Boolean
    ): Long

    suspend fun getTotalCompletedActivities(
        assignedServiceId: String
    ): Int

    suspend fun getObservationResponsesForSection(
        assignedServiceId: String,
        sectionIndex: Int
    ): List<ObservationAnswer>

    suspend fun getActivitiesProgressForSection(
        assignedServiceId: String,
        sectionIndex: Int
    ): List<ActivityProgress>

    suspend fun getEvidenceForActivity(
        activityProgressId: Long
    ): List<ActivityEvidence>

    suspend fun saveActivityEvidence(
        id: Long,
        activityProgressId: Long,
        filePath: String,
        fileType: String = "image"
    ): Long

    suspend fun saveObservationResponse(
        assignedServiceId: String,
        sectionIndex: Int,
        observationIndex: Int,
        observationDescription: String,
        response: String
    ): Long

    //****************************+***
    // Service Fields values
    //********************************
    suspend fun saveServiceFieldValue(
        assignedServiceId: String,
        fieldIndex: Int,
        fieldLabel: String,
        fieldType: String,
        required: Boolean,
        value: String?
    ): Long

    suspend fun saveServiceFieldValues(
        assignedServiceId: String,
        fields: List<ServiceFieldValue>
    )
}


