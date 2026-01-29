package com.lossabinos.domain.repositories

import com.lossabinos.domain.valueobjects.WorkRequest

interface WorkRequestRepository{
    suspend fun createWorkRequest(request: WorkRequest)
}