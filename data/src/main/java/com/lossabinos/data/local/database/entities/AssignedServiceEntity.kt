package com.lossabinos.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "assigned_services"
)
data class AssignedServiceEntity(
    @PrimaryKey
    val id: String,
    val workOrderId: String,
    val serviceTypeId: String,
    val status: String,
    val priority: String,
    val notes: String? = null,
    val scheduledStart: String? = null,
    val scheduledEnd: String? = null
)
