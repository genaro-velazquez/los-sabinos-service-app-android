package com.lossabinos.serviceapp.models

import com.lossabinos.domain.entities.Observation
import com.lossabinos.serviceapp.enums.ResponseTypeModel

class ObservationModel(
    val id: String,
    val description: String,
    val responseType: ResponseTypeModel,
    val requiresResponse: Boolean
) : Model()