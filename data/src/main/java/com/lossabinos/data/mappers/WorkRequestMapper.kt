package com.lossabinos.data.mappers

import com.lossabinos.data.datasource.local.database.entities.SyncStatusEntity
import com.lossabinos.data.datasource.local.database.entities.UrgencyLevelEntity
import com.lossabinos.data.datasource.local.database.entities.WorkOrderEntity
import com.lossabinos.data.datasource.local.database.entities.WorkRequestEntity
import com.lossabinos.domain.enums.SyncStatus
import com.lossabinos.domain.enums.UrgencyLevel
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
        urgency = UrgencyLevel.valueOf(urgency.name),
        createdAt = createdAt,
        vehicleId = vehicleId,
        syncStatus = SyncStatus.valueOf(syncStatus.name)
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
        syncStatus = SyncStatusEntity.valueOf(syncStatus.name)
    )