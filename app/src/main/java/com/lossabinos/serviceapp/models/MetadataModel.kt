package com.lossabinos.serviceapp.models

import android.view.Display
import com.lossabinos.domain.entities.Metadata

class MetadataModel (
    val id:String,
    val name: String,
    val value: String
): Model(){
    constructor(
        metadata: Metadata
    ) : this (
        id = metadata.id,
        name = metadata.name,
        value = metadata.value
    )
}