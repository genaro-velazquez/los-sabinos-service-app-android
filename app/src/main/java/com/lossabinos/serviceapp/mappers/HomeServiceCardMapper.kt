package com.lossabinos.serviceapp.mappers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import com.lossabinos.domain.enums.ServiceStatus
import com.lossabinos.domain.valueobjects.AssignedServiceProgress
import com.lossabinos.serviceapp.models.ui.ServiceCardUiModel
import com.lossabinos.serviceapp.utils.StatusColor

fun AssignedServiceProgress.toServiceCardUiModel(
    serviceTypeName: String,
    statusColor: StatusColor
): ServiceCardUiModel {

    val syncStatusText =
        if (serviceStatus == ServiceStatus.COMPLETED) {
            syncStatus.name
        } else {
            ""
        }

    return ServiceCardUiModel(
        id = assignedService.id,
        executionId = assignedService.id,
        title = serviceTypeName,
        clientName = "Cliente",
        icon = Icons.Filled.Build,
        status = serviceStatus.name.replaceFirstChar { it.uppercase() },
        statusBackgroundColor = statusColor.backgroundColor,
        statusTextColor = statusColor.textColor,
        startTime = assignedService.scheduledStart,
        endTime = assignedService.scheduledEnd,
        duration = "N/A",
        address = "N/A",
        priority = assignedService.priority.replaceFirstChar { it.uppercase() },
        note = "",
        syncStatus = syncStatusText,
        serviceStatus = serviceStatus
    )
}
