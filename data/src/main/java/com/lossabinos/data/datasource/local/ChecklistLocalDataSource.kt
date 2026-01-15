package com.lossabinos.data.datasource.local

import com.lossabinos.data.datasource.local.database.dao.ActivityEvidenceDao
import com.lossabinos.data.datasource.local.database.dao.ActivityProgressDao
import com.lossabinos.data.datasource.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.datasource.local.database.entities.ActivityProgressEntity
import javax.inject.Inject

class ChecklistLocalDataSource @Inject constructor(
    private val activityProgressDao: ActivityProgressDao,
    private val activityEvidenceDao: ActivityEvidenceDao
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


}