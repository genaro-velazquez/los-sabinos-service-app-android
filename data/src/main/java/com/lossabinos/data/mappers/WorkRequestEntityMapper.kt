package com.lossabinos.data.mappers

import com.lossabinos.data.datasource.local.database.entities.SyncStatusEntity
import com.lossabinos.data.datasource.local.database.entities.UrgencyLevelEntity
import com.lossabinos.data.datasource.local.database.entities.WorkRequestEntity
import com.lossabinos.data.datasource.local.database.enums.ConceptCategoryTypeEntity
import com.lossabinos.data.datasource.local.database.enums.IssueCategoryEntity
import com.lossabinos.domain.enums.ConceptCategoryType
import com.lossabinos.domain.enums.IssueCategoryType
import com.lossabinos.domain.enums.SyncStatus
import com.lossabinos.domain.enums.UrgencyLevelType
import com.lossabinos.domain.valueobjects.WorkRequest
import javax.inject.Inject

class WorkRequestEntityMapper @Inject constructor(

) {
    fun toEntity(domain: WorkRequest): WorkRequestEntity {
        return WorkRequestEntity(
            id = domain.id,
            workOrderId = domain.workOrderId,
            serviceExecutionId = domain.serviceExecutionId,
            title = domain.title,
            description = domain.description,
            findings = domain.findings,
            justification = domain.justification,
            photoUrls = domain.photoUls,
            requestType = domain.requestType,
            requiresCustomerApproval = domain.requiresCustomerApproval,
            urgency = UrgencyLevelEntity.valueOf(domain.urgency.name),
            createdAt = domain.createdAt,
            vehicleId = domain.vehicleId,
            syncStatus = SyncStatusEntity.valueOf(domain.syncStatus.name),
            issueCategory = IssueCategoryEntity.valueOf(domain.issueCategory.name),
            conceptCategory = ConceptCategoryTypeEntity.valueOf(domain.conceptCategory.name)
        )
    }

    fun toDomain(entity: WorkRequestEntity): WorkRequest {
        return WorkRequest(
            id = entity.id,
            workOrderId = entity.workOrderId,
            serviceExecutionId = entity.serviceExecutionId,
            title = entity.title,
            description = entity.description,
            findings = entity.findings,
            justification = entity.justification,
            photoUls = entity.photoUrls,
            requestType = entity.requestType,
            requiresCustomerApproval = entity.requiresCustomerApproval,
            urgency = UrgencyLevelType.valueOf(entity.urgency.name),
            createdAt = entity.createdAt,
            vehicleId = entity.vehicleId,
            syncStatus = SyncStatus.valueOf(entity.syncStatus.name),
            issueCategory = IssueCategoryType.valueOf(entity.issueCategory.name),
            conceptCategory = ConceptCategoryType.valueOf(entity.conceptCategory.name)
        )
    }
}