package com.lossabinos.serviceapp.models.ui

import java.util.UUID

// ═══════════════════════════════════════════════════════
// ENUM PARA CATEGORÍAS
// ═══════════════════════════════════════════════════════
enum class ExtraCostCategory(
    val displayName: String,
    val icon: String
) {
    SPARE_PARTS("Repuestos", "🔧"),
    LABOR("Mano de obra", "👨"),
    CONSUMABLES("Consumibles", "📦"),
    DIAGNOSTICS("Diagnóstico", "🔍"),
    TRANSPORTATION("Transporte", "🚗"),
    OTHER("Otros", "❓");

    companion object {
        fun fromString(value: String?): ExtraCostCategory {
            return try {
                valueOf(value?.uppercase() ?: "OTROS")
            } catch (e: Exception) {
                OTHER
            }
        }
    }
}

// ═══════════════════════════════════════════════════════
// MODEL PARA LA UI
// ═══════════════════════════════════════════════════════
data class ExtraCostUIModel(
    val id: String = UUID.randomUUID().toString(),
    val quantity: Double = 0.0,
    val category: ExtraCostCategory = ExtraCostCategory.OTHER,
    val description: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    // Validar que todos los campos obligatorios estén completos
    fun isValid(): Boolean {
        return quantity > 0.0 &&
                category != null &&
                description.isNotBlank()
    }

    // Formatos para mostrar
    fun getFormattedQuantity(): String = String.format("$%.2f", quantity)

    fun getCategoryIcon(): String = category.icon

    fun getCategoryDisplayName(): String = category.displayName
}

// ═══════════════════════════════════════════════════════
// MODEL PARA ERRORES DE VALIDACIÓN
// ═══════════════════════════════════════════════════════
data class ExtraCostFormErrors(
    val quantityError: String? = null,
    val categoryError: String? = null,
    val descriptionError: String? = null
) {
    fun hasErrors(): Boolean =
        quantityError != null ||
                categoryError != null ||
                descriptionError != null
}
