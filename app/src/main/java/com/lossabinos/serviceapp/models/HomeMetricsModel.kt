package com.lossabinos.serviceapp.models

data class HomeMetricsModel(
    val totalServices: Int,
    val pendingServices: Int,
    val inProgressServices: Int,
    val completedServices: Int,
    val efficiencyPercentage: String
)
