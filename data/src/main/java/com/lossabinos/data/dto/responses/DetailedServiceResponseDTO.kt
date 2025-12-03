package com.lossabinos.data.dto.responses

import com.lossabinos.data.dto.entities.ServiceTypeDTO
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.data.dto.valueobjects.CurrentProgressDTO
import com.lossabinos.data.dto.valueobjects.ServiceInfoDTO
import com.lossabinos.data.dto.valueobjects.TemplateDTO
import com.lossabinos.domain.responses.DetailedServiceResponse
import org.json.JSONObject

open class DetailedServiceResponseDTO(
    json : JSONObject
) : GetBaseResponseDTO<DetailedServiceResponse>(json = json) {

    val serviceExecutionId  = json.asJSONObject("data").asString("service_execution_id")
    val serviceId           = json.asJSONObject("data").asString("service_id")
    val serviceType         = ServiceTypeDTO(json = json.asJSONObject("data").asJSONObject("service_type"))
    val template            = TemplateDTO(json = json.asJSONObject("data").asJSONObject("template"))
    val currentProgress     = CurrentProgressDTO(json = json.getJSONObject("data").asJSONObject("current_progress"))
    val serviceInfo         = ServiceInfoDTO(json = json.getJSONObject("data").asJSONObject("current_progress"))

    override fun toEntity(): DetailedServiceResponse = DetailedServiceResponse(
        serviceExecutionId = serviceExecutionId,
        serviceId = serviceId,
        serviceType = serviceType.toEntity(),
        template = template.toEntity(),
        currentProgress = currentProgress.toEntity(),
        serviceInfo = serviceInfo.toEntity()
    )
}