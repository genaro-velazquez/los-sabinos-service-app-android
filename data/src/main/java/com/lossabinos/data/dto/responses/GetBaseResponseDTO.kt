package com.lossabinos.data.dto.responses

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.domain.entities.DomainEntity
import org.json.JSONObject

abstract class GetBaseResponseDTO<E : DomainEntity>(json: JSONObject) : DTO<E>()

/*
abstract class GetBaseResponseDto<E : DomainEntity>(json: JSONObject) : DTO<E>() {
    val metaData = MetaDataDTO(json.asJSONObject("meta_data"))
}
*/