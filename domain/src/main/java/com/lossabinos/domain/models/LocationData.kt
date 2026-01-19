package com.lossabinos.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class LocationData(
    val userId: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
)
