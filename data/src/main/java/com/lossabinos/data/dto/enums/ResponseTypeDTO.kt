package com.lossabinos.data.dto.enums

import com.lossabinos.domain.enums.ResponseType
import kotlinx.serialization.descriptors.PrimitiveKind
import retrofit2.Response

enum class ResponseTypeDTO(val entity: ResponseType) {
    TEXTAREA(ResponseType.TEXTAREA),
    NUMBER(ResponseType.NUMBER),
    BOOLEAN(ResponseType.BOOLEAN),
    UNDEFINED(ResponseType.UNDEFINED);

    companion object {

        fun fromApi(value: String?): ResponseTypeDTO {
            return try {
                value
                    ?.uppercase()
                    ?.let { enumValueOf<ResponseTypeDTO>(it) }
                    ?: UNDEFINED
            } catch (e: Exception) {
                UNDEFINED
            }
        }

        fun fromEntity(type: ResponseType): ResponseTypeDTO = when (type){
            ResponseType.TEXTAREA -> TEXTAREA
            ResponseType.BOOLEAN -> BOOLEAN
            ResponseType.NUMBER -> NUMBER
            else -> UNDEFINED
        }
    }
}