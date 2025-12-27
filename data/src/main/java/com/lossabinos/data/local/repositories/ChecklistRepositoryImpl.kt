package com.lossabinos.data.local.repositories

import com.lossabinos.data.local.database.dao.ActivityEvidenceDao
import com.lossabinos.data.local.database.dao.ActivityProgressDao
import com.lossabinos.data.local.database.dao.ObservationResponseDao
import com.lossabinos.data.local.database.dao.ServiceFieldValueDao
import com.lossabinos.data.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.local.database.entities.ActivityProgressEntity
import com.lossabinos.data.local.database.entities.ObservationResponseEntity
import com.lossabinos.data.local.database.entities.ServiceFieldValueEntity
import com.lossabinos.data.local.mappers.toDomain
import com.lossabinos.data.local.mappers.toEntity
import com.lossabinos.domain.entities.ActivityEvidence
import com.lossabinos.domain.entities.ActivityProgress
import com.lossabinos.domain.entities.ObservationAnswer
import com.lossabinos.domain.entities.ServiceFieldValue
import com.lossabinos.domain.repositories.ChecklistRepository
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChecklistRepositoryImpl(
    private val activityProgressDao: ActivityProgressDao,
    private val activityEvidenceDao: ActivityEvidenceDao,
    private val observationResponseDao: ObservationResponseDao,
    private val serviceFieldValueDao: ServiceFieldValueDao
) : ChecklistRepository {

    override suspend fun saveActivityProgress(
        assignedServiceId: String,
        sectionIndex: Int,
        activityIndex: Int,
        description: String,
        requiresEvidence: Boolean
    ): Long {
        return activityProgressDao.insertActivityProgress(
            ActivityProgressEntity(
                assignedServiceId = assignedServiceId,
                sectionIndex = sectionIndex,
                activityIndex = activityIndex,
                activityDescription = description,
                requiresEvidence = requiresEvidence,
                completed = true
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
        return java.time.Instant.ofEpochMilli(this)
            .toString()
    }

    override suspend fun saveObservationResponse(
        assignedServiceId: String,
        sectionIndex: Int,
        observationIndex: Int,
        observationDescription: String,
        response: String
    ): Long {
        val entity = ObservationResponseEntity(
            assignedServiceId = assignedServiceId,
            sectionIndex = sectionIndex,
            observationIndex = observationIndex,
            observationDescription = observationDescription,
            response = response,
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
}