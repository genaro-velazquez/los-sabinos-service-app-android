package com.lossabinos.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey
    val id: String,
    val licensePlate: String,
    val modelId: String,
    val makeName: String,
    val modelName: String
)
