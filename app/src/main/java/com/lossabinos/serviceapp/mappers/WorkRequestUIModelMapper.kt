package com.lossabinos.serviceapp.mappers

import com.lossabinos.domain.enums.ConceptCategoryType
import com.lossabinos.domain.enums.IssueCategoryType
import com.lossabinos.domain.enums.SyncStatus
import com.lossabinos.domain.enums.UrgencyLevelType
import com.lossabinos.domain.valueobjects.WorkRequest
import com.lossabinos.serviceapp.models.ui.WorkRequestUIModel

fun WorkRequestUIModel.toDomain(
    id: String,
    workOrderId: String,
    serviceExecutionId: String,
    vehicleId: String
): WorkRequest {
    return WorkRequest(
        id = id,
        workOrderId = workOrderId,
        serviceExecutionId = serviceExecutionId,
        title = title,
        description = description,
        findings = findings,
        justification = justification,
        photoUls = emptyList(), // luego se puede extender
        requestType = "MANUAL",
        requiresCustomerApproval = requiresCustomerApproval,
        urgency = UrgencyLevelType.valueOf(urgency.name),
        createdAt = System.currentTimeMillis(),
        vehicleId = vehicleId,
        syncStatus = SyncStatus.PENDING,
        issueCategory = IssueCategoryType.valueOf(issueCategory.name),
        conceptCategory = ConceptCategoryType.valueOf(conceptCategory.name)
    )
}
