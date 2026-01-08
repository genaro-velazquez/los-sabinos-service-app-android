package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.repositories.ChecklistRepository
import com.lossabinos.domain.enums.ServiceStatus
import com.lossabinos.domain.enums.SyncStatus

class SaveServiceProgressUseCase(
    private val checklistRepository: ChecklistRepository
) {
    suspend operator fun invoke(
        assignedServiceId: String,
        completedActivities: Int,
        totalActivities: Int,
        completedPercentage: Int,
        status: ServiceStatus,
        lastUpdatedAt: Long,
        syncStatus: SyncStatus
    ){
        checklistRepository.saveServiceProgress(
            assignedServiceId = assignedServiceId,
            completedActivities = completedActivities,
            totalActivities = totalActivities,
            completedPercentage = completedPercentage,
            status = status,
            lastUpdatedAt = lastUpdatedAt,
            syncStatus = syncStatus
        )
    }

}