package com.lossabinos.serviceapp.states

import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.serviceapp.models.ui.WorkRequestUIModel

data class WorkRequestUiState(
    val form: WorkRequestUIModel = WorkRequestUIModel(),
    val photos: List<WorkRequestPhoto> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
