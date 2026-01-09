package com.lossabinos.serviceapp.mappers

import com.lossabinos.domain.enums.ServiceStatus
import com.lossabinos.domain.valueobjects.AssignedServiceProgress
import com.lossabinos.serviceapp.models.HomeMetricsModel

fun List<AssignedServiceProgress>.toHomeMetrics(): HomeMetricsModel {
    val total = size
    val pending = count { it.serviceStatus == ServiceStatus.PENDING }
    val inProgress = count { it.serviceStatus == ServiceStatus.IN_PROGRESS }
    val completed = count { it.serviceStatus == ServiceStatus.COMPLETED }

    val efficiency =
        if (total > 0) {
            "%.0f".format(inProgress.toDouble() / total * 100)
        } else {
            "0"
        }

    return HomeMetricsModel(
        totalServices = total,
        pendingServices = pending,
        inProgressServices = inProgress,
        completedServices = completed,
        efficiencyPercentage = efficiency
    )
}

