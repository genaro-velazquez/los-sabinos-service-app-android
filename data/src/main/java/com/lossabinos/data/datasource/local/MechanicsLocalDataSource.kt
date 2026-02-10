package com.lossabinos.data.datasource.local

import com.lossabinos.data.datasource.local.database.dao.InitialDataDao
import com.lossabinos.data.datasource.local.database.entities.ServiceProgressEntity
import com.lossabinos.data.mappers.toDomain
import com.lossabinos.data.mappers.toRoomEntity
import com.lossabinos.domain.entities.AssignedService
import com.lossabinos.domain.entities.Mechanic
import com.lossabinos.domain.entities.ServiceType
import com.lossabinos.domain.responses.InitialDataResponse
import com.lossabinos.domain.valueobjects.AssignedServiceProgress
import com.lossabinos.domain.valueobjects.SyncMetadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MechanicsLocalDataSource @Inject constructor(
    private val initialDataDao: InitialDataDao
){
    suspend fun saveToRoom(data: InitialDataResponse) {
        try {
            // 1️⃣ Guardar Mechanic
            initialDataDao.insertMechanics(
                mechanics = listOf(data.mechanic.toRoomEntity())
            )

            // 2️⃣ Guardar ServiceTypes
            initialDataDao.insertServiceTypes(
                serviceTypes = data.serviceTypes.map { it.toRoomEntity() }
            )

            // 3️⃣ 🧠 SINCRONIZAR AssignedServices (insert / update / delete)

            // IDs que vienen del backend
            val remoteIds = data.assignedServices.map { it.id }.toSet()

            // IDs que existen en Room
            val localIds = initialDataDao.getAllAssignedServiceIds().toSet()

            // ❌ Servicios que ya NO existen en backend
            val idsToDelete = localIds - remoteIds

            if (idsToDelete.isNotEmpty()) {
                println("🗑️ [ROOM] Eliminando servicios obsoletos: $idsToDelete")
                initialDataDao.deleteAssignedServicesByIds(idsToDelete.toList())
            }

            // Insertar o actualizar los que sí vienen
            data.assignedServices.forEach { serviceDto ->
                val existing =
                    initialDataDao.getAssignedServiceById(serviceDto.id)

                if (existing == null) {
                    println("✅ [ROOM] Insertando servicio nuevo: ${serviceDto.id}")
                    initialDataDao.insertAssignedServices(
                        listOf(serviceDto.toRoomEntity())
                    )
                } else {
                    println("🔄 [ROOM] Actualizando servicio existente: ${serviceDto.id}")
                    initialDataDao.updateAssignedService(
                        serviceDto.toRoomEntity()
                    )
                }
            }

            // 4️⃣ Guardar SyncMetadata
            initialDataDao.insertSyncMetadata(
                syncMetadata = data.syncMetadata.toRoomEntity()
            )

            println("✅ Datos guardados en Room exitosamente")

            /*
                        // Guardar Mechanic (solo uno)
                        initialDataDao.insertMechanics(
                            mechanics = listOf(data.mechanic.toRoomEntity())
                        )

                        // Guardar ServiceTypes
                        initialDataDao.insertServiceTypes(
                            serviceTypes = data.serviceTypes.map { it.toRoomEntity() }
                        )

                        // Guardar AssignedServices
                        //initialDataDao.insertAssignedServices(
                        //    assignedServices = data.assignedServices.map { it.toRoomEntity() }
                        //)

                        data.assignedServices.forEach { serviceDto ->
                            val existing = initialDataDao.getAssignedServiceById(serviceDto.id)

                            if (existing == null) {
                                // ✅ No existe → Insertar (nuevo servicio)
                                println("✅ [ROOM] Insertando servicio nuevo: ${serviceDto.id}")
                                initialDataDao.insertAssignedServices(
                                    listOf(serviceDto.toRoomEntity())
                                )
                            } else {
                                // ✅ Existe → Actualizar (cambios del servidor, sin tocar BD local)
                                println("✅ [ROOM] Actualizando servicio existente: ${serviceDto.id}")
                                initialDataDao.updateAssignedService(
                                    serviceDto.toRoomEntity()
                                )
                            }
                        }

                        // Guardar AsyncMetadata
                        initialDataDao.insertSyncMetadata(
                            syncMetadata = data.syncMetadata.toRoomEntity()
                        )

                        // Log para debugging
                        println("✅ Datos guardados en Room exitosamente")

             */
        } catch (e: Exception) {
            println("❌ Error al guardar en Room: ${e.message}")
            throw e
        }
    }

    suspend fun getLocalInitialData(): InitialDataResponse {
        // Traer mecánico
        val mechanicEntity = initialDataDao.getMechanicOnce()
        val mechanic = mechanicEntity?.let {
            Mechanic(
                id = it.id,
                name = it.name,
                email = it.email,
                fullName = "",
                rol = "",
                zoneId = it.zoneId,
                zoneName = it.zoneName
            )
        } ?: throw Exception("Mecánico no encontrado")

        // Traer servicios asignados
        val serviceEntities = initialDataDao.getAllAssignedServicesOnce()
        val assignedServices = serviceEntities.map { entity ->
            entity.toDomain()
/*
            AssignedService(
                id = entity.id,
                workOrderId = entity.workOrderId,
                workOrderNumber = entity.workOrderNumber,
                serviceTypeId = entity.serviceTypeId,
                serviceTypeName = entity.serviceTypeName,
                vehicle = Vehicle(id = "", vin = "", economicNumber = "", modelName = "", vehicleNumber ="", licensePlate = "", vehicleVersion = VehicleVersion(make = "", model = "", year = 0), currentOdometerKm = 0),
                status = entity.status,
                priority = entity.priority,
                scheduledStart = entity.scheduledStart ?: "",
                scheduledEnd = entity.scheduledEnd ?: "",
                checklistTemplate = ChecklistTemplate(name = "", version = "", template = Template(name = "", sections = emptyList(), serviceFields = emptyList()))
                //template = Template(name = "", sections = emptyList(), serviceFields = emptyList())
            )
 */
        }

        // Traer tipos de servicio
        val serviceTypeEntities = initialDataDao.getAllServiceTypes()
        val serviceTypes = serviceTypeEntities.map { entity ->
            entity.toDomain()
/*
            ServiceType(
                id = entity.id,
                name = entity.name,
                code = "",
                category = "",
                estimatedDurationMinutes = entity.estimatedDurationMinutes
            )
 */
        }

        // Consolidar metadatos
        val syncMetadata = SyncMetadata(
            totalServices = assignedServices.size,
            pendingServices = assignedServices.count { it.status == "pending" },
            inProgressServices = assignedServices.count { it.status == "in_progress" },
            serverTimestamp = "",
            lastSync = ""
        )

        return InitialDataResponse(
            mechanic = mechanic,
            assignedServices = assignedServices,
            serviceTypes = serviceTypes,
            syncMetadata = syncMetadata
        )
    }

    // Estos se me hace que no se cuparan
    suspend fun getLocalMechanic(): Mechanic? {
        val mechanicEntity = initialDataDao.getMechanicOnce()
        return mechanicEntity?.let { mechanicEntity ->
            mechanicEntity.toDomain()
/*
            Mechanic(
                id = it.id,
                name = it.name,
                email = it.email,
                fullName = "",
                rol = "",
                zoneId = "",
                zoneName = ""
            )
 */
        }
    }

    suspend fun getLocalServiceTypes():List<ServiceType> {
        val entities = initialDataDao.getAllServiceTypes()
        return entities.map { entity ->
            entity.toDomain()
/*
            ServiceType(
                id = entity.id,
                name = entity.name,
                code = "",
                category = "",
                estimatedDurationMinutes = entity.estimatedDurationMinutes
            )
 */
        }
    }

    suspend fun getAllAssignedServicesOnce(): List<AssignedService> =
        initialDataDao.getAllAssignedServicesOnce().map { it.toDomain() }

    suspend fun updateServiceProgress(
        serviceId: String,
        completedActivities: Int,
        totalActivities: Int
    ){
        val completedPercentage = if (totalActivities > 0) {
            (completedActivities * 100) / totalActivities
        } else {
            0
        }

        val status = when {
            totalActivities == 0 -> "pending"
            completedActivities == totalActivities && totalActivities > 0 -> "completed"
            completedActivities > 0 -> "in_progress"
            else -> "pending"
        }

        val progressEntity = ServiceProgressEntity(
            assignedServiceId = serviceId,
            completedActivities = completedActivities,
            totalActivities = totalActivities,
            completedPercentage = completedPercentage,
            status = status,
            syncStatus = "PENDING"
        )

        initialDataDao.insertServiceProgress(progressEntity)
    }

    //**********
    // FLOWS
    //**********
    fun assignedServicesFlow(): Flow<List<AssignedServiceProgress>> =
        initialDataDao.getAllAssignedServicesWithProgressFlow().map { list ->
            list.map { it.toDomain() }
        }

    fun getMechanicFlow(): Flow<Mechanic?> =
        initialDataDao.getMechanicFlow().map { mechanicEntity ->
            mechanicEntity?.toDomain()
        }

    fun getServiceTypesFlow(): Flow<List<ServiceType>> =
        initialDataDao.getAllServiceTypesFlow().map { list ->
            list.map { it.toDomain() }
        }

    fun getSyncMetadataFlow(): Flow<SyncMetadata?> =
        initialDataDao.getSyncMetadataFlow().map { metadataEntity -> metadataEntity?.toDomain() }

}