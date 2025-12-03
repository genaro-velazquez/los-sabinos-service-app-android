package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.domain.responses.AssignedServicesResponse
import com.lossabinos.domain.responses.DetailedServiceResponse
import com.lossabinos.domain.usecases.mechanics.GetDetailedServiceUseCase
import com.lossabinos.domain.usecases.mechanics.GetMechanicsServicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MechanicsViewModel @Inject constructor(
    private val getMechanicsServicesUseCase: GetMechanicsServicesUseCase,
    private val getDetailedServiceUseCase: GetDetailedServiceUseCase
) : ViewModel(){

    // ==========================================
    // ASSIGNED SERVICES (Lista de servicios)
    // ==========================================
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

    // ==========================================
    // DETAILED SERVICE (Detalles de un servicio) ✨ NUEVO
    // ==========================================
    /**
    * StateFlow para almacenar los detalles de un servicio específico
    *
    * Estados posibles:
    * - Loading: Cargando datos del servicio
    * - Success: Datos cargados exitosamente
    * - Error: Error al cargar datos
     */
    private val _detailedService = MutableStateFlow<Result<DetailedServiceResponse>>(Result.Idle)
    val detailedService: StateFlow<Result<DetailedServiceResponse>> = _detailedService.asStateFlow()

    /**
    * Carga los detalles de un servicio específico
    * @param idService ID del servicio a cargar
    * Uso:
    * ```
    * mechanicsViewModel.loadDetailedService(serviceId)
    * ```
    */
    fun loadDetailedService(idService: String) {
        viewModelScope.launch {
            try {
                _detailedService.value = Result.Loading
                val response = getDetailedServiceUseCase.execute(idService = idService)
                _detailedService.value = Result.Success(response)
            } catch (e: Exception) {
                _detailedService.value = Result.Error(e)
            }
        }
    }
}

/**
* Estados posibles para las operaciones del ViewModel
*
* Usado por:
* - assignedServices: Lista de servicios asignados
* - detailedService: Detalles de un servicio
*/
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
    object Idle : Result<Nothing>()
}
