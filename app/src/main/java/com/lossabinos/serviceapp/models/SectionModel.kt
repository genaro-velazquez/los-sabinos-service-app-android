package com.lossabinos.serviceapp.models

import com.lossabinos.domain.valueobjects.Activity
import com.lossabinos.domain.valueobjects.Section
import com.lossabinos.serviceapp.mappers.toModel

data class SectionModel(
    val name: String,
    val metadata: List<MetadataModel>,
    val optional: Boolean,
    val activities: List<ActivityModel>,
    val observations: List<ObservationModel>
) : Model(){
    constructor(section: Section) : this (
        name = section.name,
        metadata = section.metadata.map { it.toModel() },
        optional = section.optional,
        activities = section.activities.map { ActivityModel(activity = it) },
        observations = section.observations.map { it.toModel() }
    )
}
