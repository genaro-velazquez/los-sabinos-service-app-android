package com.lossabinos.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.lossabinos.data.datasource.local.database.entities.SyncStatusEntity
import com.lossabinos.data.datasource.local.database.entities.WorkRequestEntity
import kotlinx.coroutines.flow.Flow

// ═══════════════════════════════════════════════════════
// WORK REQUEST DAO
// ═══════════════════════════════════════════════════════
@Dao
interface WorkRequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WorkRequestEntity)

    @Query("""
        UPDATE work_request 
        SET syncStatus = :status 
        WHERE id = :id
    """)
    suspend fun updateSyncStatus(
        id: String,
        status: SyncStatusEntity
    )

    @Query("SELECT * FROM work_request WHERE syncStatus = :status")
    suspend fun getBySyncStatus(status: SyncStatusEntity): List<WorkRequestEntity>

    @Upsert
    suspend fun upsertWorkRequest(workRequest: WorkRequestEntity)

    // ═══════════════════════════════════════════════════════
    // CREATE - Insertar reporte de trabajo
    // ═══════════════════════════════════════════════════════
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWorkRequest(workRequest: WorkRequestEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkRequests(workRequests: List<WorkRequestEntity>)

    // ═══════════════════════════════════════════════════════
    // READ - Obtener reportes de trabajo
    // ═══════════════════════════════════════════════════════

    // Obtener todos los reportes de un workOrder (como Flow)
    @Query("SELECT * FROM work_request WHERE workOrderId = :workOrderId ORDER BY createdAt DESC")
    fun getWorkRequestsByWorkOrderFlow(workOrderId: String): Flow<List<WorkRequestEntity>>

    // Obtener todos los reportes de un workOrder (una sola vez)
    @Query("SELECT * FROM work_request WHERE workOrderId = :workOrderId ORDER BY createdAt DESC")
    suspend fun getWorkRequestsByWorkOrder(workOrderId: String): List<WorkRequestEntity>

    // Obtener un reporte por ID
    @Query("SELECT * FROM work_request WHERE id = :id")
    suspend fun getWorkRequestById(id: String): WorkRequestEntity?

    // Obtener todos los reportes PENDING (para sincronizar después)
    @Query("SELECT * FROM work_request WHERE syncStatus = 'PENDING' ORDER BY createdAt DESC")
    suspend fun getPendingWorkRequests(): List<WorkRequestEntity>

    // Obtener todos los reportes
    @Query("SELECT * FROM work_request ORDER BY createdAt DESC")
    suspend fun getAllWorkRequests(): List<WorkRequestEntity>

    // ═══════════════════════════════════════════════════════
    // UPDATE - Actualizar reporte
    // ═══════════════════════════════════════════════════════
    @Update
    suspend fun updateWorkRequest(workRequest: WorkRequestEntity)

    // ═══════════════════════════════════════════════════════
    // DELETE - Eliminar reportes
    // ═══════════════════════════════════════════════════════

    // Eliminar por ID
    @Query("DELETE FROM work_request WHERE id = :id")
    suspend fun deleteWorkRequestById(id: String)

    // Eliminar un reporte
    @Delete
    suspend fun deleteWorkRequest(workRequest: WorkRequestEntity)

    // Eliminar todos los reportes de un workOrder
    @Query("DELETE FROM work_request WHERE workOrderId = :workOrderId")
    suspend fun deleteWorkRequestsByWorkOrder(workOrderId: String)

    // Eliminar todos los reportes
    @Query("DELETE FROM work_request")
    suspend fun deleteAllWorkRequests()

    // ═══════════════════════════════════════════════════════
    // COUNT - Contar reportes
    // ═══════════════════════════════════════════════════════

    // Obtener cantidad de reportes de un workOrder
    @Query("SELECT COUNT(*) FROM work_request WHERE workOrderId = :workOrderId")
    suspend fun getWorkRequestCountByWorkOrder(workOrderId: String): Int

    // Obtener cantidad de reportes PENDING
    @Query("SELECT COUNT(*) FROM work_request WHERE syncStatus = 'PENDING'")
    suspend fun getPendingWorkRequestCount(): Int
}