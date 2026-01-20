package com.lossabinos.data.repositories

import com.lossabinos.data.datasource.local.LocationWebSocketDataSource
import com.lossabinos.domain.models.Location
import com.lossabinos.domain.models.LocationEvent
import com.lossabinos.domain.repositories.LocationSocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationSocketRepositoryImpl @Inject constructor(
    private val dataSource: LocationWebSocketDataSource
) : LocationSocketRepository {

    override fun connect() = dataSource.connect()

    override fun disconnect() = dataSource.disconnect()

    override fun observeLocation(): Flow<LocationEvent> =
        dataSource.events()

    override fun sendLocation(location: Location) = 
        dataSource.send(location)
}
