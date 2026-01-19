package com.lossabinos.domain.repositories

import com.lossabinos.domain.models.Location
import com.lossabinos.domain.models.LocationEvent
import kotlinx.coroutines.flow.Flow

interface LocationSocketRepository {
    fun connect()
    fun disconnect()
    fun observeLocation(): Flow<LocationEvent>
    fun sendLocation(location: Location)
}
