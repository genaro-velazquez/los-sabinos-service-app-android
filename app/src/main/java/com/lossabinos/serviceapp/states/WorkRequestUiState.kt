package com.lossabinos.serviceapp.states

import com.lossabinos.serviceapp.models.ui.WorkRequestUIModel

data class WorkRequestUiState(
    val form: WorkRequestUIModel = WorkRequestUIModel(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
