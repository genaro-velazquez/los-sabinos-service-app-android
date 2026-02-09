package com.lossabinos.serviceapp.models.ui

import com.lossabinos.serviceapp.models.ui.enums.IssueCategoryTypeUIModel
import com.lossabinos.serviceapp.models.ui.enums.ConceptCategoryTypeUIModel
import com.lossabinos.serviceapp.models.ui.enums.UrgencyLevelTypeUIModel

data class WorkRequestUIModel(
    val title: String = "",
    val description: String = "",
    val findings: String = "",
    val justification: String = "",
    val urgency: UrgencyLevelTypeUIModel = UrgencyLevelTypeUIModel.NORMAL,
    val issueCategory: IssueCategoryTypeUIModel = IssueCategoryTypeUIModel.OTHER,
    val conceptCategory: ConceptCategoryTypeUIModel = ConceptCategoryTypeUIModel.OTROS,
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
    LOW, MEDIUM, HIGH, CRITICAL
    //LOW, NORMAL, HIGH, CRITICAL
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
