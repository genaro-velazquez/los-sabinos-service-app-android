package com.lossabinos.domain.repositories

import com.lossabinos.domain.valueobjects.WorkRequest
import com.lossabinos.domain.valueobjects.WorkRequestIssue

interface WorkRequestRepository{
    suspend fun createWorkRequest(request: WorkRequest)

    suspend fun createWorkRequestIssue(
        serviceExecutionId: String,
        issue: WorkRequestIssue
    )
}