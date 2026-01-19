package com.lossabinos.data.repositories

import com.lossabinos.data.datasource.local.ChecklistLocalDataSource
import com.lossabinos.data.datasource.local.database.dao.ActivityEvidenceDao
import com.lossabinos.data.datasource.local.database.dao.ActivityProgressDao
import com.lossabinos.data.datasource.local.database.dao.InitialDataDao
import com.lossabinos.data.datasource.local.database.dao.ObservationResponseDao
import com.lossabinos.data.datasource.local.database.dao.ServiceFieldValueDao
import com.lossabinos.data.datasource.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.datasource.local.database.entities.ActivityProgressEntity
import com.lossabinos.data.datasource.local.database.entities.ObservationResponseEntity
import com.lossabinos.data.datasource.local.database.entities.ServiceFieldValueEntity
import com.lossabinos.data.datasource.local.database.entities.ServiceProgressEntity
import com.lossabinos.data.datasource.remoto.ChecklistRemoteDataSource
import com.lossabinos.data.datasource.remoto.MechanicsRemoteDataSource
import com.lossabinos.data.mappers.ChecklistProgressRequestMapper
import com.lossabinos.data.mappers.toDomain
import com.lossabinos.data.retrofit.SyncServices
import com.lossabinos.domain.entities.ActivityEvidence
import com.lossabinos.domain.entities.ActivityProgress
import com.lossabinos.domain.entities.ObservationAnswer
import com.lossabinos.domain.entities.ServiceFieldValue
import com.lossabinos.domain.repositories.ChecklistRepository
import com.lossabinos.domain.enums.ServiceStatus
import com.lossabinos.domain.enums.SyncStatus
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChecklistRepositoryImpl(
    private val activityProgressDao: ActivityProgressDao,
    private val activityEvidenceDao: ActivityEvidenceDao,
    private val observationResponseDao: ObservationResponseDao,
    private val serviceFieldValueDao: ServiceFieldValueDao,
    private val initialDataDao: InitialDataDao,
    private val checklistProgressRequestMapper: ChecklistProgressRequestMapper,
    private val checklistRemoteDataSource: ChecklistRemoteDataSource,
    private val checklistLocalDataSource: ChecklistLocalDataSource
    ) : ChecklistRepository {

    override suspend fun saveActivityProgress(
        assignedServiceId: String,
        sectionIndex: Int,
        activityIndex: Int,
        activityId: String,
        description: String,
        requiresEvidence: Boolean,
        completed: Boolean ,
        completedAt: String
    ): Long {
        return activityProgressDao.insertActivityProgress(
            ActivityProgressEntity(
                assignedServiceId = assignedServiceId,
                sectionIndex = sectionIndex,
                activityIndex = activityIndex,
                activityId = activityId,
                activityDescription = description,
                requiresEvidence = requiresEvidence,
                completed = true,
                completedAt = completedAt
            )
        )
    }

    override suspend fun getTotalCompletedActivities(
        assignedServiceId: String
    ): Int {
        return activityProgressDao.countCompletedActivities(
            assignedServiceId = assignedServiceId
        )
    }

    override suspend fun getObservationResponsesForSection(
        assignedServiceId: String,
        sectionIndex: Int
    ): List<ObservationAnswer> {
        return observationResponseDao.getObservationResponsesBySection(
            assignedServiceId = assignedServiceId,
            sectionIndex = sectionIndex
        )
            .map { it.toDomain() }
    }

    override suspend fun getActivitiesProgressForSection(
        assignedServiceId: String,
        sectionIndex: Int
    ): List<ActivityProgress> {
        return activityProgressDao.getActivitiesBySectionAndService(
            assignedServiceId = assignedServiceId,
            sectionIndex = sectionIndex
        )
            .map { it.toDomain() }
    }

    //**************************************
    // Activity evidence : activity_evidence
    //**************************************
    override suspend fun getEvidenceForActivity(
        activityProgressId: Long
    ): List<ActivityEvidence> {

        return activityEvidenceDao.getEvidenceByActivityProgress(
            activityProgressId = activityProgressId
        )
            .map { it.toDomain() }
    }

    // NOTA EN domain se omiten id y timestap
    override suspend fun saveActivityEvidence(
        id: Long,
        activityProgressId: Long,
        filePath: String,
        fileType: String
    ) : Long {
        val activityEvidence = ActivityEvidenceEntity(
            id = id,
            activityProgressId = activityProgressId,
            fileType = fileType,
            filePath = filePath,
            timestamp =  System.currentTimeMillis().toISO8601String()
        )
        return activityEvidenceDao.insertActivityEvidence(
            evidence = activityEvidence
        )
    }

    override suspend fun deleteActivityEvidenceById(evidenceId: Long) {
        //activityEvidenceDao.deleteEvidenceById(evidenceId = evidenceId)
        val evidence = activityEvidenceDao.getEvidenceById(evidenceId = evidenceId)
        evidence?.let {
            File(it.filePath).delete()
        }
        activityEvidenceDao.deleteEvidenceById(evidenceId = evidenceId)
    }

    private fun Long.toISO8601String(): String {
        return Instant.ofEpochMilli(this)
            .toString()
    }

    override suspend fun saveObservationResponse(
        assignedServiceId: String,
        sectionIndex: Int,
        observationIndex: Int,
        observationId: String,
        observationDescription: String,
        responseType: String,
        response: String?,
        requiresResponse: Boolean
    ): Long {
        val entity = ObservationResponseEntity(
            assignedServiceId = assignedServiceId,
            sectionIndex = sectionIndex,
            observationIndex = observationIndex,
            observationId = observationId,
            observationDescription = observationDescription,
            responseType = responseType,
            response = response,
            requiresResponse = requiresResponse,
            timestamp = System.currentTimeMillis().toISO8601String()
        )
        return observationResponseDao.insertObservationResponse(
            response = entity
        )
    }

    //**************************
    // Service Fields Values
    //**************************
    override suspend fun saveServiceFieldValue(
        assignedServiceId: String,
        fieldIndex: Int,
        fieldLabel: String,
        fieldType: String,
        required: Boolean,
        value: String?
    ): Long {

        val timestamp = LocalDateTime.now().format(
            DateTimeFormatter.ISO_DATE_TIME
        )

        val entity = ServiceFieldValueEntity(
            assignedServiceId = assignedServiceId,
            fieldIndex = fieldIndex,
            fieldLabel = fieldLabel,
            fieldType = fieldType,
            required = required,
            value = value,
            timestamp = timestamp
        )

        println("💾 Guardando campo: $fieldLabel = $value")
        return serviceFieldValueDao.insertServiceFieldValue(
            entity = entity
        )
    }

    override suspend fun saveServiceFieldValues(
        assignedServiceId: String,
        fields: List<ServiceFieldValue>
    ) {
        val timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ISO_DATE_TIME)

        try {
            println("💾 ChecklistRepository.saveServiceFieldValues()")
            println("   Servicio: $assignedServiceId")
            println("   Campos: ${fields.size}")

            // 🆕 PASO 1: Obtener registros anteriores
            val previousValues = serviceFieldValueDao.getServiceFieldValuesByService(assignedServiceId)
            println("\n📋 Registros anteriores encontrados: ${previousValues.size}")

            val entities = fields.mapIndexed { index, field ->
                ServiceFieldValueEntity(
                    assignedServiceId = assignedServiceId,
                    fieldIndex = index,
                    fieldLabel = field.fieldLabel,
                    fieldType = field.fieldType.name,
                    required = field.required,
                    value = field.value,
                    timestamp = timestamp
                )
            }

            println("\n🔄 Haciendo UPSERT (actualizar o insertar)...")
            serviceFieldValueDao.upsertServiceFieldValues(entities)

            // 🆕 PASO 2: Obtener registros nuevos/actualizados
            val updatedValues = serviceFieldValueDao.getServiceFieldValuesByService(assignedServiceId)
            println("\n📊 Registros después de UPSERT: ${updatedValues.size}")

            // 🆕 PASO 3: Validar qué cambió
            println("\n✅ ChecklistRepositoryImpl - Resumen de cambios:")
            entities.forEach { entity ->
                val previous = previousValues.find {
                    it.fieldLabel == entity.fieldLabel
                }

                if (previous == null) {
                    // 🆕 NUEVO REGISTRO
                    println("   ➕ NUEVO: ${entity.fieldLabel} = ${entity.value}")
                } else if (previous.value != entity.value) {
                    // 🔄 ACTUALIZADO
                    println("   🔄 ACTUALIZADO: ${entity.fieldLabel}")
                    println("      Anterior: ${previous.value}")
                    println("      Nuevo: ${entity.value}")
                } else {
                    // ✅ SIN CAMBIOS
                    println("   ✓ SIN CAMBIOS: ${entity.fieldLabel} = ${entity.value}")
                }
            }
        } catch (e: Exception) {
            println("❌ Error guardando campos: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getServiceFieldValues(
        assignedServiceId: String
    ): List<ServiceFieldValue> {
        return serviceFieldValueDao.getServiceFieldValuesByService(
            assignedServiceId = assignedServiceId
        )
            .map { it.toDomain() }
    }

    override suspend fun saveServiceProgress(
        assignedServiceId: String,
        completedActivities: Int,
        totalActivities: Int,
        completedPercentage: Int,
        status: ServiceStatus,
        lastUpdatedAt: Long,
        syncStatus: SyncStatus
    ) {
        try {
            val serviceProgressEntity = ServiceProgressEntity(
                assignedServiceId = assignedServiceId,
                completedActivities = completedActivities,
                totalActivities = totalActivities,
                completedPercentage = completedPercentage,
                status = status.name,
                lastUpdatedAt = lastUpdatedAt,
                syncStatus = syncStatus.name
            )
            activityProgressDao.insertServiceProgress(
                progress = serviceProgressEntity
            )

            println("✅ Progreso guardado CHecklistRepositoryImp: $assignedServiceId")
        }catch (e: Exception){
            println("❌ Error guardando progreso: ${e.message}")
            throw e
        }
    }

    override suspend fun syncChecklist(serviceId: String) {
        try {
            println("🔄 [Repo] Sincronizando checklist: $serviceId")

            // 1️⃣ Obtener el servicio (para conseguir el template JSON)
            val assignedService = initialDataDao.getAssignedServiceById(id = serviceId)
                ?:throw Exception("Servicio no eencontrado")

            // Usar Elvis operator para manejar null
            val templateJson = assignedService.checklistTemplateJson
                ?: throw Exception("Template JSON no encontrado")

            println("✅ [Repo] templateJson:$templateJson")
            println("✅ [Repo] Servicio obtenido")

            // 2️⃣ Obtener todos los datos de Room
            val activities = activityProgressDao.getAllCompletedActivities(assignedServiceId = serviceId)
            val observations = observationResponseDao.getObservationsByService(serviceId = serviceId)
            val fields =  serviceFieldValueDao.getServiceFieldValuesByService(assignedServiceId = serviceId)

            println("✅ [Repo] Datos obtenidos:")
            println("   - Activities: ${activities.size}")
            println("   - Observations: ${observations.size}")
            println("   - Fields: ${fields.size}")

            // 3️⃣ Construir el JSON correctamente usando el Mapper
            val requestJSON = checklistProgressRequestMapper.buildChecklistProgressRequest(
                templateJson = templateJson,
                activities = activities.map { it.toDomain() },
                observations = observations.map { it.toDomain() },
                fields = fields.map { it.toDomain() }
            )

            println("✅ [Repo] JSON construido")
            println("📋 [Repo] Payload:\n${requestJSON.toString(2)}")

            // 4️⃣ Crear RequestBody
            val requestBody = requestJSON.toString().toRequestBody("application/json".toMediaType())

            // 5️⃣ Enviar al servidor
            println("🌐 [Repo] Enviando al servidor...")
            val response = checklistRemoteDataSource.syncProgress(
                serviceId = serviceId,
                request = requestBody
            )

            // 6️⃣ Procesar respuesta
            if (response.isSuccessful) {
                println("✅ [Repo] Response exitosa: ${response.code()}")
                // Marcar como sincronizado

                // ✅ Actualizar solo syncStatus a SYNCED
                activityProgressDao.updateServiceProgressSyncStatus(
                    assignedServiceId = serviceId,
                    syncStatus = SyncStatus.SYNCED.name
                )

                println("✅ [Repo] Servicio marcado como SYNCED")
            } else {
                // capturar error
                val errorBody = response.errorBody()?.string() ?: ""
                val errorMessage = try {
                    val jsonObject = JSONObject(errorBody)
                    jsonObject.getString("detail") // Extrae el campo "detail"
                } catch (e: Exception) {
                    "Error: ${response.code()}"
                }

                println("❌ [Repo] Error HTTP: $errorMessage")
                throw Exception(errorMessage)
            }

            // ═══════════════════════════════════════════════════════════════════════════════════
            //  SEGUNDO: Sincronizar evidencias (fotos)
            // ═══════════════════════════════════════════════════════════════════════════════════

            println("\n📸 [Repo] Iniciando sincronización de evidencias...")

            // Obtener todas las actividades con sus evidencias
            //val allActivities = activityProgressDao.getActivityProgressByService(serviceId)
            val allActivities = checklistLocalDataSource.getActivityProgressByService(serviceId)

            println("📊 [Repo] Total de actividades: ${allActivities.size}")

            // Filtrar actividades que tienen evidencias
            val activitiesWithEvidence = allActivities.filter { activity ->
                val evidences = activityEvidenceDao.getEvidenceByActivityProgress(activity.id)
                evidences.isNotEmpty()
            }

            println("📸 [Repo] Actividades con fotos: ${activitiesWithEvidence.size}")

            if (activitiesWithEvidence.isNotEmpty()) {
                // Sincronizar fotos de cada actividad
                activitiesWithEvidence.forEach { activity ->
                    try {
                        println("\n🔄 [Repo] Sincronizando fotos de: ${activity.activityDescription}")

                        // Obtener fotos de esta actividad
                        val evidences = activityEvidenceDao.getEvidenceByActivityProgress(activity.id)

                        println("   📷 Total de fotos: ${evidences.size}")

                        // Enviar cada foto
                        evidences.forEach { evidence ->
                            try {
                                println("   📤 Enviando: ${evidence.filePath}")

                                val photoFile = File(evidence.filePath)

                                if (!photoFile.exists()) {
                                    println("   ❌ Archivo no existe: ${evidence.filePath}")
                                    return@forEach
                                }

                                // Enviar foto al servidor
                                val photoResponse = checklistRemoteDataSource.syncProgressEvidence(
                                    serviceId = serviceId,
                                    activityId = activity.activityId,
                                    photoFile = photoFile,
                                    photoType = "general",
                                    description = ""
                                )

                                if (photoResponse.isSuccessful) {
                                    println("   ✅ Foto enviada exitosamente")
                                    // Opcional: eliminar archivo después de sincronizar
                                    // photoFile.delete()
                                } else {
                                    println("   ❌ Error enviando foto: ${photoResponse.code()}")
                                    throw Exception("Error foto: ${photoResponse.code()}")
                                }

                            } catch (e: Exception) {
                                println("   ❌ Exception en foto: ${e.message}")
                                // Continuar con las siguientes fotos
                            }
                        }

                    } catch (e: Exception) {
                        println("❌ [Repo] Error en actividad: ${e.message}")
                        // Continuar con las siguientes actividades
                    }
                }
                println("\n✅ [Repo] Todas las fotos sincronizadas")
            } else {
                println("⚠️ [Repo] No hay evidencias para sincronizar")
            }
            println("\n✅ [Repo] Sincronización completa (checklist + evidencias)")

        }
        catch (e: Exception){
            println("❌ [Repo] Exception: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun syncActivityChecklistEvidence(serviceId: String, activityId: String) {
        try {
            println("🔄 [Repo] Sincronizando evidencia de actividad: $activityId")

            // 1️⃣ Obtener las actividades del servicio
            //val activities = activityProgressDao.getActivityProgressByService(serviceId)
            val activities = checklistLocalDataSource.getActivityProgressByService(serviceId)

            // 2️⃣ Encontrar la actividad específica
            val activity = activities.find {
                it.activityIndex == activityId.toIntOrNull()  // Ajusta según tu estructura
            } ?: throw Exception("Actividad no encontrada: $activityId")

            println("✅ [Repo] Actividad encontrada: ${activity.activityDescription}")

            // 3️⃣ Obtener las fotos asociadas a esta actividad
            //val evidences = activityEvidenceDao.getEvidenceByActivityProgress(activity.id)
            val evidences = checklistLocalDataSource.getEvidenceByActivityProgress(activity.id)

            println("📸 [Repo] Total de fotos a sincronizar: ${evidences.size}")

            if (evidences.isEmpty()) {
                println("⚠️ [Repo] No hay fotos para sincronizar")
                return
            }

            // 4️⃣ Enviar cada foto al servidor
            evidences.forEach { evidence ->
                try {
                    println("\n📤 [Repo] Enviando foto: ${evidence.filePath}")

                    val photoFile = File(evidence.filePath)

                    // Validar archivo existe
                    if (!photoFile.exists()) {
                        println("❌ [Repo] Archivo no existe: ${evidence.filePath}")
                        return@forEach
                    }

                    // Enviar foto
                    val response = checklistRemoteDataSource.syncProgressEvidence(
                        serviceId = serviceId,
                        activityId = activityId,
                        photoFile = photoFile,
                        photoType = "general",  // Default
                        description = ""        // Default vacío
                    )

                    // 5️⃣ Procesar respuesta
                    if (response.isSuccessful) {
                        println("✅ [Repo] Foto enviada exitosamente: ${evidence.filePath}")
                        println("   Response: ${response}")

                        // 6️⃣ Eliminar la foto de Room después de sincronizar
                        // (Opcional: depende si quieres mantener un historial)
                        // activityEvidenceDao.deleteEvidenceById(evidence.id)

                    } else {
                        val errorBody = response
                        println("❌ [Repo] Error enviando foto: ${response.code()}")
                        println("   Error: $errorBody")
                        throw Exception("Error ${response.code()}: $errorBody")
                    }

                } catch (e: Exception) {
                    println("❌ [Repo] Exception enviando foto: ${e.message}")
                    e.printStackTrace()
                    throw e
                }
            }

            println("\n✅ [Repo] Todas las fotos sincronizadas correctamente")
        }
        catch (e: Exception){
            println("❌ [Repo] Exception: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}
