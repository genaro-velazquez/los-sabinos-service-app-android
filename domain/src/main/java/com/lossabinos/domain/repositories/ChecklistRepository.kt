package com.lossabinos.domain.repositories

import com.lossabinos.domain.entities.ActivityEvidence
import com.lossabinos.domain.entities.ActivityProgress
import com.lossabinos.domain.entities.ExtraCost
import com.lossabinos.domain.entities.ObservationAnswer
import com.lossabinos.domain.entities.ServiceFieldValue
import com.lossabinos.domain.enums.ServiceStatus
import com.lossabinos.domain.enums.SyncStatus
import com.lossabinos.domain.responses.SignChecklistResponse
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository {

    suspend fun saveActivityProgress(
        assignedServiceId: String,
        sectionIndex: Int,
        activityIndex: Int,
        activityId:String,
        description: String,
        requiresEvidence: Boolean,
        completed: Boolean = true,
        completedAt: String = System.currentTimeMillis().toString()
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

    /*******************************************
     * evidence_activity (guardar fotos/video)
     ******************************************/
    suspend fun getEvidenceForActivity(
        activityProgressId: Long
    ): List<ActivityEvidence>

    suspend fun saveActivityEvidence(
        id: Long,
        activityProgressId: Long,
        filePath: String,
        fileType: String = "image"
    ): Long

    suspend fun deleteActivityEvidenceById(evidenceId: Long)

    suspend fun saveObservationResponse(
        assignedServiceId: String,
        sectionIndex: Int,
        observationIndex: Int,
        observationId: String,
        observationDescription: String,
        responseType: String,
        response: String? = null,
        requiresResponse: Boolean = false
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

    suspend fun getServiceFieldValues(
        assignedServiceId: String
    ): List<ServiceFieldValue>

    //****************************+***
    // Service_progress
    //********************************
    suspend fun saveServiceProgress(
        assignedServiceId: String,
        completedActivities: Int = 0,
        totalActivities: Int = 0,
        completedPercentage: Int = 0,
        status: ServiceStatus, // "pending", "in_progress", "completed"
        lastUpdatedAt: Long, //= System.currentTimeMillis(),
        syncStatus: SyncStatus //= "PENDING"
    )

    //****************************+***
    // SyncChecklist to server
    //********************************
    suspend fun syncChecklist(
        serviceId: String
    )

    suspend fun syncActivityChecklistEvidence(
        serviceId: String,
        activityId: String
    )

    suspend fun isServiceSynced(serviceId: String): Boolean

    suspend fun signChecklist(
        serviceExecutionId:String
    ) : SignChecklistResponse

    suspend fun createReportExtraCost(
        idExecutionService: String,
        amount: Double,
        category:String,
        description: String,
        notes: String
    )

    fun observeExtraCosts(
        assignedServiceId: String
    ): Flow<List<ExtraCost>>
    suspend fun insertExtraCost(extraCost: ExtraCost)
    suspend fun updateExtraCost(extraCost: ExtraCost)
    suspend fun deleteExtraCost(id: String)
}


