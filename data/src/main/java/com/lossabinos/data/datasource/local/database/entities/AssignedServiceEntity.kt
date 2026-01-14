package com.lossabinos.data.datasource.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "assigned_services"
)
data class AssignedServiceEntity(
    @PrimaryKey
    val id: String,
    val workOrderId: String,
    val workOrderNumber:String,
    val serviceTypeId: String,
    val serviceTypeName:String,
    // vehicle
    val vehicleId:String,
    val vehicleVin:String,
    val vehicleEconomicNumber: String,
    val vehicleModelName:String,
    val status: String,
    val priority: String,
    val notes: String? = null,
    val scheduledStart: String? = null,
    val scheduledEnd: String? = null,
    val checklistTemplateJson: String? = null,
    val progressPercentage: Int = 0
)
