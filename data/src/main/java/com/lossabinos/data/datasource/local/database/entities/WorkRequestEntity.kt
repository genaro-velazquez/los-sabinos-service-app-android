package com.lossabinos.data.datasource.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// ═══════════════════════════════════════════════════════
// WORK REQUEST ENTITY
// ═══════════════════════════════════════════════════════
@Entity(
    tableName = "work_request",
    indices = [
        Index("workOrderId"),
        Index("syncStatus")
    ]
)
data class WorkRequestEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val workOrderId: String,  // FK - El workOrder donde se reporta
    val title: String,  // "Balatas dañadas"
    val description: String,  // Descripción detallada
    val findings: String,  // Hallazgos específicos
    val justification: String,  // Justificación
    val photoUrls: List<String> ,  // JSON serializado: ["path1", "path2"]
    val requestType: String,  // ADDITIONAL_WORK, PART_REPLACEMENT, etc.
    val requiresCustomerApproval: Boolean = true,
    val urgency: UrgencyLevelEntity,  // LOW, NORMAL, HIGH, CRITICAL
    val createdAt: Long = System.currentTimeMillis(),
    val vehicleId: String,
    val syncStatus: SyncStatusEntity  // PENDING, SYNCED, ERROR
)

