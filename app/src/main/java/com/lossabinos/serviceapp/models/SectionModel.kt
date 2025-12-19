package com.lossabinos.serviceapp.models

import com.lossabinos.domain.valueobjects.Activity
import com.lossabinos.domain.valueobjects.Section

data class SectionModel(
    val name: String,
    val metadata: List<String>,
    val optional: Boolean,
    val activities: List<ActivityModel>,
    val observations: List<String>
) : Model(){
    constructor(section: Section) : this (
        name = section.name,
        metadata = section.metadata,
        optional = section.optional,
        activities = section.activities.map { ActivityModel(activity = it) },
        observations = section.observations
    )
}