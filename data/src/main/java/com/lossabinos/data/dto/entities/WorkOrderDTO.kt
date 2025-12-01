package com.lossabinos.data.dto.entities

import com.lossabinos.data.dto.utilities.asJSONArray
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.entities.WorkOrder
import org.json.JSONObject

class WorkOrderDTO(
    val orderId: String,
    val orderNumber: String,
    val orderStatus:String,
    val priority:String,
    val vehicule: VehiculeDTO,
    val zone: ZoneDTO,
    val scheduledDate:String,
    val scheduledEnd: String,
    val assignedServices: List<ServiceDTO>
) : DTO<WorkOrder>() {

    constructor(json: JSONObject) : this (
        orderId = json.asString("work_order_id"),
        orderNumber = json.asString("work_order_number"),
        orderStatus = json.asString("work_order_status"),
        priority = json.asString("priority"),
        vehicule = VehiculeDTO(json = json.asJSONObject("vehicle")),
        zone = ZoneDTO(json = json.asJSONObject("zone")),
        scheduledDate = json.asString("scheduled_date"),
        scheduledEnd = json.asString("scheduled_end"),
        assignedServices = json.asJSONArray("assigned_services")
            .let { array ->
                List(array.length()) { index ->
                    ServiceDTO(json = array.getJSONObject(index))
                }
            }
    )

    override fun toEntity(): WorkOrder = WorkOrder(
        orderId = orderId,
        orderNumber = orderNumber,
        orderStatus = orderStatus,
        priority = priority,
        vehicule = vehicule.toEntity(),
        zone = zone.toEntity(),
        scheduledDate = scheduledDate,
        scheduledEnd = scheduledEnd,
        assignedServices = assignedServices.map {  it.toEntity() }
    )
}