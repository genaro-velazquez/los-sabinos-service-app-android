package com.lossabinos.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Query
import androidx.room.Upsert
import com.lossabinos.data.datasource.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.datasource.local.database.entities.ActivityProgressEntity
import com.lossabinos.data.datasource.local.database.entities.ExtraCostEntity
import com.lossabinos.data.datasource.local.database.entities.ObservationResponseEntity
import com.lossabinos.data.datasource.local.database.entities.ServiceFieldValueEntity
import com.lossabinos.data.datasource.local.database.entities.ServiceProgressEntity
import com.lossabinos.data.datasource.local.database.entities.ServiceStartEntity
import kotlinx.coroutines.flow.Flow


// 1️⃣ DAO para Activity Progress
@Dao
interface ActivityProgressDao {

    // ═══════════════════════════════════════════════════════
    // Elimina todoas las ActivityProgress
    // ═══════════════════════════════════════════════════════
    @Query("DELETE FROM activity_progress")
    suspend fun deleteAllActivityProgress()

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

    //===========================================
    // ServiceProgressEntity -> service_progress
    //===========================================
    // Insertar/actualizar progreso
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServiceProgress(progress: ServiceProgressEntity)

    // Obtener progreso de un servicio
    @Query("SELECT * FROM service_progress WHERE assignedServiceId = :serviceId")
    suspend fun getServiceProgress(serviceId: String): ServiceProgressEntity?

    @Query("UPDATE service_progress SET syncStatus = :syncStatus WHERE assignedServiceId = :assignedServiceId")
    suspend fun updateServiceProgressSyncStatus(
        assignedServiceId: String,
        syncStatus: String
    )

    @Query("DELETE FROM service_progress")
    suspend fun deleteAllServiceProgress()

    @Query("""
    SELECT syncStatus 
    FROM service_progress 
    WHERE assignedServiceId = :serviceId
    LIMIT 1
""")
    suspend fun getSyncStatus(serviceId: String): String?

}

// 2️⃣ DAO para Activity Evidence
@Dao
interface ActivityEvidenceDao {

    // ═══════════════════════════════════
    // Elimina todoas las ActivityEvidence
    // ═══════════════════════════════════
    @Query("DELETE FROM activity_evidence")
    suspend fun deleteAllActivityEvidences()

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

    @Query("DELETE FROM activity_evidence WHERE id = :evidenceId")
    suspend fun deleteEvidenceById(evidenceId: Long)

    @Query("SELECT * FROM activity_evidence WHERE id = :evidenceId LIMIT 1")
    suspend fun getEvidenceById(evidenceId: Long): ActivityEvidenceEntity?
}

// 3️⃣ DAO para Observation Response
@Dao
interface ObservationResponseDao {

    // ═══════════════════════════════════
    // Elimina todoas los ObservationResponse
    // ═══════════════════════════════════=
    @Query("DELETE FROM observation_response")
    suspend fun deleteAllObservationResponses()

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

    // ═══════════════════════════════════
    // Elimina todoas los Services fields values
    // ═══════════════════════════════════=
    @Query("DELETE FROM service_field_value")
    suspend fun deleteAllServiceFieldValues()

    // 🆕 UPSERT (Update or Insert) - Más eficiente
    @Upsert
    suspend fun upsertServiceFieldValue(entity: ServiceFieldValueEntity)

    // 🆕 UPSERT múltiples
    @Upsert
    suspend fun upsertServiceFieldValues(entities: List<ServiceFieldValueEntity>)

    // Guardar uno (deprecated - usar upsert)
    @Insert
    suspend fun insertServiceFieldValue(entity: ServiceFieldValueEntity): Long

    // Guardar múltiples (deprecated - usar upsert)
    @Insert
    suspend fun insertServiceFieldValues(entities: List<ServiceFieldValueEntity>)

    // 🆕 Actualizar
    @Update
    suspend fun updateServiceFieldValue(entity: ServiceFieldValueEntity)

    // 🆕 Obtener valor específico
    @Query("SELECT * FROM service_field_value WHERE assignedServiceId = :assignedServiceId AND fieldLabel = :fieldLabel LIMIT 1")
    suspend fun getServiceFieldValue(assignedServiceId: String, fieldLabel: String): ServiceFieldValueEntity?

    // Obtener valores de un servicio
    @Query("SELECT * FROM service_field_value WHERE assignedServiceId = :assignedServiceId")
    suspend fun getServiceFieldValuesByService(assignedServiceId: String): List<ServiceFieldValueEntity>

    // 🆕 Eliminar campos de un servicio (antes de guardar nuevamente)
    @Query("DELETE FROM service_field_value WHERE assignedServiceId = :assignedServiceId")
    suspend fun deleteServiceFieldValuesByService(assignedServiceId: String)
}

@Dao
interface ExtraCostDao {
    // ═══════════════════════════════════════════════════════
    // CREATE - Insertar un costo extra
    // ═══════════════════════════════════════════════════════
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExtraCost(extraCost: ExtraCostEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExtraCosts(extraCosts: List<ExtraCostEntity>)

    // ═══════════════════════════════════════════════════════
    // READ - Obtener costos extra
    // ═══════════════════════════════════════════════════════

    // Obtener todos los costos de un servicio (como Flow)
    @Query("SELECT * FROM extra_cost WHERE assignedServiceId = :assignedServiceId ORDER BY createdAt DESC")
    fun getExtraCostsByServiceFlow(assignedServiceId: String): Flow<List<ExtraCostEntity>>

    // Obtener todos los costos de un servicio (una sola vez)
    @Query("SELECT * FROM extra_cost WHERE assignedServiceId = :assignedServiceId ORDER BY createdAt DESC")
    suspend fun getExtraCostsByService(assignedServiceId: String): List<ExtraCostEntity>

    // Obtener un costo extra por ID
    @Query("SELECT * FROM extra_cost WHERE id = :id")
    suspend fun getExtraCostById(id: String): ExtraCostEntity?

    // Obtener todos los costos extra
    @Query("SELECT * FROM extra_cost ORDER BY createdAt DESC")
    suspend fun getAllExtraCosts(): List<ExtraCostEntity>

    // ═══════════════════════════════════════════════════════
    // UPDATE - Actualizar un costo extra
    // ═══════════════════════════════════════════════════════
    @Update
    suspend fun updateExtraCost(extraCost: ExtraCostEntity)

    // ═══════════════════════════════════════════════════════
    // DELETE - Eliminar costos extra
    // ═══════════════════════════════════════════════════════

    // Eliminar por ID
    @Query("DELETE FROM extra_cost WHERE id = :id")
    suspend fun deleteExtraCostById(id: String)

    // Eliminar un costo extra
    @Delete
    suspend fun deleteExtraCost(extraCost: ExtraCostEntity)

    // Eliminar todos los costos de un servicio
    @Query("DELETE FROM extra_cost WHERE assignedServiceId = :assignedServiceId")
    suspend fun deleteExtraCostsByService(assignedServiceId: String)

    // Eliminar todos los costos extra
    @Query("DELETE FROM extra_cost")
    suspend fun deleteAllExtraCosts()

    // ═══════════════════════════════════════════════════════
    // TOTAL - Calcular totales
    // ═══════════════════════════════════════════════════════

    // Obtener suma total de costos de un servicio
    @Query("SELECT COALESCE(SUM(quantity), 0.0) FROM extra_cost WHERE assignedServiceId = :assignedServiceId")
    suspend fun getTotalExtraCostByService(assignedServiceId: String): Double

    // Obtener suma total de costos de un servicio (como Flow)
    @Query("SELECT COALESCE(SUM(quantity), 0.0) FROM extra_cost WHERE assignedServiceId = :assignedServiceId")
    fun getTotalExtraCostByServiceFlow(assignedServiceId: String): Flow<Double>

    // Obtener cantidad de costos de un servicio
    @Query("SELECT COUNT(*) FROM extra_cost WHERE assignedServiceId = :assignedServiceId")
    suspend fun getExtraCostCountByService(assignedServiceId: String): Int
}

@Dao
interface ServiceStartDao {

    // ═══════════════════════════════════════════════════════
    // CREATE - Insertar registro de inicio
    // ═══════════════════════════════════════════════════════
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertServiceStart(serviceStart: ServiceStartEntity): Long

    // ═══════════════════════════════════════════════════════
    // READ - Obtener registro de inicio
    // ═══════════════════════════════════════════════════════

    // Obtener registro por assignedServiceId
    @Query("SELECT * FROM service_start WHERE assignedServiceId = :assignedServiceId LIMIT 1")
    suspend fun getServiceStartByService(assignedServiceId: String): ServiceStartEntity?

    // Obtener todos los registros PENDING
    @Query("SELECT * FROM service_start WHERE syncStatus = 'PENDING' ORDER BY createdAt DESC")
    suspend fun getPendingServiceStarts(): List<ServiceStartEntity>

    // ═══════════════════════════════════════════════════════
    // UPDATE - Actualizar registro
    // ═══════════════════════════════════════════════════════
    @Update
    suspend fun updateServiceStart(serviceStart: ServiceStartEntity)

    // ═══════════════════════════════════════════════════════
    // DELETE - Eliminar registro
    // ═══════════════════════════════════════════════════════
    @Query("DELETE FROM service_start WHERE assignedServiceId = :assignedServiceId")
    suspend fun deleteServiceStartByService(assignedServiceId: String)

    @Query("DELETE FROM service_start")
    suspend fun deleteAllServiceStarts()
}
