package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.domain.entities.ExtraCost
import com.lossabinos.domain.usecases.checklist.CreateReportExtraCostUseCase
import com.lossabinos.domain.usecases.checklist.DeleteExtraCostUseCase
import com.lossabinos.domain.usecases.checklist.InsertExtraCostUseCase
import com.lossabinos.domain.usecases.checklist.ObserveExtraCostsUseCase
import com.lossabinos.domain.usecases.checklist.UpdateExtraCostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.lossabinos.serviceapp.models.ui.ExtraCostCategory
import com.lossabinos.serviceapp.models.ui.ExtraCostFormErrors
import com.lossabinos.serviceapp.models.ui.ExtraCostUIModel
import javax.inject.Inject

// ═══════════════════════════════════════════════════════
// EXTRA COST VIEW MODEL
// ═══════════════════════════════════════════════════════
@HiltViewModel
class ExtraCostViewModel @Inject constructor(
    private val observeExtraCostsUseCase: ObserveExtraCostsUseCase,
    private val insertExtraCostUseCase: InsertExtraCostUseCase,
    private val updateExtraCostUseCase: UpdateExtraCostUseCase,
    private val deleteExtraCostUseCase: DeleteExtraCostUseCase,
    private val createReportExtraCostUseCase: CreateReportExtraCostUseCase
) : ViewModel() {

    // ═══════════════════════════════════════════════════════
    // STATES
    // ═══════════════════════════════════════════════════════

    private val _extraCosts = MutableStateFlow<List<ExtraCostUIModel>>(emptyList())
    val extraCosts: StateFlow<List<ExtraCostUIModel>> = _extraCosts.asStateFlow()

    private val _showExtraCostModal = MutableStateFlow(false)
    val showExtraCostModal: StateFlow<Boolean> = _showExtraCostModal.asStateFlow()

    private val _currentExtraCostForm = MutableStateFlow(ExtraCostUIModel())
    val currentExtraCostForm: StateFlow<ExtraCostUIModel> = _currentExtraCostForm.asStateFlow()

    private val _extraCostFormErrors = MutableStateFlow(ExtraCostFormErrors())
    val extraCostFormErrors: StateFlow<ExtraCostFormErrors> = _extraCostFormErrors.asStateFlow()

    private val _isExtraCostLoading = MutableStateFlow(false)
    val isExtraCostLoading: StateFlow<Boolean> = _isExtraCostLoading.asStateFlow()

    private val _editingExtraCostId = MutableStateFlow<String?>(null)
    val editingExtraCostId: StateFlow<String?> = _editingExtraCostId.asStateFlow()

    private val _showDeleteConfirmation = MutableStateFlow(false)
    val showDeleteConfirmation: StateFlow<Boolean> = _showDeleteConfirmation.asStateFlow()

    private val _extraCostToDelete = MutableStateFlow<ExtraCostUIModel?>(null)
    val extraCostToDelete: StateFlow<ExtraCostUIModel?> = _extraCostToDelete.asStateFlow()

    // ═══════════════════════════════════════════════════════
    // LOAD EXTRA COSTS
    // ═══════════════════════════════════════════════════════
    fun loadExtraCosts(serviceId: String) {
        println("📦 Loading extra costs for service: $serviceId")

        viewModelScope.launch {
            observeExtraCostsUseCase(assignedServiceId = serviceId)
                .collect { domainModels ->
                    println("✅ Extra costs loaded: ${domainModels.size}")

                    // Convertir de Domain a UI Model
                    val uiModels = domainModels.map { domainModel ->
                        ExtraCostUIModel(
                            id = domainModel.id,
                            quantity = domainModel.quantity,
                            category = ExtraCostCategory.fromString(domainModel.category),
                            description = domainModel.description,
                            notes = domainModel.notes ?: "",
                            createdAt = domainModel.createdAt
                        )
                    }

                    _extraCosts.value = uiModels
                }
        }
    }

    // ═══════════════════════════════════════════════════════
    // MODAL CONTROL
    // ═══════════════════════════════════════════════════════

    fun openAddExtraCostModal() {
        println("📝 Opening add extra cost modal")
        _currentExtraCostForm.value = ExtraCostUIModel()
        _extraCostFormErrors.value = ExtraCostFormErrors()
        _editingExtraCostId.value = null
        _showExtraCostModal.value = true
    }

    fun openEditExtraCostModal(extraCost: ExtraCostUIModel) {
        println("✏️ Opening edit modal for: ${extraCost.description}")
        _currentExtraCostForm.value = extraCost.copy()
        _extraCostFormErrors.value = ExtraCostFormErrors()
        _editingExtraCostId.value = extraCost.id
        _showExtraCostModal.value = true
    }

    fun closeExtraCostModal() {
        println("❌ Closing extra cost modal")
        _showExtraCostModal.value = false
        _currentExtraCostForm.value = ExtraCostUIModel()
        _extraCostFormErrors.value = ExtraCostFormErrors()
        _editingExtraCostId.value = null
    }

    // ═══════════════════════════════════════════════════════
    // FORM UPDATES
    // ═══════════════════════════════════════════════════════

    fun updateExtraCostQuantity(quantity: Double) {
        _currentExtraCostForm.update { it.copy(quantity = quantity) }
        validateExtraCostForm()
    }

    fun updateExtraCostCategory(category: ExtraCostCategory) {
        _currentExtraCostForm.update { it.copy(category = category) }
        validateExtraCostForm()
    }

    fun updateExtraCostDescription(description: String) {
        _currentExtraCostForm.update { it.copy(description = description) }
        validateExtraCostForm()
    }

    fun updateExtraCostNotes(notes: String) {
        _currentExtraCostForm.update { it.copy(notes = notes) }
    }

    // ═══════════════════════════════════════════════════════
    // VALIDATION
    // ═══════════════════════════════════════════════════════

    private fun validateExtraCostForm() {
        val form = _currentExtraCostForm.value
        var errors = ExtraCostFormErrors()

        if (form.quantity <= 0.0) {
            errors = errors.copy(quantityError = "Amount must be greater than 0")
        }

        if (form.category == null) {
            errors = errors.copy(categoryError = "Please select a category")
        }

        if (form.description.isBlank()) {
            errors = errors.copy(descriptionError = "Description is required")
        }

        _extraCostFormErrors.value = errors
        println("📋 Form validation: ${if (errors.hasErrors()) "❌ Has errors" else "✅ Valid"}")
    }

    // ═══════════════════════════════════════════════════════
    // SAVE EXTRA COST (INSERT OR UPDATE)
    // ═══════════════════════════════════════════════════════

    fun saveExtraCost(assignedServiceId: String) {
        validateExtraCostForm()

        if (_extraCostFormErrors.value.hasErrors()) {
            println("❌ Cannot save: form has errors")
            return
        }

        _isExtraCostLoading.value = true

        viewModelScope.launch {
            try {
                val form = _currentExtraCostForm.value
                val editingId = _editingExtraCostId.value

                // Convertir UI Model a Domain Model
                val domainModel = ExtraCost(
                    id = form.id,
                    assignedServiceId = assignedServiceId,
                    quantity = form.quantity,
                    category = form.category.name,
                    description = form.description,
                    notes = form.notes,
                    createdAt = form.createdAt,
                    syncStatus = "PENDING",
                    timestamp = System.currentTimeMillis()
                )

                if (editingId != null) {
                    // UPDATE mode
                    println("✏️ Updating extra cost: $editingId")
                    updateExtraCostUseCase(domainModel)
                } else {
                    // ADD mode
                    println("➕ Adding new extra cost")
                    insertExtraCostUseCase(domainModel)
                }

                closeExtraCostModal()
                println("✅ Extra cost saved to database")

            } catch (e: Exception) {
                println("❌ Error saving extra cost: ${e.message}")
            } finally {
                _isExtraCostLoading.value = false
            }
        }
    }

    // ═══════════════════════════════════════════════════════
    // DELETE EXTRA COST
    // ═══════════════════════════════════════════════════════

    fun showDeleteConfirmation(extraCost: ExtraCostUIModel) {
        println("⚠️ Showing delete confirmation for: ${extraCost.description}")
        _extraCostToDelete.value = extraCost
        _showDeleteConfirmation.value = true
    }

    fun confirmDeleteExtraCost() {
        val costToDelete = _extraCostToDelete.value
        if (costToDelete != null) {
            viewModelScope.launch {
                try {
                    println("🗑️ Deleting from database: ${costToDelete.description}")
                    deleteExtraCostUseCase(costToDelete.id)
                    println("✅ Extra cost deleted from database")
                    closeDeleteConfirmation()
                } catch (e: Exception) {
                    println("❌ Error deleting extra cost: ${e.message}")
                }
            }
        }
    }

    fun closeDeleteConfirmation() {
        println("❌ Canceling deletion")
        _showDeleteConfirmation.value = false
        _extraCostToDelete.value = null
    }

    // ═══════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════

    fun getTotalExtraCosts(): Double {
        return _extraCosts.value.sumOf { it.quantity }
    }
}
