package com.lossabinos.data.datasource.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_metadata")
data class SyncMetadataEntity(
    @PrimaryKey
    val id: String = "sync_metadata",  // Siempre mismo ID (solo 1 registro)
    val serverTimestamp: String,
    val totalServices: Int,
    val pendingServices: Int,
    val inProgressServices: Int,
    val lastSync: String?,
    val updatedAt: String  // Cuándo se actualizó localmente
)
