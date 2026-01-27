package com.lossabinos.data.datasource.local

import com.lossabinos.data.datasource.local.database.dao.ActivityEvidenceDao
import com.lossabinos.data.datasource.local.database.dao.ActivityProgressDao
import com.lossabinos.data.datasource.local.database.dao.ExtraCostDao
import com.lossabinos.data.datasource.local.database.dao.ServiceStartDao
import com.lossabinos.data.datasource.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.datasource.local.database.entities.ActivityProgressEntity
import com.lossabinos.data.datasource.local.database.entities.ExtraCostEntity
import com.lossabinos.data.datasource.local.database.entities.ServiceStartEntity
import com.lossabinos.domain.usecases.checklist.CreateReportExtraCostUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChecklistLocalDataSource @Inject constructor(
    private val activityProgressDao: ActivityProgressDao,
    private val activityEvidenceDao: ActivityEvidenceDao,
    private val extraCostDao: ExtraCostDao,
    private val serviceStartDao: ServiceStartDao
) {

    suspend fun getActivityProgressByService(
        serviceId: String
    ) :  List<ActivityProgressEntity>{
        return activityProgressDao.getActivityProgressByService(serviceId)
    }

    suspend fun getEvidenceByActivityProgress(
        activityId: Long
    ) :  List<ActivityEvidenceEntity> {
        return activityEvidenceDao.getEvidenceByActivityProgress(activityProgressId = activityId)
    }

    /** ExtraCostEntity **/
    suspend fun insertExtraCost(entity: ExtraCostEntity){
        extraCostDao.insertExtraCost(extraCost = entity)
    }

    fun getExtraCostbyServiceFlow(
        assignedServiceId: String
    ): Flow<List<ExtraCostEntity>>{
        return extraCostDao.getExtraCostsByServiceFlow(assignedServiceId = assignedServiceId)
    }

    suspend fun updateExtraCost(entity: ExtraCostEntity){
        extraCostDao.updateExtraCost(entity)
    }

    suspend fun deleteExtrCostById(id: String){
        extraCostDao.deleteExtraCostById(id = id)
    }

    /**
     * ServiceStartEntity - Guardar registro de inicio de servicio
     */
    suspend fun insertServiceStart(serviceStart: ServiceStartEntity) {
        println("💾 [LocalDataSource] Guardando service start: ${serviceStart.assignedServiceId}")
        serviceStartDao.insertServiceStart(serviceStart)
        println("✅ [LocalDataSource] Service start guardado")
    }

    /**
     * ServiceStartEntity - Obtener registro de inicio de un servicio
     */
    suspend fun getServiceStartByService(assignedServiceId: String): ServiceStartEntity? {
        println("📖 [LocalDataSource] Obteniendo service start para: $assignedServiceId")
        return serviceStartDao.getServiceStartByService(assignedServiceId)
    }

    /**
     * ServiceStartEntity - Obtener todos los service start PENDING
     */
    suspend fun getPendingServiceStarts(): List<ServiceStartEntity> {
        println("📖 [LocalDataSource] Obteniendo todos los service starts PENDING")
        return serviceStartDao.getPendingServiceStarts()
    }

    /**
     * ServiceStartEntity - Actualizar registro de inicio (cambiar status)
     */
    suspend fun updateServiceStart(serviceStart: ServiceStartEntity) {
        println("✏️ [LocalDataSource] Actualizando service start: ${serviceStart.assignedServiceId}")
        serviceStartDao.updateServiceStart(serviceStart)
        println("✅ [LocalDataSource] Service start actualizado")
    }

    /**
     * ServiceStartEntity - Eliminar registro de inicio
     */
    suspend fun deleteServiceStartByService(assignedServiceId: String) {
        println("🗑️ [LocalDataSource] Eliminando service start: $assignedServiceId")
        serviceStartDao.deleteServiceStartByService(assignedServiceId)
        println("✅ [LocalDataSource] Service start eliminado")
    }
}