package com.lossabinos.serviceapp.mappers

import com.lossabinos.domain.enums.ResponseType
import com.lossabinos.serviceapp.enums.ResponseTypeModel


fun ResponseType.toModel(): ResponseTypeModel = when (this) {
    ResponseType.TEXTAREA  -> ResponseTypeModel.TEXTAREA
    ResponseType.NUMBER    -> ResponseTypeModel.NUMBER
    ResponseType.BOOLEAN  -> ResponseTypeModel.BOOLEAN
    ResponseType.UNDEFINED -> ResponseTypeModel.UNDEFINED
}

/*
interface responseTypeMapper<E, M> {
    fun E.toModel(): M
}
*/