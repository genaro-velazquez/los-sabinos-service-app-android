package com.lossabinos.serviceapp.models.ui

data class HomeMetricsUiModel(
    val totalServices: Int,
    val pendingServices: Int,
    val inProgressServices: Int,
    val completedServices: Int,
    val efficiencyPercentage: String
)