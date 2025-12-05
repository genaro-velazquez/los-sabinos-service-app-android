package com.lossabinos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lossabinos.data.local.database.dao.InitialDataDao
import com.lossabinos.data.local.database.entities.AssignedServiceEntity
import com.lossabinos.data.local.database.entities.MechanicEntity
import com.lossabinos.data.local.database.entities.ServiceTypeEntity
import com.lossabinos.data.local.database.entities.VehicleEntity
import com.lossabinos.data.local.database.entities.WorkOrderEntity
import com.lossabinos.data.local.database.entities.ZoneEntity

@Database(
    entities = [
        ServiceTypeEntity::class,
        ZoneEntity::class,
        VehicleEntity::class,
        MechanicEntity::class,
        WorkOrderEntity::class,
        AssignedServiceEntity::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun initialDataDao(): InitialDataDao
}
