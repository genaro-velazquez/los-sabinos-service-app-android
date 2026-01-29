package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.domain.usecases.workrequest.CreateWorkRequestUseCase
import com.lossabinos.serviceapp.events.WorkRequestUiEvent
import com.lossabinos.serviceapp.mappers.toDomain
import com.lossabinos.serviceapp.models.ui.WorkRequestUIModel
import com.lossabinos.serviceapp.states.WorkRequestUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkRequestViewModel @Inject constructor(
    private val createWorkRequestUseCase: CreateWorkRequestUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(WorkRequestUiState())
    val uiState: StateFlow<WorkRequestUiState> = _uiState.asStateFlow()

    fun onEvent(
        event: WorkRequestUiEvent,
        workOrderId: String,
        vehicleId: String
    ) {
        when (event) {
            is WorkRequestUiEvent.OnTitleChange ->
                updateForm { copy(title = event.value) }

            is WorkRequestUiEvent.OnDescriptionChange ->
                updateForm { copy(description = event.value) }

            is WorkRequestUiEvent.OnFindingsChange ->
                updateForm { copy(findings = event.value) }

            is WorkRequestUiEvent.OnJustificationChange ->
                updateForm { copy(justification = event.value) }

            is WorkRequestUiEvent.OnUrgencyChange ->
                updateForm { copy(urgency = event.value) }

            is WorkRequestUiEvent.OnRequiresApprovalChange ->
                updateForm { copy(requiresCustomerApproval = event.value) }

            WorkRequestUiEvent.OnSubmit ->
                submit(workOrderId, vehicleId)
        }
    }

    private fun updateForm(update: WorkRequestUIModel.() -> WorkRequestUIModel) {
        _uiState.update {
            it.copy(form = it.form.update())
        }
    }

    private fun submit(workOrderId: String, vehicleId: String) {
        val form = _uiState.value.form

        if (!form.isValid()) {
            _uiState.update {
                it.copy(errorMessage = "Completa todos los campos")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val domain = form.toDomain(
                    workOrderId = workOrderId,
                    vehicleId = vehicleId
                )

                createWorkRequestUseCase(domain)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al crear reporte"
                    )
                }
            }
        }
    }
}