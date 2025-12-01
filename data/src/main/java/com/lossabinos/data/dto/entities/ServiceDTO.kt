package com.lossabinos.data.dto.entities

import com.lossabinos.data.dto.utilities.asBoolean
import com.lossabinos.data.dto.utilities.asInt
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.entities.Service
import org.json.JSONObject

class ServiceDTO(
    val id: String,
    val executionId: String,
    val serviceType: ServiceTypeDTO,
    val status: String,
    val priority: String,
    val scheduledStart: String?,
    val scheduledEnd: String?,
    val actualStart:String?,
    val actualEnd:String?,
    val progressPercentage: Int,
    val checklistTemplateId:String?,
    val hasTemplate: Boolean,
    val hasStarted: Boolean,
    val checklistItemsCompleted:Int,
    val checklistItemsTotal:Int,
    val notes: String
) : DTO<Service>(){

    constructor(json: JSONObject) : this (
        id                      = json.asString("service_id"),
        executionId             = json.asString("service_execution_id"),
        serviceType             = ServiceTypeDTO(json = json.asJSONObject("service_type")),
        status                  = json.asString("status"),
        priority                = json.asString("priority"),
        scheduledStart          = json.asString("scheduled_start"),
        scheduledEnd            = json.asString("scheduled_end"),
        actualStart             = json.asString("actual_start"),
        actualEnd               = json.asString("actual_end"),
        progressPercentage      = json.asInt("progress_percentage"),
        checklistTemplateId     = json.asString("checklist_template_id"),
        hasTemplate             = json.asBoolean("has_template"),
        hasStarted              = json.asBoolean("has_started"),
        checklistItemsCompleted = json.asInt("checklist_items_completed"),
        checklistItemsTotal     = json.asInt("checklist_items_total"),
        notes                   = json.asString("notes")
    )

    override fun toEntity(): Service = Service(
        id                      = id,
        executionId             = executionId,
        serviceType             = serviceType.toEntity() ,
        status                  = status,
        priority                = priority,
        scheduledStart          = scheduledStart,
        scheduledEnd            = scheduledEnd,
        actualStart             = actualStart,
        actualEnd               = actualEnd,
        progressPercentage      = progressPercentage,
        checklistTemplateId     = checklistTemplateId,
        hasTemplate             = hasTemplate,
        hasStarted              = hasStarted,
        checklistItemsCompleted = checklistItemsCompleted,
        checklistItemsTotal     = checklistItemsTotal,
        notes                   = notes
    )
}