package com.lossabinos.domain.usecases.workrequest

import com.lossabinos.domain.repositories.WorkRequestRepository
import com.lossabinos.domain.valueobjects.WorkRequest

class CreateWorkRequestUseCase(
    private val repository: WorkRequestRepository
) {
    suspend operator fun invoke (
      workRequest: WorkRequest
    ) {
        repository.createWorkRequest(request = workRequest)
    }
}