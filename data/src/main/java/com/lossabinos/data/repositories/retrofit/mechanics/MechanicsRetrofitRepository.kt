package com.lossabinos.data.repositories.retrofit.mechanics

import com.lossabinos.data.dto.repositories.retrofit.RetrofitResponseValidator
import com.lossabinos.data.dto.responses.AssignedServicesResponseDTO
import com.lossabinos.data.dto.responses.DetailedServiceResponseDTO
import com.lossabinos.data.dto.responses.InitialDataResponseDTO
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.local.database.dao.InitialDataDao
import com.lossabinos.data.local.mappers.toRoomEntity
import com.lossabinos.domain.entities.AssignedService
import com.lossabinos.domain.entities.Mechanic
import com.lossabinos.domain.entities.ServiceType
import com.lossabinos.domain.entities.Vehicle
import com.lossabinos.domain.repositories.MechanicsRepository
import com.lossabinos.domain.responses.AssignedServicesResponse
import com.lossabinos.domain.responses.DetailedServiceResponse
import com.lossabinos.domain.responses.InitialDataResponse
import com.lossabinos.domain.valueobjects.ChecklistTemplate
import com.lossabinos.domain.valueobjects.SyncMetadata
import com.lossabinos.domain.valueobjects.Template
import com.lossabinos.domain.valueobjects.VehicleVersion


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
        val initialDataResponse = dto.toEntity()
        // GUARDAR EN ROOM (nuevo)
        saveToRoom(initialDataResponse)
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
            initialDataDao.insertAssignedServices(
                assignedServices = data.assignedServices.map { it.toRoomEntity() }
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
        val mechanicEntity = initialDataDao.getMechanic()
        val mechanic = mechanicEntity?.let {
            Mechanic(
                id = it.id,
                name = it.name,
                email = it.email,
                fullName = "",
                rol = "",
                zoneId = "",
                zoneName = ""
            )
        } ?: throw Exception("Mecánico no encontrado")

        // Traer servicios asignados
        val serviceEntities = initialDataDao.getAllAssignedServices()
        val assignedServices = serviceEntities.map { entity ->
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
        val mechanicEntity = initialDataDao.getMechanic()
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
        val entities = initialDataDao.getAllAssignedServices()
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


}