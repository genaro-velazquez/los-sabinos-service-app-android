package com.lossabinos.data.repositories

import com.lossabinos.data.datasource.local.WorkRequestLocalDataSource
import com.lossabinos.data.datasource.local.database.entities.SyncStatusEntity
import com.lossabinos.data.datasource.local.database.entities.UrgencyLevelEntity
import com.lossabinos.data.datasource.local.database.entities.WorkRequestEntity
import com.lossabinos.data.datasource.remoto.WorkRequestRemoteDataSource
import com.lossabinos.data.mappers.toDomain
import com.lossabinos.domain.repositories.WorkRequestRepository
import com.lossabinos.domain.valueobjects.WorkRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class WorkRequestRepositoryImp(
    private val workRequestLocalDataSource: WorkRequestLocalDataSource,
    private val workRequestRemoteRepository: WorkRequestRemoteDataSource
) : WorkRequestRepository {

    override suspend fun createWorkRequest(request: WorkRequest) {
        try {
            println("💾 [Repo] Creando work request: ${request.title}")

            // Convertir Domain a Entity
            val entity = WorkRequestEntity(
                id = request.id,
                workOrderId = request.workOrderId,
                title = request.title,
                description = request.description,
                findings = request.findings,
                justification = request.justification,
                photoUrls = request.photoUls,  // Convertir List a JSON String
                requestType = "additional_work", //request.requestType,
                requiresCustomerApproval = request.requiresCustomerApproval,
                urgency = UrgencyLevelEntity.valueOf(request.urgency.name),
                createdAt = request.createdAt,
                vehicleId = request.vehicleId,
                syncStatus = SyncStatusEntity.PENDING
            )

            // 🔑 CLAVE: decidir INSERT vs UPDATE
            val existing =
                workRequestLocalDataSource.getWorkRequestById(entity.id)

            if (existing == null) {
                workRequestLocalDataSource.insertWorkRequest(entity)
                println("✅ [Repo] Draft insertado")
            } else {
                workRequestLocalDataSource.updateWorkRequest(entity)
                println("🔁 [Repo] Draft actualizado")
            }

            // Intentar sincronizar
            try {
                syncWorkRequest(entity = entity)
            } catch (e: Exception) {
                println("⚠️ [Repo] Sin conexión, work request se sincronizará después")
            }

        } catch (e: Exception) {
            println("❌ [Repo] Error en createWorkRequest: ${e.message}")
            throw e
        }
    }

    /**
     * Obtener reportes de un workOrder
     */
    suspend fun getWorkRequestsByWorkOrder(workOrderId: String): List<WorkRequest> {
        println("📖 [Repo] Obteniendo work requests para: $workOrderId")

        val entities = workRequestLocalDataSource.getWorkRequestsByWorkOrder(workOrderId)

        return entities.map { entity ->
            entity.toDomain()
        }
    }

    /**
     * Sincronizar reportes PENDING
     */
    private suspend fun syncWorkRequest(entity: WorkRequestEntity) {
        try {
            println("📤 [Repo] Sincronizando work request: ${entity.id}")

            val workRequest = buildWorkRequestBody(entity = entity)
            workRequestRemoteRepository.workRequest( request = workRequest )

            // Actualizar a SYNCED
            val syncedEntity = entity.copy(syncStatus = SyncStatusEntity.SYNCED) //"SYNCED"
            workRequestLocalDataSource.updateWorkRequest(syncedEntity)

            println("✅ [Repo] Work request sincronizado")

        } catch (e: Exception) {
            println("❌ [Repo] Error sincronizando work request: ${e.message}")

            // Actualizar a ERROR
            val errorEntity = entity.copy(syncStatus = SyncStatusEntity.PENDING) //"ERROR"
            workRequestLocalDataSource.updateWorkRequest(errorEntity)
            println("⚠️ [Repo] Se mantiene ERROR para reintento futuro")
        }
    }

    /**
     * Sincronizar todos los reportes PENDING
     */
    suspend fun syncPendingWorkRequests() {
        try {
            println("📤 [Repo] Sincronizando work requests PENDING")

            val pendingRequests = workRequestLocalDataSource.getPendingWorkRequests()

            println("📤 [Repo] Work requests PENDING: ${pendingRequests.size}")

            pendingRequests.forEach { entity ->
                try {
                    syncWorkRequest(entity)
                } catch (e: Exception) {
                    println("❌ [Repo] Error sincronizando: ${e.message}")
                    // Continuar con los siguientes
                }
            }

            println("✅ [Repo] Sincronización de work requests completada")

        } catch (e: Exception) {
            println("❌ [Repo] Error en syncPendingWorkRequests: ${e.message}")
            throw e
        }
    }

    private fun buildWorkRequestBody(entity: WorkRequestEntity): RequestBody {
        val jsonObject = JSONObject().apply {
            put("work_order_id", entity.workOrderId)
            put("title", entity.title)
            put("description", entity.description)
            put("findings", entity.findings)
            put("justification", entity.justification)
            put("photo_urls", JSONArray(entity.photoUrls))
            put("request_type", entity.requestType)
            put("requires_customer_approval", entity.requiresCustomerApproval)
            put("vehicle_id", entity.vehicleId)
            put("urgency", entity.urgency.name.lowercase())
        }

        return jsonObject.toString()
            .toRequestBody("application/json".toMediaType())
    }

}