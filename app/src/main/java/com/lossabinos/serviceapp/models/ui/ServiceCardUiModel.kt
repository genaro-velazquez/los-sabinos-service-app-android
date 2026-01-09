package com.lossabinos.serviceapp.models.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.lossabinos.domain.enums.ServiceStatus

data class ServiceCardUiModel (
    val id: String,
    val executionId:String,
    val icon: ImageVector,
    val title: String,
    val status: String,
    val statusBackgroundColor: Color = Color(0xFFE0E0E0),  // 🆕
    val statusTextColor: Color = Color(0xFF424242),        // 🆕
    val clientName: String,
    val startTime: String,
    val endTime: String,
    val duration: String,
    val address: String,
    val priority: String,
    val note: String,
    val syncStatus: String = "SYNCED",         // "SYNCED", "PENDING", "ERROR"
    val serviceStatus: ServiceStatus = ServiceStatus.PENDING
    //val onCompleteClick: () -> Unit = {},
    //val onRescheduleClick: () -> Unit = {},
    //val onSyncClick: () -> Unit = {}
)