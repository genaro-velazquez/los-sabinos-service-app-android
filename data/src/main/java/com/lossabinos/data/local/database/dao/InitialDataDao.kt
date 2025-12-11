package com.lossabinos.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lossabinos.data.local.database.entities.AssignedServiceEntity
import com.lossabinos.data.local.database.entities.MechanicEntity
import com.lossabinos.data.local.database.entities.ServiceTypeEntity
import com.lossabinos.data.local.database.entities.SyncMetadataEntity
import com.lossabinos.data.local.database.entities.VehicleEntity
import com.lossabinos.data.local.database.entities.WorkOrderEntity
import com.lossabinos.data.local.database.entities.ZoneEntity

@Dao
interface InitialDataDao {

    // ============ SERVICE TYPES ============

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServiceTypes(serviceTypes: List<ServiceTypeEntity>)

    @Query("SELECT * FROM service_types")
    suspend fun getAllServiceTypes(): List<ServiceTypeEntity>

    @Query("SELECT * FROM service_types WHERE id = :id")
    suspend fun getServiceTypeById(id: String): ServiceTypeEntity?

    @Delete
    suspend fun deleteServiceType(serviceType: ServiceTypeEntity)


    // ============ ZONES ============

    @Insert
    suspend fun insertZones(zones: List<ZoneEntity>)

    @Query("SELECT * FROM zones")
    suspend fun getAllZones(): List<ZoneEntity>

    @Query("SELECT * FROM zones WHERE id = :id")
    suspend fun getZoneById(id: String): ZoneEntity?

    @Delete
    suspend fun deleteZone(zone: ZoneEntity)


    // ============ VEHICLES ============

    @Insert
    suspend fun insertVehicles(vehicles: List<VehicleEntity>)

    @Query("SELECT * FROM vehicles")
    suspend fun getAllVehicles(): List<VehicleEntity>

    @Query("SELECT * FROM vehicles WHERE id = :id")
    suspend fun getVehicleById(id: String): VehicleEntity?

    @Delete
    suspend fun deleteVehicle(vehicle: VehicleEntity)


    // ============ MECHANICS ============

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMechanics(mechanics: List<MechanicEntity>)

    @Query("SELECT * FROM mechanics")
    suspend fun getAllMechanics(): List<MechanicEntity>

    @Query("SELECT * FROM mechanics WHERE id = :id")
    suspend fun getMechanicById(id: String): MechanicEntity?

    @Delete
    suspend fun deleteMechanic(mechanic: MechanicEntity)


    // ============ WORK ORDERS ============

    @Insert
    suspend fun insertWorkOrders(workOrders: List<WorkOrderEntity>)

    @Query("SELECT * FROM work_orders")
    suspend fun getAllWorkOrders(): List<WorkOrderEntity>

    @Query("SELECT * FROM work_orders WHERE id = :id")
    suspend fun getWorkOrderById(id: String): WorkOrderEntity?

    @Query("SELECT * FROM work_orders WHERE vehicleId = :vehicleId")
    suspend fun getWorkOrdersByVehicle(vehicleId: String): List<WorkOrderEntity>

    @Delete
    suspend fun deleteWorkOrder(workOrder: WorkOrderEntity)


    // ============ ASSIGNED SERVICES ============

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignedServices(assignedServices: List<AssignedServiceEntity>)

    @Query("SELECT * FROM assigned_services")
    suspend fun getAllAssignedServices(): List<AssignedServiceEntity>

    @Query("SELECT * FROM assigned_services WHERE id = :id")
    suspend fun getAssignedServiceById(id: String): AssignedServiceEntity?

    @Query("SELECT * FROM assigned_services WHERE workOrderId = :workOrderId")
    suspend fun getAssignedServicesByWorkOrder(workOrderId: String): List<AssignedServiceEntity>

    @Delete
    suspend fun deleteAssignedService(assignedService: AssignedServiceEntity)


    // ============ UTILITY FUNCTIONS ============

    @Query("DELETE FROM service_types")
    suspend fun deleteAllServiceTypes()

    @Query("DELETE FROM zones")
    suspend fun deleteAllZones()

    @Query("DELETE FROM vehicles")
    suspend fun deleteAllVehicles()

    @Query("DELETE FROM mechanics")
    suspend fun deleteAllMechanics()

    @Query("DELETE FROM work_orders")
    suspend fun deleteAllWorkOrders()

    @Query("DELETE FROM assigned_services")
    suspend fun deleteAllAssignedServices()

    // ============ QUERIES PARA LECTURA ============
    @Query("SELECT * FROM mechanics LIMIT 1")
    suspend fun getMechanic(): MechanicEntity?

    // ============ SYNC METADATA ============
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncMetadata(syncMetadata: SyncMetadataEntity)

    @Query("SELECT * FROM sync_metadata WHERE id = 'sync_metadata' LIMIT 1")
    suspend fun getSyncMetadata(): SyncMetadataEntity?

    @Query("DELETE FROM sync_metadata")
    suspend fun deleteSyncMetadata()

    @Update
    suspend fun updateSyncMetadata(syncMetadata: SyncMetadataEntity)

}