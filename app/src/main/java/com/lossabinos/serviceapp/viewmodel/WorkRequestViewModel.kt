package com.lossabinos.serviceapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.domain.enums.SyncStatus
import com.lossabinos.domain.usecases.WorkRequestPhoto.DeleteWorkRequestPhotoUseCase
import com.lossabinos.domain.usecases.WorkRequestPhoto.GetWorkRequestPhotosUseCase
import com.lossabinos.domain.usecases.WorkRequestPhoto.SaveWorkRequestPhotoUseCase
import com.lossabinos.domain.usecases.submitWorkRequestUseCase.SubmitWorkRequestUseCase
import com.lossabinos.serviceapp.events.WorkRequestUiEvent
import com.lossabinos.serviceapp.mappers.toDomain
import com.lossabinos.serviceapp.models.ui.WorkRequestUIModel
import com.lossabinos.serviceapp.states.WorkRequestFormErrors
import com.lossabinos.serviceapp.states.WorkRequestUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WorkRequestViewModel @Inject constructor(
    //private val createWorkRequestUseCase: CreateWorkRequestUseCase,
    private val saveWorkRequestPhotoUseCase: SaveWorkRequestPhotoUseCase,
    private val deleteWorkRequestPhotoUseCase: DeleteWorkRequestPhotoUseCase,
    private val getWorkRequestPhotosUseCase: GetWorkRequestPhotosUseCase,
    private val submitWorkRequestUseCase: SubmitWorkRequestUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(WorkRequestUiState())
    val uiState: StateFlow<WorkRequestUiState> = _uiState.asStateFlow()

    private var currentWorkRequestId = UUID.randomUUID().toString()
    private var currentServiceExecutionId: String? = null


    // ─────────────────────────────
    // ABRIR MODAL
    // ─────────────────────────────
    fun openWorkRequest(serviceExecutionId: String) {
        currentServiceExecutionId = serviceExecutionId
        currentWorkRequestId = UUID.randomUUID().toString()
        initializeDraft()
        _uiState.update { it.copy(isModalVisible = true) }
    }

    private fun initializeDraft() {
        _uiState.update {
            it.copy(
                form = WorkRequestUIModel(),
                photos = emptyList(),
                errorMessage = null,
                isSuccess = false
            )
        }
    }

    // ─────────────────────────────
    // EVENTOS
    // ─────────────────────────────
    fun onEvent(event: WorkRequestUiEvent) {
        when (event) {

            is WorkRequestUiEvent.OnTitleChange ->
                updateForm(
                    update = { copy(title = event.value) },
                    updateErrors = { copy(title = null) }
                )

            is WorkRequestUiEvent.OnDescriptionChange ->
                updateForm(
                    update = { copy(description = event.value) },
                    updateErrors = { copy(description = null) }
                )

            is WorkRequestUiEvent.OnFindingsChange ->
                updateForm(
                    update = { copy(findings = event.value) },
                    updateErrors = { copy(findings = null) }
                )

            is WorkRequestUiEvent.OnJustificationChange ->
                updateForm(
                    update = { copy(justification = event.value) },
                    updateErrors = { copy(justification = null) }
                )

            is WorkRequestUiEvent.OnUrgencyChange ->
                updateForm(
                    update = { copy(urgency = event.value) }
                )

            is WorkRequestUiEvent.OnCategoryChange ->
                updateForm(
                    update = {copy(issueCategory = event.value)}
                )

            is WorkRequestUiEvent.OnConceptCategoryChange ->
                updateForm(
                    update = {copy(conceptCategory = event.value)}
                )

            is WorkRequestUiEvent.OnRequiresApprovalChange ->
                updateForm(
                    update = { copy(requiresCustomerApproval = event.value) }
                )

            // ───────── SUBMIT ─────────
            is WorkRequestUiEvent.OnSubmit -> {
                submit(
                    serviceExecutionId = event.serviceExecutionId,
                    workOrderId = event.workOrderId,
                    vehicleId = event.vehicleId
                )
            }

            // ───────── FOTO ─────────
            is WorkRequestUiEvent.OnPhotoCaptured -> {
                viewModelScope.launch {
                    if (_uiState.value.photos.size >= 3) return@launch

                    val photo = WorkRequestPhoto(
                        id = UUID.randomUUID().toString(),
                        workRequestId = currentWorkRequestId,
                        localPath = event.localPath,
                        remoteUrl = null,
                        syncStatus = SyncStatus.PENDING,
                        createdAt = System.currentTimeMillis()
                    )

                    saveWorkRequestPhotoUseCase(photo)

                    val photos = getWorkRequestPhotosUseCase(currentWorkRequestId)
                    _uiState.update { it.copy(photos = photos) }
                }
            }

            is WorkRequestUiEvent.OnPhotoDeleted -> {
                viewModelScope.launch {
                    deleteWorkRequestPhotoUseCase(event.photoId)
                    val photos = getWorkRequestPhotosUseCase(currentWorkRequestId)
                    _uiState.update { it.copy(photos = photos) }
                }
            }

            // ───────── CANCELAR ─────────
            is WorkRequestUiEvent.OnCancel -> {
                _uiState.value = WorkRequestUiState()
            }
        }
    }

    /*
    private fun updateForm(update: WorkRequestUIModel.() -> WorkRequestUIModel) {
        _uiState.update { it.copy(form = it.form.update()) }
    }
    */

    /*
    private fun updateForm(
        clearErrors: (WorkRequestFormErrors.() -> WorkRequestFormErrors)? = null,
        update: WorkRequestUIModel.() -> WorkRequestUIModel
    ) {
        _uiState.update {
            it.copy(
                form = it.form.update(),
                formErrors = clearErrors?.let { clear ->
                    it.formErrors.clear()
                } ?: it.formErrors,
                errorMessage = null
            )
        }
    }
    */

    private fun updateForm(
        update: WorkRequestUIModel.() -> WorkRequestUIModel,
        updateErrors: (WorkRequestFormErrors.() -> WorkRequestFormErrors)? = null
    ) {
        _uiState.update { state ->
            state.copy(
                form = state.form.update(),
                formErrors = updateErrors?.invoke(state.formErrors) ?: state.formErrors,
                errorMessage = null
            )
        }
    }




    // ─────────────────────────────
    // SUBMIT REAL
    // ─────────────────────────────
    private fun submit(
        serviceExecutionId:String,
        workOrderId: String,
        vehicleId: String)
    {
        Log.d("WR_VALIDATE", "Submit llamado")

        val form = _uiState.value.form

        // 1️⃣ VALIDAR CAMPOS
        val errors = WorkRequestFormErrors(
            title = if (form.title.isBlank()) "El título es obligatorio" else null,
            description = when {
                form.description.isBlank() ->
                    "La descripción es obligatoria"

                form.description.length < 20 ->
                    "La descripción debe tener al menos 20 caracteres"

                else -> null
            },
            findings = if (form.findings.isBlank()) "Los hallazgos son obligatorios" else null,
            justification = if (form.justification.isBlank()) "La justificación es obligatoria" else null
        )

        Log.d("WR_VALIDATE", "Errores: $errors")

        // 2️⃣ SI HAY ERRORES → NO SUBMIT
        if (errors.hasErrors()) {
            _uiState.update {
                it.copy(formErrors = errors)
            }
            return
        }

        // 3️⃣ LIMPIAR ERRORES SI TODO OK
        _uiState.update {
            it.copy(formErrors = WorkRequestFormErrors())
        }

        // ⬇️ LO QUE YA TENÍAS
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

/*
                submitWorkRequestUseCase(
                    workRequest = form.toDomain(
                        id = currentWorkRequestId,
                        workOrderId = workOrderId,
                        serviceExecutionId = serviceExecutionId,
                        vehicleId = vehicleId
                    ),
                    serviceExecutionId = serviceExecutionId
                )
 */
                submitWorkRequestUseCase(
                    workRequest = form.toDomain(
                        id = currentWorkRequestId,
                        workOrderId = workOrderId,
                        serviceExecutionId = serviceExecutionId,
                        vehicleId = vehicleId
                    ),
                    photos = _uiState.value.photos
                )


                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        isModalVisible = false
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }
}