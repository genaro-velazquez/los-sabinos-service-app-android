package com.lossabinos.data.repositories

import com.lossabinos.data.datasource.local.MechanicsLocalDataSource
import com.lossabinos.data.datasource.remoto.MechanicsRemoteDataSource
import com.lossabinos.domain.entities.AssignedService
import com.lossabinos.domain.entities.Mechanic
import com.lossabinos.domain.entities.ServiceType
import com.lossabinos.domain.entities.Vehicle
import com.lossabinos.domain.repositories.MechanicsRepository
import com.lossabinos.domain.responses.AssignedServicesResponse
import com.lossabinos.domain.responses.DetailedServiceResponse
import com.lossabinos.domain.responses.InitialDataResponse
import com.lossabinos.domain.valueobjects.AssignedServiceProgress
import com.lossabinos.domain.valueobjects.SyncMetadata
import kotlinx.coroutines.flow.Flow

class MechanicsRetrofitRepository(
    private val remoteDataSource: MechanicsRemoteDataSource,
    private val localDataSource: MechanicsLocalDataSource
) : MechanicsRepository {

    override suspend fun assignedServices(): AssignedServicesResponse {
        val dto = remoteDataSource.assignedServices()
        return dto.toEntity()
    }

    override suspend fun detailedService(idService: String): DetailedServiceResponse {
        val dto = remoteDataSource.detailedService(idService = idService)
        return dto.toEntity()
    }

    override suspend fun syncInitialData(): InitialDataResponse {
        val dto = remoteDataSource.syncInitialData()
        val domain = dto.toEntity()

        try {
            localDataSource.saveToRoom(data = domain)
        }catch (e: Exception){
            println("❌ [REPO] Error guardando datos locales: ${e.message}")
            e.stackTrace
        }

        return domain
    }

    override suspend fun getLocalInitialData(): InitialDataResponse {
        return localDataSource.getLocalInitialData()
    }

    override suspend fun getLocalMechanic(): Mechanic? {
        return localDataSource.getLocalMechanic()
    }

    override suspend fun getLocalAssignedServices(): List<AssignedService> {
        return localDataSource.getAllAssignedServicesOnce()
    }

    override suspend fun getLocalServiceTypes(): List<ServiceType> {
        return localDataSource.getLocalServiceTypes()
    }

    // ============================================================
    // 3️⃣ ACTUALIZAR PROGRESO
    // ============================================================
    override suspend fun updateServiceProgress(
        serviceId: String,
        completedActivities: Int,
        totalActivities: Int
    ) {
        try {
            localDataSource.updateServiceProgress(
                serviceId = serviceId,
                completedActivities = completedActivities,
                totalActivities = totalActivities
            )
            println("✅ [REPO] Progreso actualizado para servicio: $serviceId")
        }
        catch (e: Exception){
            println("❌ [REPO] Error actualizando progreso: ${e.message}")
            throw  e
        }
    }

    // ============================================================
    // 1️⃣ MECHANIC FLOW
    // ============================================================
    override fun getMechanicFlow(): Flow<Mechanic?> {
        return localDataSource.getMechanicFlow()
    }

    // ============================================================
    // 2️⃣ ASSIGNED SERVICES FLOW
    // ============================================================
    override fun getAssignedServicesFlow(): Flow<List<AssignedServiceProgress>> {
        return localDataSource.assignedServicesFlow()
    }

    // ============================================================
    // 3️⃣ SERVICE TYPES FLOW
    // ============================================================
    override fun getServiceTypesFlow(): Flow<List<ServiceType>> {
        return localDataSource.getServiceTypesFlow()
    }

    // ============================================================
    // 4️⃣ SYNC METADATA FLOW
    // ============================================================
    override fun getSyncMetadataFlow(): Flow<SyncMetadata?> {
        return localDataSource.getSyncMetadataFlow()
    }

    override suspend fun getQRVehicle(vehicleId: String): Vehicle {
        val dto = remoteDataSource.getQRVehicle(vehicleId = vehicleId)
        return dto.toEntity()
    }
}