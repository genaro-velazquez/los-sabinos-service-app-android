package com.lossabinos.domain.valueobjects

data class LocationVO(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float? = null,
    val timestamp: Long
)
