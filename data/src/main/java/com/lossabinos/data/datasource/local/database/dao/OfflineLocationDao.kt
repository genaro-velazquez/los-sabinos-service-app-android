package com.lossabinos.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lossabinos.data.datasource.local.database.entities.OfflineLocationEntity

@Dao
interface OfflineLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: OfflineLocationEntity)

    @Query("SELECT * FROM offline_locations ORDER BY createdAt ASC")
    suspend fun getAll(): List<OfflineLocationEntity>

    @Delete
    suspend fun delete(entity: OfflineLocationEntity)
}
