package com.lossabinos.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lossabinos.data.datasource.local.database.dao.ActivityEvidenceDao
import com.lossabinos.data.datasource.local.database.dao.ActivityProgressDao
import com.lossabinos.data.datasource.local.database.dao.InitialDataDao
import com.lossabinos.data.datasource.local.database.dao.ObservationResponseDao
import com.lossabinos.data.datasource.local.database.dao.ServiceFieldValueDao
import com.lossabinos.data.datasource.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.datasource.local.database.entities.ActivityProgressEntity
import com.lossabinos.data.datasource.local.database.entities.AssignedServiceEntity
import com.lossabinos.data.datasource.local.database.entities.MechanicEntity
import com.lossabinos.data.datasource.local.database.entities.ObservationResponseEntity
import com.lossabinos.data.datasource.local.database.entities.ServiceFieldValueEntity
import com.lossabinos.data.datasource.local.database.entities.ServiceProgressEntity
import com.lossabinos.data.datasource.local.database.entities.ServiceTypeEntity
import com.lossabinos.data.datasource.local.database.entities.SyncMetadataEntity
import com.lossabinos.data.datasource.local.database.entities.VehicleEntity
import com.lossabinos.data.datasource.local.database.entities.WorkOrderEntity
import com.lossabinos.data.datasource.local.database.entities.ZoneEntity

@Database(
    entities = [
        ServiceTypeEntity::class,
        ZoneEntity::class,
        VehicleEntity::class,
        MechanicEntity::class,
        WorkOrderEntity::class,
        AssignedServiceEntity::class,
        ActivityProgressEntity::class,
        ActivityEvidenceEntity::class,
        ObservationResponseEntity::class,
        ServiceFieldValueEntity::class,
        SyncMetadataEntity::class,
        ServiceProgressEntity::class
    ],
    version = 8
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun initialDataDao(): InitialDataDao
    abstract fun activityProgressDao(): ActivityProgressDao
    abstract fun activityEvidenceDao(): ActivityEvidenceDao
    abstract fun observationResponseDao(): ObservationResponseDao
    abstract fun serviceFieldValueDao(): ServiceFieldValueDao
}
