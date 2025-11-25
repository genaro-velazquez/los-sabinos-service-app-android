package com.lossabinos.data.dto.utilities

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.domain.entities.DomainEntity
import org.json.JSONObject

interface EntityBuilder<E: DomainEntity, D: DTO<E>> {
    fun build(json: JSONObject): D
}