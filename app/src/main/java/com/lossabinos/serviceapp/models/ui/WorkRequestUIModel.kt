package com.lossabinos.serviceapp.models.ui

data class WorkRequestUIModel(
    val title: String = "",
    val description: String = "",
    val findings: String = "",
    val justification: String = "",
    val urgency: UrgencyUI = UrgencyUI.NORMAL,
    val requiresCustomerApproval: Boolean = true
) {
    fun isValid(): Boolean {
        return title.isNotBlank() &&
                description.isNotBlank() &&
                findings.isNotBlank() &&
                justification.isNotBlank()
    }
}

enum class UrgencyUI {
    LOW, NORMAL, HIGH, CRITICAL
}
