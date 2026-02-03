package com.lossabinos.domain.valueobjects

fun WorkRequest.withPhotoUrls(
    urls: List<String>
): WorkRequest =
    WorkRequest(
        id = id,
        workOrderId = workOrderId,
        title = title,
        description = description,
        findings = findings,
        justification = justification,
        photoUls = urls,
        requestType = requestType,
        requiresCustomerApproval = requiresCustomerApproval,
        urgency = urgency,
        createdAt = createdAt,
        vehicleId = vehicleId,
        syncStatus = syncStatus,
        issueCategory = issueCategory,
        conceptCategory = conceptCategory
    )
