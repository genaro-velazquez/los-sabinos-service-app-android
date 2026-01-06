package com.lossabinos.data.repositories.retrofit.mechanics

import com.lossabinos.data.dto.entities.VehicleDTO
import com.lossabinos.data.dto.repositories.retrofit.RetrofitResponseValidator
import com.lossabinos.data.dto.responses.AssignedServicesResponseDTO
import com.lossabinos.data.dto.responses.DetailedServiceResponseDTO
import com.lossabinos.data.dto.responses.InitialDataResponseDTO
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.local.database.dao.InitialDataDao
import com.lossabinos.data.local.database.entities.AssignedServiceEntity
import com.lossabinos.data.local.database.entities.MechanicEntity
import com.lossabinos.data.local.database.entities.ServiceProgressEntity
import com.lossabinos.data.local.database.entities.ServiceTypeEntity
import com.lossabinos.data.local.database.entities.SyncMetadataEntity
import com.lossabinos.data.local.mappers.toDomain
import com.lossabinos.data.local.mappers.toRoomEntity
import com.lossabinos.domain.entities.AssignedService
import com.lossabinos.domain.entities.Mechanic
import com.lossabinos.domain.entities.ServiceType
import com.lossabinos.domain.entities.Vehicle
import com.lossabinos.domain.repositories.MechanicsRepository
import com.lossabinos.domain.responses.AssignedServicesResponse
import com.lossabinos.domain.responses.DetailedServiceResponse
import com.lossabinos.domain.responses.InitialDataResponse
import com.lossabinos.domain.valueobjects.AssignedServiceProgress
import com.lossabinos.domain.valueobjects.ChecklistTemplate
import com.lossabinos.domain.valueobjects.SyncMetadata
import com.lossabinos.domain.valueobjects.Template
import com.lossabinos.domain.valueobjects.VehicleVersion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map


class MechanicsRetrofitRepository(
    private val assignedServices: MechanicsServices,
    private val headersMaker: HeadersMaker,
    private val initialDataDao: InitialDataDao
) : MechanicsRepository {

    override suspend fun assignedServices(): AssignedServicesResponse {
        val response = assignedServices.assignedServices(headers = headersMaker.build())
        val json = RetrofitResponseValidator.validate(response = response)
        val dto  = AssignedServicesResponseDTO(json = json)
        return dto.toEntity()
    }

    override suspend fun detailedService(idService: String): DetailedServiceResponse {
        val response = assignedServices.detailedService(headers = headersMaker.build(), idService = idService)
        val json = RetrofitResponseValidator.validate(response = response)
        val dto = DetailedServiceResponseDTO(json = json)
        return dto.toEntity()
    }

    override suspend fun syncInitialData(): InitialDataResponse {
        val response = assignedServices.syncInitialData(headers = headersMaker.build())
        val json = RetrofitResponseValidator.validate(response = response)
        val dto = InitialDataResponseDTO(json = json)
        //return dto.toEntity()
        println("✅ [API] DTO Response recibido")
        val initialDataResponse = dto.toEntity()
        println("✅ [API] DTO to Entity")
        // GUARDAR EN ROOM (nuevo)
        println("✅ [API] Salvando en bd local")
        saveToRoom(initialDataResponse)
        println("✅ [API] Información guardada en bd local")
        // DEVOLVER DATOS
        return initialDataResponse

    }

    // ✨ NUEVO: Método para guardar en Room
    private suspend fun saveToRoom(data: InitialDataResponse) {
        try {
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
        } catch (e: Exception) {
            println("❌ Error al guardar en Room: ${e.message}")
            throw e
        }
    }

    // Carga infrmacion de carga inicial
    override suspend fun getLocalInitialData(): InitialDataResponse {
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
        }

        // Traer tipos de servicio
        val serviceTypeEntities = initialDataDao.getAllServiceTypes()
        val serviceTypes = serviceTypeEntities.map { entity ->
            ServiceType(
                id = entity.id,
                name = entity.name,
                code = "",
                category = "",
                estimatedDurationMinutes = entity.estimatedDurationMinutes
            )
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
    override suspend fun getLocalMechanic(): Mechanic? {
        val mechanicEntity = initialDataDao.getMechanicOnce()
        return mechanicEntity?.let {
            Mechanic(
                id = it.id,
                name = it.name,
                email = it.email,
                fullName = "",
                rol = "",
                zoneId = "",
                zoneName = ""
            )
        }
    }

    override suspend fun getLocalAssignedServices(): List<AssignedService> {
        val entities = initialDataDao.getAllAssignedServicesOnce()
        return entities.map { entity ->
            AssignedService(
                id = entity.id,
                workOrderId = entity.workOrderId,
                workOrderNumber = "",
                serviceTypeId = entity.serviceTypeId,
                serviceTypeName = "",
                vehicle = Vehicle(id = "", vin = "", economicNumber = "", modelName = "", vehicleNumber ="", licensePlate = "", vehicleVersion = VehicleVersion(make = "", model = "", year = 0), currentOdometerKm = 0),
                status = entity.status,
                priority = entity.priority,
                scheduledStart = entity.scheduledStart ?: "",
                scheduledEnd = entity.scheduledEnd ?: "",
                checklistTemplate = ChecklistTemplate(
                    name = "",
                    version = "",
                    template = Template(name = "", sections = emptyList(), serviceFields = emptyList()))
                //template = Template(name = "", sections = emptyList(), serviceFields = emptyList())
            )
        }
    }

    override suspend fun getLocalServiceTypes(): List<ServiceType> {
        val entities = initialDataDao.getAllServiceTypes()
        return entities.map { entity ->
            ServiceType(
                id = entity.id,
                name = entity.name,
                code = "",
                category = "",
                estimatedDurationMinutes = entity.estimatedDurationMinutes
            )
        }
    }

    // ============================================================
    // 1️⃣ MECHANIC FLOW
    // ============================================================
    override fun getMechanicFlow(): Flow<Mechanic?> {
        return initialDataDao.getMechanicFlow()
            .map { mechanicEntity ->
                mechanicEntity?.toDomain()
            }
            .catch { exception ->
                println("❌ [REPO] Error en MechanicFlow: ${exception.message}")
                throw exception
            }
    }

    // ============================================================
    // 2️⃣ ASSIGNED SERVICES FLOW
    // ============================================================
    override fun getAssignedServicesFlow(): Flow<List<AssignedServiceProgress>> {

        return initialDataDao.getAllAssignedServicesWithProgressFlow()
            .map { entitiesWithProgress ->
                entitiesWithProgress.map { item ->
                    item.toDomain()
                }
            }
            .catch { exception ->
                println("❌ [REPO] Error en getAssignedServicesFlow: ${exception.message}")
                throw exception
            }

/*
        return initialDataDao.getAllAssignedServicesFlow()
            .map { serviceEntities ->
                serviceEntities.map { entity ->
                    entity.toDomain()
                }
            }
            .catch { exception ->
                println("❌ [REPO] Error en AssignedServicesFlow: ${exception.message}")
                throw exception
            }
 */
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

            val progress = ServiceProgressEntity(
                assignedServiceId = serviceId,
                completedActivities = completedActivities,
                totalActivities = totalActivities,
                completedPercentage = completedPercentage,
                status = status,
                syncStatus = "PENDING"
            )

            initialDataDao.insertServiceProgress(progress)
            println("✅ Progreso actualizado para servicio: $serviceId")
        }
        catch (e: Exception){
            println("❌ Error actualizando progreso: ${e.message}")
            throw e
        }
    }


    // ============================================================
    // 3️⃣ SERVICE TYPES FLOW
    // ============================================================
    override fun getServiceTypesFlow(): Flow<List<ServiceType>> {
        return initialDataDao.getAllServiceTypesFlow()
            .map { typeEntities ->
                typeEntities.map { entity ->
                    entity.toDomain()
                }
            }
            .catch { exception ->
                println("❌ [REPO] Error en ServiceTypesFlow: ${exception.message}")
                throw exception
            }
    }

    // ============================================================
    // 4️⃣ SYNC METADATA FLOW
    // ============================================================
    override fun getSyncMetadataFlow(): Flow<SyncMetadata?> {
        return initialDataDao.getSyncMetadataFlow()
            .map { metadataEntity ->
                metadataEntity?.toDomain()
            }
            .catch { exception ->
                println("❌ [REPO] Error en SyncMetadataFlow: ${exception.message}")
                throw exception
            }
    }

    override suspend fun getQRVehicle(
        vehicleId: String
    ): Vehicle {
        val response = assignedServices.getVehicleByQR(headers = headersMaker.build(), idVehicle = vehicleId)
        val json = RetrofitResponseValidator.validate(response = response)
        val dto = VehicleDTO(json = json)
        return dto.toEntity()
    }

}