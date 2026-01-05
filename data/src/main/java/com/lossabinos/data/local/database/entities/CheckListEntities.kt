package com.lossabinos.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// ACTIVITY PROGRESS
@Entity(
    tableName = "activity_progress",
    foreignKeys = [
        ForeignKey(
            entity = AssignedServiceEntity::class,
            parentColumns = ["id"],
            childColumns = ["assignedServiceId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ActivityProgressEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val assignedServiceId: String,  // FK (camelCase)
    val sectionIndex: Int,
    val activityIndex: Int,
    val activityDescription: String,
    val requiresEvidence: Boolean,
    val completed: Boolean = false,
    val completedAt: String? = null
)

// ACTIVITY EVIDENCE
@Entity(
    tableName = "activity_evidence",
    foreignKeys = [
        ForeignKey(
            entity = ActivityProgressEntity::class,
            parentColumns = ["id"],
            childColumns = ["activityProgressId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ActivityEvidenceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val activityProgressId: Long,  // FK (camelCase)
    val filePath: String,
    val fileType: String = "image",  // "image" o "video"
    val timestamp: String
)

// OBSERVATION RESPONSE
@Entity(
    tableName = "observation_response",
    foreignKeys = [
        ForeignKey(
            entity = AssignedServiceEntity::class,
            parentColumns = ["id"],
            childColumns = ["assignedServiceId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ObservationResponseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val assignedServiceId: String,  // FK (camelCase)
    val sectionIndex: Int,
    val observationIndex: Int,
    val observationDescription: String,
    val response: String? = null,
    val timestamp: String? = null
)

// OBSERVATION RESPONSE
@Entity(
    tableName = "service_field_value",
    foreignKeys = [
        ForeignKey(
            entity = AssignedServiceEntity::class,
            parentColumns = ["id"],
            childColumns = ["assignedServiceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    // 🆕 Agregar índice único
    indices = [
        Index(
            value = ["assignedServiceId", "fieldLabel"],
            unique = true  // Garantiza que no haya duplicados
        )
    ]
)
data class ServiceFieldValueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val assignedServiceId: String,  // FK (camelCase)
    val fieldIndex: Int,
    val fieldLabel: String,
    val fieldType: String,
    val required: Boolean,
    val value: String? = null,
    val timestamp: String? = null
)

@Entity(tableName = "service_progress")
data class ServiceProgressEntity(
    @PrimaryKey
    val assignedServiceId: String,
    val completedActivities: Int = 0,
    val totalActivities: Int = 0,
    val completedPercentage: Int = 0,
    val status: String, // "pending", "in_progress", "completed"
    val lastUpdatedAt: Long = System.currentTimeMillis(),
    val syncStatus: String = "PENDING" // PENDING, SYNCED, ERROR
)

// Union tablas : assigned_services, service_progress, activity_progress
data class AssignedServiceWithProgressEntity(
    @Embedded
    val assignedService: AssignedServiceEntity,

    @ColumnInfo(name = "completedActivities")
    val completedActivities: Int = 0,

    @ColumnInfo(name = "totalActivities")
    val totalActivities: Int = 0,

    @ColumnInfo(name = "completedPercentage")
    val completedPercentage: Int = 0,

    @ColumnInfo(name = "syncStatus")
    val syncStatus: String = "PENDING",

    @ColumnInfo(name = "activityProgressCount")
    val activityProgressCount: Int = 0,

    @ColumnInfo(name = "completedCount")
    val completedCount: Int = 0
)

