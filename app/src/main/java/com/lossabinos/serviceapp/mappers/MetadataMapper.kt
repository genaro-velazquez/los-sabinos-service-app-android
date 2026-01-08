package com.lossabinos.serviceapp.mappers

import com.lossabinos.domain.entities.Metadata
import com.lossabinos.serviceapp.models.MetadataModel

fun Metadata.toModel(): MetadataModel = MetadataModel(
    id = id,
    name = name,
    value = value
)