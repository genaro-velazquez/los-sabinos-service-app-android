package com.lossabinos.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Query
import com.lossabinos.data.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.local.database.entities.ActivityProgressEntity
import com.lossabinos.data.local.database.entities.ObservationResponseEntity
import com.lossabinos.data.local.database.entities.ServiceFieldValueEntity


// 1️⃣ DAO para Activity Progress
@Dao
interface ActivityProgressDao {

    // ═══════════════════════════════════════════════════════
    // 1. INSERTAR actividad completada
    // ═══════════════════════════════════════════════════════
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityProgress(activity: ActivityProgressEntity): Long

    // ═══════════════════════════════════════════════════════
    // 4. ✨ OBTENER actividades de una sección específica
    // ═══════════════════════════════════════════════════════
    @Query("""
        SELECT * FROM activity_progress 
        WHERE assignedServiceId = :assignedServiceId 
        AND sectionIndex = :sectionIndex
        ORDER BY activityIndex ASC
    """)
    suspend fun getActivitiesBySectionAndService(
        assignedServiceId: String,
        sectionIndex: Int
    ): List<ActivityProgressEntity>

    // ═══════════════════════════════════════════════════════
    // 7. ✨ CONTAR actividades completadas de un servicio
    // ═══════════════════════════════════════════════════════
    @Query("""
        SELECT COUNT(*) FROM activity_progress 
        WHERE assignedServiceId = :assignedServiceId 
        AND completed = 1
    """)
    suspend fun countCompletedActivities(
        assignedServiceId: String
    ): Int

    // ═══════════════════════════════════════════════════════
    // 8. ✨ OBTENER TODAS las actividades completadas (para verificar sección)
    // ═══════════════════════════════════════════════════════
    @Query("""
        SELECT * FROM activity_progress 
        WHERE assignedServiceId = :assignedServiceId 
        AND completed = 1
        ORDER BY sectionIndex, activityIndex ASC
    """)
    suspend fun getAllCompletedActivities(
        assignedServiceId: String
    ): List<ActivityProgressEntity>

    // ═══════════════════════════════════════════════════════
    // 9. ✨ VERIFICAR si una actividad ya está completada
    // ═══════════════════════════════════════════════════════
    @Query("""
        SELECT * FROM activity_progress 
        WHERE assignedServiceId = :assignedServiceId 
        AND sectionIndex = :sectionIndex
        AND activityIndex = :activityIndex
        LIMIT 1
    """)
    suspend fun getActivityProgress(
        assignedServiceId: String,
        sectionIndex: Int,
        activityIndex: Int
    ): ActivityProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityProgressList(activities: List<ActivityProgressEntity>)

    @Update
    suspend fun updateActivityProgress(activity: ActivityProgressEntity)

    @Query("""
        SELECT * FROM activity_progress 
        WHERE assignedServiceId = :serviceId
        ORDER BY sectionIndex, activityIndex
    """)
    suspend fun getActivityProgressByService(serviceId: String): List<ActivityProgressEntity>

    @Query("""
        SELECT COUNT(*) FROM activity_progress 
        WHERE assignedServiceId = :serviceId AND completed = 1
    """)
    suspend fun getCompletedCount(serviceId: String): Int

    @Query("""
        SELECT COUNT(*) FROM activity_progress 
        WHERE assignedServiceId = :serviceId
    """)
    suspend fun getTotalActivitiesCount(serviceId: String): Int

    @Delete
    suspend fun deleteActivityProgress(activity: ActivityProgressEntity)
}

// 2️⃣ DAO para Activity Evidence
@Dao
interface ActivityEvidenceDao {

    // ═══════════════════════════════════════════════════════
    // 2. INSERTAR evidencia (foto)
    // ═══════════════════════════════════════════════════════
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityEvidence(evidence: ActivityEvidenceEntity): Long

    // ═══════════════════════════════════════════════════════
    // 5. ✨ OBTENER evidencias de una actividad
    // ═══════════════════════════════════════════════════════
    @Query("""
        SELECT * FROM activity_evidence 
        WHERE activityProgressId = :activityProgressId
        ORDER BY timestamp DESC
    """)
    suspend fun getEvidenceByActivityProgress(
        activityProgressId: Long
    ): List<ActivityEvidenceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvidence(evidence: ActivityEvidenceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvidenceList(evidences: List<ActivityEvidenceEntity>)

    @Query("""
        SELECT * FROM activity_evidence 
        WHERE activityProgressId = :activityProgressId
    """)
    suspend fun getEvidenceByActivity(activityProgressId: Long): List<ActivityEvidenceEntity>

    @Query("""
        SELECT * FROM activity_evidence 
        WHERE activityProgressId IN (
            SELECT id FROM activity_progress 
            WHERE assignedServiceId = :serviceId
        )
    """)
    suspend fun getEvidenceByService(serviceId: String): List<ActivityEvidenceEntity>

    @Delete
    suspend fun deleteEvidence(evidence: ActivityEvidenceEntity)
}

// 3️⃣ DAO para Observation Response
@Dao
interface ObservationResponseDao {

    // ═══════════════════════════════════════════════════════
    // 3. INSERTAR respuesta a observación
    // ═══════════════════════════════════════════════════════
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservationResponse(response: ObservationResponseEntity): Long

    // ═══════════════════════════════════════════════════════
    // 6. ✨ OBTENER respuestas de observaciones de una sección
    // ═══════════════════════════════════════════════════════
    @Query("""
        SELECT * FROM observation_response 
        WHERE assignedServiceId = :assignedServiceId 
        AND sectionIndex = :sectionIndex
        ORDER BY observationIndex ASC
    """)
    suspend fun getObservationResponsesBySection(
        assignedServiceId: String,
        sectionIndex: Int
    ): List<ObservationResponseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservationResponseList(responses: List<ObservationResponseEntity>)

    @Update
    suspend fun updateObservationResponse(response: ObservationResponseEntity)

    @Query("""
        SELECT * FROM observation_response 
        WHERE assignedServiceId = :serviceId
        ORDER BY sectionIndex, observationIndex
    """)
    suspend fun getObservationsByService(serviceId: String): List<ObservationResponseEntity>

    @Query("""
        SELECT * FROM observation_response 
        WHERE assignedServiceId = :serviceId 
        AND sectionIndex = :sectionIndex 
        AND observationIndex = :observationIndex
    """)
    suspend fun getObservationResponse(
        serviceId: String,
        sectionIndex: Int,
        observationIndex: Int
    ): ObservationResponseEntity?

    @Delete
    suspend fun deleteObservationResponse(response: ObservationResponseEntity)
}

// 4️⃣ DAO para Service Field Value
@Dao
interface ServiceFieldValueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFieldValue(fieldValue: ServiceFieldValueEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFieldValueList(fieldValues: List<ServiceFieldValueEntity>)

    @Update
    suspend fun updateFieldValue(fieldValue: ServiceFieldValueEntity)

    @Query("""
        SELECT * FROM service_field_value 
        WHERE assignedServiceId = :serviceId
        ORDER BY fieldIndex
    """)
    suspend fun getFieldValuesByService(serviceId: String): List<ServiceFieldValueEntity>

    @Query("""
        SELECT * FROM service_field_value 
        WHERE assignedServiceId = :serviceId AND fieldIndex = :fieldIndex
    """)
    suspend fun getFieldValue(
        serviceId: String,
        fieldIndex: Int
    ): ServiceFieldValueEntity?

    @Delete
    suspend fun deleteFieldValue(fieldValue: ServiceFieldValueEntity)
}
