package com.lossabinos.serviceapp.models

import com.lossabinos.domain.valueobjects.Activity

data class ActivityModel(
    val Id:String,
    val description: String,
    val requiresEvidence: Boolean
): Model()  {
    constructor(activity: Activity) : this (
        Id = activity.id,
        description = activity.description,
        requiresEvidence = activity.requiresEvidence
    )
}