package com.lossabinos.data.repositories.local

import com.lossabinos.data.local.database.dao.ActivityEvidenceDao
import com.lossabinos.data.local.database.dao.ActivityProgressDao
import com.lossabinos.data.local.database.dao.ObservationResponseDao
import com.lossabinos.data.local.database.dao.ServiceFieldValueDao
import com.lossabinos.data.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.local.database.entities.ActivityProgressEntity
import com.lossabinos.data.local.database.entities.AssignedServiceEntity
import com.lossabinos.data.local.database.entities.ObservationResponseEntity
import com.lossabinos.data.local.database.entities.ServiceFieldValueEntity
import com.lossabinos.domain.valueobjects.Template
import kotlinx.serialization.json.Json


class ChecklistRepository(
    private val activityProgressDao: ActivityProgressDao,
    private val activityEvidenceDao: ActivityEvidenceDao,
    private val observationResponseDao: ObservationResponseDao,
    private val serviceFieldValueDao: ServiceFieldValueDao
)  {

    // ✨ Inicializar checklist cuando usuario abre el servicio
    suspend fun initializeServiceChecklist(assignedService: AssignedServiceEntity) {
        try {
            // 1. Deserializar template
            val template = Json.decodeFromString<Template>(
                assignedService.checklistTemplateJson ?: return
            )

            // 2. Crear activity_progress para CADA activity
            template.sections.forEachIndexed { sectionIndex, section ->

                section.activities.forEachIndexed { activityIndex, activity ->
                    activityProgressDao.insertActivityProgress(
                        ActivityProgressEntity(
                            assignedServiceId = assignedService.id,
                            sectionIndex = sectionIndex,
                            activityIndex = activityIndex,
                            activityDescription = activity.description,
                            requiresEvidence = activity.requiresEvidence,
                            completed = false
                        )
                    )
                }

                // 3. Crear observation_response para CADA observation
                section.activities.forEachIndexed { activityIndex, activity ->
                    observationResponseDao.insertObservationResponse(
                        ObservationResponseEntity(
                            assignedServiceId = assignedService.id,
                            sectionIndex = sectionIndex,
                            observationIndex = activityIndex,
                            observationDescription = activity.description,
                            response = null
                        )
                    )
                }
            }

            // 4. Crear service_field_value para CADA campo
            template.serviceFields.forEachIndexed { fieldIndex, field ->
                serviceFieldValueDao.insertFieldValue(
                    ServiceFieldValueEntity(
                        assignedServiceId = assignedService.id,
                        fieldIndex = fieldIndex,
                        fieldLabel = field.label,
                        fieldType = field.type,
                        required = field.required,
                        value = null
                    )
                )
            }

            println("✅ Checklist inicializado para servicio: ${assignedService.id}")
        } catch (e: Exception) {
            println("❌ Error inicializando checklist: ${e.message}")
            throw e
        }
    }

    // ✨ Completar una activity
    suspend fun completeActivity(
        serviceId: String,
        sectionIndex: Int,
        activityIndex: Int
    ) {
        val activity = activityProgressDao.getActivityProgress(
            serviceId, sectionIndex, activityIndex
        ) ?: return

        activityProgressDao.updateActivityProgress(
            activity.copy(
                completed = true,
                completedAt = getCurrentTimestamp()
            )
        )

        updateServiceProgress(serviceId)
    }

    // ✨ Agregar evidencia (foto/video)
    suspend fun addActivityEvidence(
        serviceId: String,
        sectionIndex: Int,
        activityIndex: Int,
        filePath: String
    ) {
        val activity = activityProgressDao.getActivityProgress(
            serviceId, sectionIndex, activityIndex
        ) ?: return

        // Guardar evidencia
        activityEvidenceDao.insertEvidence(
            ActivityEvidenceEntity(
                activityProgressId = activity.id,
                filePath = filePath,
                fileType = "image",
                timestamp = getCurrentTimestamp()
            )
        )

        // Si requería evidencia, marcar como completada
        if (activity.requiresEvidence) {
            completeActivity(serviceId, sectionIndex, activityIndex)
        }
    }

    // ✨ Responder una observation
    suspend fun respondToObservation(
        serviceId: String,
        sectionIndex: Int,
        observationIndex: Int,
        response: String
    ) {
        val observation = observationResponseDao.getObservationResponse(
            serviceId, sectionIndex, observationIndex
        ) ?: return

        observationResponseDao.updateObservationResponse(
            observation.copy(
                response = response,
                timestamp = getCurrentTimestamp()
            )
        )
    }

    // ✨ Actualizar valor de campo
    suspend fun updateServiceFieldValue(
        serviceId: String,
        fieldIndex: Int,
        value: String
    ) {
        val field = serviceFieldValueDao.getFieldValue(serviceId, fieldIndex) ?: return

        serviceFieldValueDao.updateFieldValue(
            field.copy(
                value = value,
                timestamp = getCurrentTimestamp()
            )
        )
    }

    // ✨ Actualizar progreso general del servicio
    suspend fun updateServiceProgress(serviceId: String) {
        val totalActivities = activityProgressDao.getTotalActivitiesCount(serviceId)
        val completedActivities = activityProgressDao.getCompletedCount(serviceId)

        val percentage = if (totalActivities > 0) {
            (completedActivities * 100) / totalActivities
        } else {
            0
        }

        println("📊 Progreso del servicio $serviceId: $completedActivities/$totalActivities ($percentage%)")
    }

    private fun getCurrentTimestamp(): String {
        return java.time.LocalDateTime.now().toString()
    }





    // ═══════════════════════════════════════════════════════
    // 1. OBTENER actividades completadas de una sección
    // ═══════════════════════════════════════════════════════
    suspend fun getActivitiesProgressForSection(
        assignedServiceId: String,
        sectionIndex: Int
    ): List<ActivityProgressEntity> {
        return activityProgressDao.getActivitiesBySectionAndService(
            assignedServiceId = assignedServiceId,
            sectionIndex = sectionIndex
        )
    }

    // ═══════════════════════════════════════════════════════
    // 2. OBTENER evidencias (fotos) de una actividad
    // ═══════════════════════════════════════════════════════
    suspend fun getEvidenceForActivity(
        activityProgressId: Long
    ): List<ActivityEvidenceEntity> {
        return activityEvidenceDao.getEvidenceByActivityProgress(activityProgressId)
    }

    // ═══════════════════════════════════════════════════════
    // 3. OBTENER respuestas de observaciones de una sección
    // ═══════════════════════════════════════════════════════
    suspend fun getObservationResponsesForSection(
        assignedServiceId: String,
        sectionIndex: Int
    ): List<ObservationResponseEntity> {
        return observationResponseDao.getObservationResponsesBySection(
            assignedServiceId = assignedServiceId,
            sectionIndex = sectionIndex
        )
    }

    // ═══════════════════════════════════════════════════════
    // 4. OBTENER TODAS las actividades completadas del servicio
    // ═══════════════════════════════════════════════════════
    suspend fun getTotalCompletedActivities(
        assignedServiceId: String
    ): Int {
        return activityProgressDao.countCompletedActivities(
            assignedServiceId = assignedServiceId
        )
    }

    // ═══════════════════════════════════════════════════════
    // 5. GUARDAR actividad completada
    // ═══════════════════════════════════════════════════════
    suspend fun saveActivityProgress(
        assignedServiceId: String,
        sectionIndex: Int,
        activityIndex: Int,
        description: String,
        requiresEvidence: Boolean
    ): Long {
        val entity = ActivityProgressEntity(
            assignedServiceId = assignedServiceId,
            sectionIndex = sectionIndex,
            activityIndex = activityIndex,
            activityDescription = description,
            requiresEvidence = requiresEvidence,
            completed = true,
            completedAt = System.currentTimeMillis().toISO8601String()
        )
        return activityProgressDao.insertActivityProgress(entity)
    }

    // ═══════════════════════════════════════════════════════
    // 6. GUARDAR evidencia (foto)
    // ═══════════════════════════════════════════════════════
    suspend fun saveActivityEvidence(
        activityProgressId: Long,
        filePath: String,
        fileType: String = "image"
    ) {
        val entity = ActivityEvidenceEntity(
            activityProgressId = activityProgressId,
            filePath = filePath,
            fileType = fileType,
            timestamp = System.currentTimeMillis().toISO8601String()
        )
        activityEvidenceDao.insertActivityEvidence(entity)
    }

    // ═══════════════════════════════════════════════════════
    // 7. GUARDAR respuesta a observación
    // ═══════════════════════════════════════════════════════
    suspend fun saveObservationResponse(
        assignedServiceId: String,
        sectionIndex: Int,
        observationIndex: Int,
        observationDescription: String,
        response: String
    ) {
        val entity = ObservationResponseEntity(
            assignedServiceId = assignedServiceId,
            sectionIndex = sectionIndex,
            observationIndex = observationIndex,
            observationDescription = observationDescription,
            response = response,
            timestamp = System.currentTimeMillis().toISO8601String()
        )
        observationResponseDao.insertObservationResponse(entity)
    }

    // ═══════════════════════════════════════════════════════
    // 8. Helper: Convertir ISO8601 String a formato legible
    // ═══════════════════════════════════════════════════════
    private fun Long.toISO8601String(): String {
        return java.time.Instant.ofEpochMilli(this)
            .toString()
    }
}