package com.lossabinos.data.datasource.local

import com.lossabinos.data.datasource.local.database.dao.ActivityEvidenceDao
import com.lossabinos.data.datasource.local.database.dao.ActivityProgressDao
import com.lossabinos.data.datasource.local.database.dao.ExtraCostDao
import com.lossabinos.data.datasource.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.datasource.local.database.entities.ActivityProgressEntity
import com.lossabinos.data.datasource.local.database.entities.ExtraCostEntity
import com.lossabinos.domain.usecases.checklist.CreateReportExtraCostUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChecklistLocalDataSource @Inject constructor(
    private val activityProgressDao: ActivityProgressDao,
    private val activityEvidenceDao: ActivityEvidenceDao,
    private val extraCostDao: ExtraCostDao
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

}