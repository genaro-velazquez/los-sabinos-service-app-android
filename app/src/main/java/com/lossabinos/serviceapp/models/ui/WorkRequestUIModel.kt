package com.lossabinos.serviceapp.models.ui

data class WorkRequestUIModel(
    val title: String = "",
    val description: String = "",
    val findings: String = "",
    val justification: String = "",
    val urgency: UrgencyUI = UrgencyUI.NORMAL,
    val category: CategoryUI = CategoryUI.OTHER,
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

enum class CategoryUI {
    MECHANICAL, ELECTRICAL, STRUCTURAL, AESTHETIC, SAFETY, OTHER
}

fun WorkRequestUIModel.validate(): String? {
    if (title.isBlank()) return "El título es obligatorio"
    if (description.isBlank()) return "La descripción es obligatoria"
    if (findings.isBlank()) return "Los hallazgos son obligatorios"
    if (justification.isBlank()) return "La justificación es obligatoria"
    return null
}
