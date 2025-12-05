package com.lossabinos.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mechanics")
data class MechanicEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String
)
