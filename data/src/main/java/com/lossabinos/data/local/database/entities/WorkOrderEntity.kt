package com.lossabinos.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "work_orders"
)
data class WorkOrderEntity(
    @PrimaryKey
    val id: String,
    val vehicleId: String,
    val zoneId: String,
    val createdAt: String,
    val updatedAt: String
)
