package com.lossabinos.data.datasource.local

import com.lossabinos.data.datasource.local.database.dao.WorkRequestDao
import com.lossabinos.data.datasource.local.database.entities.SyncStatusEntity
import com.lossabinos.data.datasource.local.database.entities.WorkRequestEntity
import com.lossabinos.domain.valueobjects.WorkRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkRequestLocalDataSource @Inject constructor(
    private val workRequestDao: WorkRequestDao
) {

    suspend fun deleteById(id: String) {
        workRequestDao.deleteWorkRequestById(id = id)
    }


    suspend fun updateSyncStatus(
        workRequestId: String,
        status: SyncStatusEntity
    ) {
        workRequestDao.updateSyncStatus(
            id = workRequestId,
            status = status
        )
    }
    suspend fun getBySyncStatus(
        status: SyncStatusEntity
    ): List<WorkRequestEntity> {
        return workRequestDao.getBySyncStatus(status)
    }

    /**
     * Guardar un nuevo reporte de trabajo
     */
    suspend fun insert(workRequest: WorkRequestEntity){
        workRequestDao.insert(entity = workRequest)
    }

    /**
     * Guardar un nuevo reporte de trabajo
     */
    suspend fun insertWorkRequest(workRequest: WorkRequestEntity) {
        println("💾 [LocalDataSource] Guardando work request: ${workRequest.title}")
        workRequestDao.insertWorkRequest(workRequest)
        println("✅ [LocalDataSource] Work request guardado")
    }

    /**
     * Obtener reportes de un workOrder (como Flow)
     */
    fun getWorkRequestsByWorkOrderFlow(workOrderId: String): Flow<List<WorkRequestEntity>> {
        println("📖 [LocalDataSource] Observando work requests para: $workOrderId")
        return workRequestDao.getWorkRequestsByWorkOrderFlow(workOrderId)
    }

    /**
     * Obtener reportes de un workOrder (una sola vez)
     */
    suspend fun getWorkRequestsByWorkOrder(workOrderId: String): List<WorkRequestEntity> {
        println("📖 [LocalDataSource] Obteniendo work requests para: $workOrderId")
        return workRequestDao.getWorkRequestsByWorkOrder(workOrderId)
    }

    /**
     * Obtener un reporte por ID
     */
    suspend fun getWorkRequestById(id: String): WorkRequestEntity? {
        println("📖 [LocalDataSource] Obteniendo work request: $id")
        return workRequestDao.getWorkRequestById(id)
    }

    /**
     * Obtener todos los reportes PENDING
     */
    suspend fun getPendingWorkRequests(): List<WorkRequestEntity> {
        println("📖 [LocalDataSource] Obteniendo work requests PENDING")
        return workRequestDao.getPendingWorkRequests()
    }

    /**
     * Actualizar un reporte (cambiar status, etc.)
     */
    suspend fun updateWorkRequest(workRequest: WorkRequestEntity) {
        println("✏️ [LocalDataSource] Actualizando work request: ${workRequest.id}")
        workRequestDao.updateWorkRequest(workRequest)
        println("✅ [LocalDataSource] Work request actualizado")
    }

    /**
     * Eliminar un reporte
     */
    suspend fun deleteWorkRequestById(id: String) {
        println("🗑️ [LocalDataSource] Eliminando work request: $id")
        workRequestDao.deleteWorkRequestById(id)
        println("✅ [LocalDataSource] Work request eliminado")
    }

    /**
     * Eliminar todos los reportes de un workOrder
     */
    suspend fun deleteWorkRequestsByWorkOrder(workOrderId: String) {
        println("🗑️ [LocalDataSource] Eliminando work requests para: $workOrderId")
        workRequestDao.deleteWorkRequestsByWorkOrder(workOrderId)
        println("✅ [LocalDataSource] Work requests eliminados")
    }

    /**
     * Obtener cantidad de reportes PENDING
     */
    suspend fun getPendingWorkRequestCount(): Int {
        println("📊 [LocalDataSource] Contando work requests PENDING")
        return workRequestDao.getPendingWorkRequestCount()
    }
}