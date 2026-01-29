package com.lossabinos.serviceapp.events

import com.lossabinos.serviceapp.models.ui.UrgencyUI

sealed interface WorkRequestUiEvent {
    data class OnTitleChange(val value: String) : WorkRequestUiEvent
    data class OnDescriptionChange(val value: String) : WorkRequestUiEvent
    data class OnFindingsChange(val value: String) : WorkRequestUiEvent
    data class OnJustificationChange(val value: String) : WorkRequestUiEvent
    data class OnUrgencyChange(val value: UrgencyUI) : WorkRequestUiEvent
    data class OnRequiresApprovalChange(val value: Boolean) : WorkRequestUiEvent
    object OnSubmit : WorkRequestUiEvent
}
