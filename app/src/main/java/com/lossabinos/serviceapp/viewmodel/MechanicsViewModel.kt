package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.domain.responses.AssignedServicesResponse
import com.lossabinos.domain.usecases.mechanics.GetMechanicsServicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MechanicsViewModel @Inject constructor(
    private val getMechanicsServicesUseCase: GetMechanicsServicesUseCase
) : ViewModel(){

    private val _assignedServices = MutableStateFlow<Result<AssignedServicesResponse>>(Result.Loading)
    val assignedServices: StateFlow<Result<AssignedServicesResponse>> = _assignedServices.asStateFlow()

    fun loadAssignedServices() {
        viewModelScope.launch {
            try {
                _assignedServices.value = Result.Loading
                val response = getMechanicsServicesUseCase.execute()
                _assignedServices.value = Result.Success(response)
            } catch (e: Exception) {
                _assignedServices.value = Result.Error(e)
            }
        }
    }
}

/**
 * Eventos que pueden ocurrir
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
