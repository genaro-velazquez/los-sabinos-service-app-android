package com.lossabinos.domain.repositories

import com.lossabinos.domain.entities.AssignedService
import com.lossabinos.domain.entities.Mechanic
import com.lossabinos.domain.entities.ServiceType
import com.lossabinos.domain.entities.Vehicle
import com.lossabinos.domain.responses.AssignedServicesResponse
import com.lossabinos.domain.responses.DetailedServiceResponse
import com.lossabinos.domain.responses.InitialDataResponse
import com.lossabinos.domain.valueobjects.SyncMetadata
import kotlinx.coroutines.flow.Flow

interface MechanicsRepository {

    suspend fun assignedServices(): AssignedServicesResponse

    suspend fun detailedService(idService:String): DetailedServiceResponse

    suspend fun syncInitialData(): InitialDataResponse

    // esto se me hace que no se usará
    suspend fun getLocalMechanic(): Mechanic?
    suspend fun getLocalAssignedServices(): List<AssignedService>
    suspend fun getLocalServiceTypes(): List<ServiceType>

    suspend fun getLocalInitialData(): InitialDataResponse

    // Room Flows (leyendo datos locales)
    fun getMechanicFlow(): Flow<Mechanic?>
    fun getAssignedServicesFlow(): Flow<List<AssignedService>>
    fun getServiceTypesFlow(): Flow<List<ServiceType>>
    fun getSyncMetadataFlow(): Flow<SyncMetadata?>

    suspend fun getQRVehicle(
        vehicleId:String
    ): Vehicle

}