package com.lossabinos.data.datasource.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_locations")
data class OfflineLocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jsonMessage: String,
    val createdAt: Long = System.currentTimeMillis()
)
