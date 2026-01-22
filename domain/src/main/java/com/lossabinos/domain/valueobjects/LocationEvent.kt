package com.lossabinos.domain.valueobjects

sealed class LocationEvent {

    object Idle : LocationEvent()

    object Connecting : LocationEvent()

    object Disconnected : LocationEvent()

    data class LocationUpdate(
        val lat: Double,
        val lng: Double,
        val timestamp: Long
    ) : LocationEvent()

    data class Error(
        val message: String
    ) : LocationEvent()
}
