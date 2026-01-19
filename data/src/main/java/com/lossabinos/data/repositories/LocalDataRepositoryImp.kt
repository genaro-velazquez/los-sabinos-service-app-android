package com.lossabinos.data.repositories

import com.lossabinos.data.datasource.local.database.dao.ActivityEvidenceDao
import com.lossabinos.data.datasource.local.database.dao.ActivityProgressDao
import com.lossabinos.data.datasource.local.database.dao.InitialDataDao
import com.lossabinos.data.datasource.local.database.dao.ObservationResponseDao
import com.lossabinos.data.datasource.local.database.dao.ServiceFieldValueDao
import com.lossabinos.domain.repositories.LocalDataRepository

class LocalDataRepositoryImp(
    private val initialDataDao: InitialDataDao,
    private val activityProgressDao: ActivityProgressDao,
    private val activityEvidenceDao: ActivityEvidenceDao,
    private val observationResponseDao: ObservationResponseDao,
    private val serviceFieldValueDao: ServiceFieldValueDao
) : LocalDataRepository {

    override suspend fun clearAll() {
        initialDataDao.deleteAllData()
        activityProgressDao.deleteAllActivityProgress()
        activityProgressDao.deleteAllServiceProgress()
        activityEvidenceDao.deleteAllActivityEvidences()
        observationResponseDao.deleteAllObservationResponses()
        serviceFieldValueDao.deleteAllServiceFieldValues()
    }
}