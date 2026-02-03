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

fun WorkRequestEntity.toDomain(): WorkRequest =
    WorkRequest(
        id = id,
        workOrderId = workOrderId,
        title = title,
        description = description,
        findings = findings,
        justification = justification,
        photoUls = photoUrls,
        requestType = requestType,
        requiresCustomerApproval = requiresCustomerApproval,
        urgency = UrgencyLevelType.valueOf(urgency.name),
        createdAt = createdAt,
        vehicleId = vehicleId,
        syncStatus = SyncStatus.valueOf(syncStatus.name),
        issueCategory = IssueCategoryType.valueOf(issueCategory.name),
        conceptCategory = ConceptCategoryType.valueOf(conceptCategory.name)
    )

fun WorkRequest.toEntity() : WorkRequestEntity =
    WorkRequestEntity(
        id = id,
        workOrderId = workOrderId,
        title = title,
        description = description,
        findings = findings,
        justification = justification,
        photoUrls = photoUls,
        requestType = requestType,
        requiresCustomerApproval = requiresCustomerApproval,
        urgency = UrgencyLevelEntity.valueOf(urgency.name),
        createdAt = createdAt,
        vehicleId = vehicleId,
        syncStatus = SyncStatusEntity.valueOf(syncStatus.name),
        issueCategory = IssueCategoryEntity.valueOf(issueCategory.name),
        conceptCategory = ConceptCategoryTypeEntity.valueOf(conceptCategory.name)
    )