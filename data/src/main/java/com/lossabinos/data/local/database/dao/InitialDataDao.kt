package com.lossabinos.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lossabinos.data.local.database.entities.AssignedServiceEntity
import com.lossabinos.data.local.database.entities.AssignedServiceWithProgressEntity
import com.lossabinos.data.local.database.entities.MechanicEntity
import com.lossabinos.data.local.database.entities.ServiceProgressEntity
import com.lossabinos.data.local.database.entities.ServiceTypeEntity
import com.lossabinos.data.local.database.entities.SyncMetadataEntity
import com.lossabinos.data.local.database.entities.VehicleEntity
import com.lossabinos.data.local.database.entities.WorkOrderEntity
import com.lossabinos.data.local.database.entities.ZoneEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InitialDataDao {

    // ============
    // mechanics
    // ============

    @Query("SELECT * FROM mechanics")
    suspend fun getAllMechanics(): List<MechanicEntity>

    @Query("SELECT * FROM mechanics LIMIT 1")
    fun getMechanicFlow(): Flow<MechanicEntity?>

    @Query("SELECT * FROM mechanics LIMIT 1")
    suspend fun getMechanicOnce(): MechanicEntity?

    @Query("SELECT * FROM mechanics WHERE id = :id")
    suspend fun getMechanicById(id: String): MechanicEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMechanics(mechanics: List<MechanicEntity>)

    @Query("DELETE FROM mechanics")
    suspend fun deleteAllMechanics()

    @Delete
    suspend fun deleteMechanic(mechanic: MechanicEntity)

    // ==================
    // assigned_services
    // ==================

    @Query("SELECT * FROM assigned_services")
    fun getAllAssignedServicesFlow(): Flow<List<AssignedServiceEntity>>

    @Query("SELECT * FROM assigned_services")
    suspend fun getAllAssignedServicesOnce(): List<AssignedServiceEntity>

    @Query("SELECT * FROM assigned_services WHERE id = :id")
    suspend fun getAssignedServiceById(id: String): AssignedServiceEntity?

    @Query("SELECT * FROM assigned_services WHERE workOrderId = :workOrderId")
    suspend fun getAssignedServicesByWorkOrder(workOrderId: String): List<AssignedServiceEntity>

    // Actualizar un servicio (no usar REPLACE)
    @Update
    suspend fun updateAssignedService(service: AssignedServiceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignedServices(assignedServices: List<AssignedServiceEntity>)

    @Query("DELETE FROM assigned_services")
    suspend fun deleteAllAssignedServices()

    @Delete
    suspend fun deleteAssignedService(assignedService: AssignedServiceEntity)

    //===========================================
    // ServiceProgressEntity -> service_progress
    //===========================================
    // Insertar/actualizar progreso
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServiceProgress(progress: ServiceProgressEntity)

    // Obtener todos los servicios con su progreso
    @Query("""
        SELECT 
            a.*,
            COALESCE(sp.completedActivities, 0) as completedActivities,
            COALESCE(sp.totalActivities, 0) as totalActivities,
            COALESCE(sp.completedPercentage, 0) as completedPercentage,
            COALESCE(sp.syncStatus, 'PENDING') as syncStatus,
            COUNT(DISTINCT ap.id) as activityProgressCount,
            SUM(CASE WHEN ap.completed = 1 THEN 1 ELSE 0 END) as completedCount
        FROM assigned_services a
        LEFT JOIN service_progress sp ON a.id = sp.assignedServiceId
        LEFT JOIN activity_progress ap ON a.id = ap.assignedServiceId
        GROUP BY a.id
        ORDER BY a.status
    """)
    fun getAllAssignedServicesWithProgressFlow(): Flow<List<AssignedServiceWithProgressEntity>>

    // ==============
    // service_types
    // ==============

    @Query("SELECT * FROM service_types")
    fun getAllServiceTypesFlow(): Flow<List<ServiceTypeEntity>>

    @Query("SELECT * FROM service_types")
    suspend fun getAllServiceTypes(): List<ServiceTypeEntity>

    @Query("SELECT * FROM service_types")
    suspend fun getAllServiceTypesOnce(): List<ServiceTypeEntity>

    @Query("SELECT * FROM service_types WHERE id = :id")
    suspend fun getServiceTypeById(id: String): ServiceTypeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServiceTypes(serviceTypes: List<ServiceTypeEntity>)

    @Query("DELETE FROM service_types")
    suspend fun deleteAllServiceTypes()

    @Delete
    suspend fun deleteServiceType(serviceType: ServiceTypeEntity)

    // ==============
    // sync_metadata
    // ==============

    @Query("SELECT * FROM sync_metadata WHERE id = 'sync_metadata' LIMIT 1")
    fun getSyncMetadataFlow(): Flow<SyncMetadataEntity?>

    @Query("SELECT * FROM sync_metadata WHERE id = 'sync_metadata' LIMIT 1")
    suspend fun getSyncMetadataOnce(): SyncMetadataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncMetadata(syncMetadata: SyncMetadataEntity)

    @Update
    suspend fun updateSyncMetadata(syncMetadata: SyncMetadataEntity)

    @Query("DELETE FROM sync_metadata")
    suspend fun deleteSyncMetadata()

    // ==============
    // zones *++SIN USO AÚN
    // ==============

    @Query("SELECT * FROM zones")
    suspend fun getAllZones(): List<ZoneEntity>

    @Query("SELECT * FROM zones WHERE id = :id")
    suspend fun getZoneById(id: String): ZoneEntity?

    @Insert
    suspend fun insertZones(zones: List<ZoneEntity>)

    @Query("DELETE FROM zones")
    suspend fun deleteAllZones()

    @Delete
    suspend fun deleteZone(zone: ZoneEntity)

    // ==============
    // vehicle
    // ==============

    @Query("SELECT * FROM vehicles")
    suspend fun getAllVehicles(): List<VehicleEntity>

    @Query("SELECT * FROM vehicles WHERE id = :id")
    suspend fun getVehicleById(id: String): VehicleEntity?

    @Insert
    suspend fun insertVehicles(vehicles: List<VehicleEntity>)

    @Delete
    suspend fun deleteVehicle(vehicle: VehicleEntity)

    @Query("DELETE FROM vehicles")
    suspend fun deleteAllVehicles()

    // ==============
    // work_orders  *++SIN USO AÚN
    // ==============

    @Query("SELECT * FROM work_orders")
    suspend fun getAllWorkOrders(): List<WorkOrderEntity>

    @Query("SELECT * FROM work_orders WHERE id = :id")
    suspend fun getWorkOrderById(id: String): WorkOrderEntity?

    @Insert
    suspend fun insertWorkOrders(workOrders: List<WorkOrderEntity>)

    @Query("DELETE FROM work_orders")
    suspend fun deleteAllWorkOrders()

    @Delete
    suspend fun deleteWorkOrder(workOrder: WorkOrderEntity)

}