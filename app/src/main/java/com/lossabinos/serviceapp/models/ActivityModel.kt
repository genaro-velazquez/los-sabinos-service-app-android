package com.lossabinos.serviceapp.models

import com.lossabinos.domain.valueobjects.Activity

data class ActivityModel(
    val description: String,
    val requiresEvidence: Boolean
): Model()  {
    constructor(activity: Activity) : this (
        description = activity.description,
        requiresEvidence = activity.requiresEvidence
    )
}