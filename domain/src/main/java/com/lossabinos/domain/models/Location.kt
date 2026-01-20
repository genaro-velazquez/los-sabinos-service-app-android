package com.lossabinos.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val userId: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis()
)
