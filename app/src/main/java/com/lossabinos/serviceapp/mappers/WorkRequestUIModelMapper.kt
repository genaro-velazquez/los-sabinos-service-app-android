package com.lossabinos.serviceapp.mappers

import com.lossabinos.domain.enums.SyncStatus
import com.lossabinos.domain.enums.UrgencyLevel
import com.lossabinos.domain.valueobjects.WorkRequest
import com.lossabinos.serviceapp.models.ui.WorkRequestUIModel
import java.util.UUID

fun WorkRequestUIModel.toDomain(
    workOrderId: String,
    vehicleId: String
): WorkRequest {
    return WorkRequest(
        id = UUID.randomUUID().toString(),
        workOrderId = workOrderId,
        title = title,
        description = description,
        findings = findings,
        justification = justification,
        photoUls = emptyList(), // luego se puede extender
        requestType = "MANUAL",
        requiresCustomerApproval = requiresCustomerApproval,
        urgency = UrgencyLevel.valueOf(urgency.name),
        createdAt = System.currentTimeMillis(),
        vehicleId = vehicleId,
        syncStatus = SyncStatus.PENDING
    )
}
