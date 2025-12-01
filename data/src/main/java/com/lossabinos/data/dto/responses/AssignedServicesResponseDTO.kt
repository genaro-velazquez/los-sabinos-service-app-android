package com.lossabinos.data.dto.responses

import com.lossabinos.data.dto.entities.MechanicDTO
import com.lossabinos.data.dto.entities.WorkOrderDTO
import com.lossabinos.data.dto.utilities.asJSONArray
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.valueobjects.PaginationDTO
import com.lossabinos.data.dto.valueobjects.SummaryDTO
import com.lossabinos.domain.responses.AssignedServicesResponse
import org.json.JSONObject

open class AssignedServicesResponseDTO(
    json: JSONObject
) : GetBaseResponseDTO<AssignedServicesResponse>(json = json) {

    val mechanic = MechanicDTO(json = json.asJSONObject("data").asJSONObject("mechanic_info"))
    val summary = SummaryDTO(json = json.asJSONObject("data").asJSONObject("summary"))
    val workOrder  = json.asJSONObject("data").asJSONArray("work_orders").let { array ->
        List(size = array.length()) { index ->
            WorkOrderDTO(array.getJSONObject(index))
        }
    }
    val pagination = PaginationDTO(json = json.asJSONObject("data").asJSONObject("pagination"))

    override fun toEntity(): AssignedServicesResponse = AssignedServicesResponse(
        mechanic = mechanic.toEntity(),
        summary = summary.toEntity(),
        pagination = pagination.toEntity(),
        workOrder = workOrder.map { it.toEntity() },
    )
}