package com.lossabinos.data.dto.entities

import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.data.dto.valueobjects.ChecklistTemplateDTO
import com.lossabinos.data.dto.valueobjects.TemplateDTO
import com.lossabinos.domain.entities.AssignedService
import org.json.JSONObject

open class AssignedServiceDTO (
    val id:String,
    val workOrderId: String,
    val workOrderNumber:String,
    val serviceTypeId:String,
    val serviceTypeName:String,
    val vehicle: VehicleDTO,
    val status: String,
    val scheduledStart: String,
    val scheduledEnd: String,
    val priority: String,
    val checklistTemplate: ChecklistTemplateDTO
): DTO<AssignedService>(){

    constructor(json: JSONObject) : this (
        id = json.asString("id"),
        workOrderId = json.asString("work_order_id"),
        workOrderNumber = json.asString("work_order_number"),
        serviceTypeId = json.asString("service_type_id"),
        serviceTypeName = json.asString("service_type_name"),
        vehicle = VehicleDTO(json = json.asJSONObject("vehicle")),
        status = json.asString("status"),
        scheduledStart = json.asString("scheduled_start"),
        scheduledEnd = json.asString("scheduled_end"),
        priority = json.asString("priority"),
        checklistTemplate = ChecklistTemplateDTO(json = json.asJSONObject("checklist_template"))
    )

    override fun toEntity(): AssignedService = AssignedService(
        id = id,
        workOrderId = workOrderId,
        workOrderNumber = workOrderNumber,
        serviceTypeId = serviceTypeId,
        serviceTypeName = serviceTypeName,
        vehicle = vehicle.toEntity(),
        status = status,
        scheduledStart = scheduledStart,
        scheduledEnd = scheduledEnd,
        priority = priority,
        checklistTemplate = checklistTemplate.toEntity()
    )
}