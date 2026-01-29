package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.domain.enums.SyncStatus
import com.lossabinos.domain.enums.UrgencyLevel
import com.lossabinos.domain.usecases.WorkRequestPhoto.DeleteWorkRequestPhotoUseCase
import com.lossabinos.domain.usecases.WorkRequestPhoto.GetWorkRequestPhotosUseCase
import com.lossabinos.domain.usecases.WorkRequestPhoto.SaveWorkRequestPhotoUseCase
import com.lossabinos.domain.usecases.workrequest.CreateWorkRequestUseCase
import com.lossabinos.domain.valueobjects.WorkRequest
import com.lossabinos.serviceapp.events.WorkRequestUiEvent
import com.lossabinos.serviceapp.mappers.toDomain
import com.lossabinos.serviceapp.models.ui.UrgencyUI
import com.lossabinos.serviceapp.models.ui.WorkRequestUIModel
import com.lossabinos.serviceapp.states.WorkRequestUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WorkRequestViewModel @Inject constructor(
    private val createWorkRequestUseCase: CreateWorkRequestUseCase,
    private val saveWorkRequestPhotoUseCase: SaveWorkRequestPhotoUseCase,
    private val deleteWorkRequestPhotoUseCase: DeleteWorkRequestPhotoUseCase,
    private val getWorkRequestPhotosUseCase: GetWorkRequestPhotosUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(WorkRequestUiState())
    val uiState: StateFlow<WorkRequestUiState> = _uiState.asStateFlow()

    private val currentWorkRequestId =
        UUID.randomUUID().toString()

    private suspend fun createDraftWorkRequest(
        workOrderId: String,
        vehicleId: String
    ) {
        val draft = WorkRequest(
            id = currentWorkRequestId,
            workOrderId = workOrderId,   // ✅ FK válida
            title = "",
            description = "",
            findings = "",
            justification = "",
            photoUls = emptyList(),
            requestType = "",
            requiresCustomerApproval = false,
            urgency = UrgencyLevel.NORMAL,
            createdAt = System.currentTimeMillis(),
            vehicleId = vehicleId,
            syncStatus = SyncStatus.PENDING
        )

        createWorkRequestUseCase(draft)
    }

    fun initializeDraft(
        workOrderId: String,
        vehicleId: String
    ) {
        viewModelScope.launch {
            println("🟡 Initializing draft work request")
            createDraftWorkRequest(
                workOrderId = workOrderId,
                vehicleId = vehicleId
            )

            val photos = getWorkRequestPhotosUseCase(currentWorkRequestId)
            _uiState.update {
                it.copy(photos = photos)
            }
        }
    }

    fun onEvent(
        event: WorkRequestUiEvent
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

            is WorkRequestUiEvent.OnSubmit -> {
                submit(
                    workOrderId = event.workOrderId,
                    vehicleId = event.vehicleId
                )
            }

            is WorkRequestUiEvent.OnPhotoCaptured -> {
                viewModelScope.launch {

                    // ⛔ regla de máximo 3
                    if (_uiState.value.photos.size >= 3) {
                        _uiState.update {
                            it.copy(errorMessage = "Máximo 3 fotos permitidas")
                        }
                        return@launch
                    }

                    val photo = WorkRequestPhoto(
                        id = UUID.randomUUID().toString(),
                        workRequestId = currentWorkRequestId,
                        localPath = event.localPath,
                        remoteUrl = null,
                        syncStatus = SyncStatus.PENDING,
                        createdAt = System.currentTimeMillis()
                    )

                    saveWorkRequestPhotoUseCase(photo)

                    val updatedPhotos =
                        getWorkRequestPhotosUseCase(currentWorkRequestId)

                    _uiState.update {
                        it.copy(photos = updatedPhotos)
                    }
                }
            }

            is WorkRequestUiEvent.OnPhotoDeleted -> {
                viewModelScope.launch {

                    val photo = _uiState.value.photos
                        .firstOrNull { it.id == event.photoId }

                    // 1️⃣ borrar archivo físico
                    photo?.localPath?.let {
                        runCatching { File(it).delete() }
                    }

                    // 2️⃣ borrar de Room
                    deleteWorkRequestPhotoUseCase(event.photoId)

                    // 3️⃣ refrescar lista
                    val updatedPhotos =
                        getWorkRequestPhotosUseCase(currentWorkRequestId)

                    _uiState.update {
                        it.copy(photos = updatedPhotos)
                    }
                }
            }


            is WorkRequestUiEvent.OnCancel -> {
                resetState()
            }
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
                    id = currentWorkRequestId,
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
    fun resetState() {
        _uiState.value = WorkRequestUiState()
    }

}