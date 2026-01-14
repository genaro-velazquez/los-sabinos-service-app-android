package com.lossabinos.serviceapp.models

import com.lossabinos.data.datasource.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.datasource.local.database.entities.ActivityProgressEntity
import com.lossabinos.data.datasource.local.database.entities.ObservationResponseEntity


// ✨ Modelo UI para una actividad con su progreso
data class ActivityUIModel(
    val activity: ActivityModel,
    val progress: ActivityProgressEntity? = null,
    val evidence: List<ActivityEvidenceEntity> = emptyList()
)

// ✨ Modelo UI para una observación con su respuesta
data class ObservationUIModel(
    val observation:String,
    val response: ObservationResponseEntity? = null
)

// ✨ Modelo UI para la sección completa
data class SectionUIModel(
    val section: SectionModel,
    val activities: List<ActivityUIModel> = emptyList(),
    val observations: List<ObservationUIModel> = emptyList()
)