package com.lossabinos.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zones")
data class ZoneEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val region: String? = null
)