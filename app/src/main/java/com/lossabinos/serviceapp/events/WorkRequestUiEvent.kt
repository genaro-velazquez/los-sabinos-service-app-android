package com.lossabinos.serviceapp.events

import com.lossabinos.serviceapp.models.ui.CategoryUI
import com.lossabinos.serviceapp.models.ui.UrgencyUI
import com.lossabinos.serviceapp.models.ui.enums.ConceptCategoryTypeUIModel
import com.lossabinos.serviceapp.models.ui.enums.IssueCategoryTypeUIModel
import com.lossabinos.serviceapp.models.ui.enums.UrgencyLevelTypeUIModel

sealed interface WorkRequestUiEvent {
    data class OnTitleChange(val value: String) : WorkRequestUiEvent
    data class OnDescriptionChange(val value: String) : WorkRequestUiEvent
    data class OnFindingsChange(val value: String) : WorkRequestUiEvent
    data class OnJustificationChange(val value: String) : WorkRequestUiEvent
    data class OnUrgencyChange(val value: UrgencyLevelTypeUIModel) : WorkRequestUiEvent
    data class OnRequiresApprovalChange(val value: Boolean) : WorkRequestUiEvent
    data class OnPhotoCaptured(val localPath: String) : WorkRequestUiEvent
    data class OnPhotoDeleted(val photoId: String) : WorkRequestUiEvent

    data class OnCategoryChange(val value: IssueCategoryTypeUIModel): WorkRequestUiEvent

    data class OnConceptCategoryChange(val value: ConceptCategoryTypeUIModel): WorkRequestUiEvent
    data class OnSubmit(
        val serviceExecutionId: String,
        val workOrderId: String,
        val vehicleId: String
    ) : WorkRequestUiEvent

    object OnCancel : WorkRequestUiEvent
}
