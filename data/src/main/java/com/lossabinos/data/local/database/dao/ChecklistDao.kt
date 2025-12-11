package com.lossabinos.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Query
import com.lossabinos.data.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.local.database.entities.ActivityProgressEntity
import com.lossabinos.data.local.database.entities.ObservationResponseEntity
import com.lossabinos.data.local.database.entities.ServiceFieldValueEntity


// 1️⃣ DAO para Activity Progress
@Dao
interface ActivityProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityProgress(activity: ActivityProgressEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityProgressList(activities: List<ActivityProgressEntity>)

    @Update
    suspend fun updateActivityProgress(activity: ActivityProgressEntity)

    @Query("""
        SELECT * FROM activity_progress 
        WHERE assignedServiceId = :serviceId
        ORDER BY sectionIndex, activityIndex
    """)
    suspend fun getActivityProgressByService(serviceId: String): List<ActivityProgressEntity>

    @Query("""
        SELECT * FROM activity_progress 
        WHERE assignedServiceId = :serviceId 
        AND sectionIndex = :sectionIndex 
        AND activityIndex = :activityIndex
    """)
    suspend fun getActivityProgress(
        serviceId: String,
        sectionIndex: Int,
        activityIndex: Int
    ): ActivityProgressEntity?

    @Query("""
        SELECT COUNT(*) FROM activity_progress 
        WHERE assignedServiceId = :serviceId AND completed = 1
    """)
    suspend fun getCompletedCount(serviceId: String): Int

    @Query("""
        SELECT COUNT(*) FROM activity_progress 
        WHERE assignedServiceId = :serviceId
    """)
    suspend fun getTotalActivitiesCount(serviceId: String): Int

    @Delete
    suspend fun deleteActivityProgress(activity: ActivityProgressEntity)
}

// 2️⃣ DAO para Activity Evidence
@Dao
interface ActivityEvidenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvidence(evidence: ActivityEvidenceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvidenceList(evidences: List<ActivityEvidenceEntity>)

    @Query("""
        SELECT * FROM activity_evidence 
        WHERE activityProgressId = :activityProgressId
    """)
    suspend fun getEvidenceByActivity(activityProgressId: Long): List<ActivityEvidenceEntity>

    @Query("""
        SELECT * FROM activity_evidence 
        WHERE activityProgressId IN (
            SELECT id FROM activity_progress 
            WHERE assignedServiceId = :serviceId
        )
    """)
    suspend fun getEvidenceByService(serviceId: String): List<ActivityEvidenceEntity>

    @Delete
    suspend fun deleteEvidence(evidence: ActivityEvidenceEntity)
}

// 3️⃣ DAO para Observation Response
@Dao
interface ObservationResponseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservationResponse(response: ObservationResponseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservationResponseList(responses: List<ObservationResponseEntity>)

    @Update
    suspend fun updateObservationResponse(response: ObservationResponseEntity)

    @Query("""
        SELECT * FROM observation_response 
        WHERE assignedServiceId = :serviceId
        ORDER BY sectionIndex, observationIndex
    """)
    suspend fun getObservationsByService(serviceId: String): List<ObservationResponseEntity>

    @Query("""
        SELECT * FROM observation_response 
        WHERE assignedServiceId = :serviceId 
        AND sectionIndex = :sectionIndex 
        AND observationIndex = :observationIndex
    """)
    suspend fun getObservationResponse(
        serviceId: String,
        sectionIndex: Int,
        observationIndex: Int
    ): ObservationResponseEntity?

    @Delete
    suspend fun deleteObservationResponse(response: ObservationResponseEntity)
}

// 4️⃣ DAO para Service Field Value
@Dao
interface ServiceFieldValueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFieldValue(fieldValue: ServiceFieldValueEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFieldValueList(fieldValues: List<ServiceFieldValueEntity>)

    @Update
    suspend fun updateFieldValue(fieldValue: ServiceFieldValueEntity)

    @Query("""
        SELECT * FROM service_field_value 
        WHERE assignedServiceId = :serviceId
        ORDER BY fieldIndex
    """)
    suspend fun getFieldValuesByService(serviceId: String): List<ServiceFieldValueEntity>

    @Query("""
        SELECT * FROM service_field_value 
        WHERE assignedServiceId = :serviceId AND fieldIndex = :fieldIndex
    """)
    suspend fun getFieldValue(
        serviceId: String,
        fieldIndex: Int
    ): ServiceFieldValueEntity?

    @Delete
    suspend fun deleteFieldValue(fieldValue: ServiceFieldValueEntity)
}
