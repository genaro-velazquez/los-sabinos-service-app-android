package com.lossabinos.data.datasource.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_types")
data class ServiceTypeEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val code: String,
    val category: String,
    val estimatedDurationMinutes: Int
)
