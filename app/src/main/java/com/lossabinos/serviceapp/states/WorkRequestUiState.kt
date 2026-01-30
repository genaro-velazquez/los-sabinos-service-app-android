package com.lossabinos.serviceapp.states

import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.serviceapp.models.ui.WorkRequestUIModel

data class WorkRequestUiState(
    val form: WorkRequestUIModel = WorkRequestUIModel(),
    val photos: List<WorkRequestPhoto> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val isModalVisible: Boolean = false,
    val formErrors: WorkRequestFormErrors = WorkRequestFormErrors()
)


data class WorkRequestFormErrors(
    val title: String? = null,
    val description: String? = null,
    val findings: String? = null,
    val justification: String? = null
) {
    fun hasErrors(): Boolean =
        title != null || description != null || findings != null || justification != null
}
