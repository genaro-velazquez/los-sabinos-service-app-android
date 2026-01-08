package com.lossabinos.serviceapp.mappers

import com.lossabinos.domain.entities.Observation
import com.lossabinos.serviceapp.models.ObservationModel

fun Observation.toModel(): ObservationModel =
    ObservationModel(
        id = id,
        description = description,
        responseType = responseType.toModel(),
        requiresResponse = requiresResponse
    )
