package com.lossabinos.domain.repositories

import com.lossabinos.domain.valueobjects.WorkRequest
import com.lossabinos.domain.valueobjects.WorkRequestIssue

interface WorkRequestRepository{
    suspend fun createWorkRequest(request: WorkRequest)

    suspend fun createWorkRequestIssue(
        serviceExecutionId: String,
        issue: WorkRequestIssue
    )

    suspend fun register(workRequest: WorkRequest)

    suspend fun getPending(): List<WorkRequest>

    suspend fun markAsSynced(
        workRequestId: String
    )

    suspend fun sync(
        workRequest: WorkRequest,
        photoUrls: List<String>
    )

    suspend fun deleteById(id: String)
}