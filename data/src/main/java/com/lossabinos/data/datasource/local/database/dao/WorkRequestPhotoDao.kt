package com.lossabinos.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lossabinos.data.datasource.local.database.entities.SyncStatusEntity
import com.lossabinos.data.datasource.local.database.entities.WorkRequestPhotoEntity


@Dao
interface WorkRequestPhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: WorkRequestPhotoEntity)

    @Query("""
        SELECT * FROM work_request_photo
        WHERE workRequestId = :workRequestId
    """)
    suspend fun getPhotosForWorkRequest(
        workRequestId: String
    ): List<WorkRequestPhotoEntity>

    @Query("""
        SELECT * FROM work_request_photo
        WHERE syncStatus = :status
    """)
    suspend fun getBySyncStatus(
        status: SyncStatusEntity
    ): List<WorkRequestPhotoEntity>

    @Query("""
        DELETE FROM work_request_photo
        WHERE id = :photoId
    """)
    suspend fun deleteById(photoId: String)

}
