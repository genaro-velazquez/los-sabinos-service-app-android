package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.domain.entities.AssignedService
import com.lossabinos.domain.entities.Mechanic
import com.lossabinos.domain.entities.ServiceType
import com.lossabinos.domain.responses.AssignedServicesResponse
import com.lossabinos.domain.responses.DetailedServiceResponse
import com.lossabinos.domain.responses.InitialDataResponse
import com.lossabinos.domain.usecases.mechanics.GetAssignedServicesFlowUseCase
import com.lossabinos.domain.usecases.mechanics.GetDetailedServiceUseCase
import com.lossabinos.domain.usecases.mechanics.GetLocalInitialDataUseCase
import com.lossabinos.domain.usecases.mechanics.GetMechanicFlowUseCase
import com.lossabinos.domain.usecases.mechanics.GetMechanicsServicesUseCase
import com.lossabinos.domain.usecases.mechanics.GetServiceTypesFlowUseCase
import com.lossabinos.domain.usecases.mechanics.GetSyncInitialDataUseCase
import com.lossabinos.domain.usecases.mechanics.GetSyncMetadataFlowUseCase
import com.lossabinos.domain.valueobjects.AssignedServiceProgress
import com.lossabinos.domain.valueobjects.SyncMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MechanicsViewModel @Inject constructor(
    private val getMechanicsServicesUseCase: GetMechanicsServicesUseCase,
    private val getDetailedServiceUseCase: GetDetailedServiceUseCase,
    private val getInitialDataUseCase: GetSyncInitialDataUseCase,
    private val getLocalInitialDataUseCase: GetLocalInitialDataUseCase,
    getMechanicFlowUseCase: GetMechanicFlowUseCase,
    getAssignedServicesFlowUseCase: GetAssignedServicesFlowUseCase,
    getServiceTypesFlowUseCase: GetServiceTypesFlowUseCase,
    getSyncMetadataFlowUseCase: GetSyncMetadataFlowUseCase
) : ViewModel(){


    // ==============
    // CARGA INICIAL
    // ==============
    private val _syncInitialData = MutableStateFlow<Result<InitialDataResponse>>(Result.Loading)
    val syncInitialData: StateFlow<Result<InitialDataResponse>> = _syncInitialData

    fun loadInitialData(){
        viewModelScope.launch {
            try {
                println("\n⏳ [API] Iniciando carga desde API...")
                _syncInitialData.value = Result.Loading
                val response = getInitialDataUseCase.execute()
                //println("✅ [API] Datos recibidos")

                //println("💾 [ROOM] Guardando datos...")
                _syncInitialData.value = Result.Success(data = response)
                println("✅ [API] Proceso completo\n")
            }
            catch (e: Exception){
                println("❌ [API] Error: ${e.message}")
                _syncInitialData.value = Result.Error(exception = e)
                println("✅ [API] Continuando con datos de Room\n")
            }
        }
    }

/*
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
                _assignedServices.value = Result.Success(data = response)
            } catch (e: Exception) {
                _assignedServices.value = Result.Error(exception = e)
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
                _detailedService.value = Result.Success(data = response)
            } catch (e: Exception) {
                _detailedService.value = Result.Error(exception = e)
            }
        }
    }
*/
    // ============== ← NUEVO
    // DATOS LOCALES (desde Room)
    // ==============
    private val _localInitialData = MutableStateFlow<Result<InitialDataResponse>>(Result.Idle)
    val localInitialData: StateFlow<Result<InitialDataResponse>> = _localInitialData.asStateFlow()

    fun loadLocalData(){
        viewModelScope.launch {
            try {
                _localInitialData.value = Result.Loading
                val response = getLocalInitialDataUseCase()
                if (response != null) {
                    _localInitialData.value = Result.Success(data = response)
                    println("✅ Datos cargados de Room: ${response.mechanic.name}")
                } else {
                    _localInitialData.value = Result.Error(exception = Exception("No hay datos locales"))
                }
            } catch (e: Exception) {
                _localInitialData.value = Result.Error(exception = e)
                println("❌ Error cargando datos locales: ${e.message}")
            }
        }
    }

    // ============================================================
    // MECHANIC (de Room)
    // ============================================================
    val mechanic: StateFlow<Mechanic?> =
        getMechanicFlowUseCase()
            .catch { exception ->
                println("❌ [VM] Error en mechanic Flow: ${exception.message}")
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = null
            )

    // ============================================================
    // ASSIGNED SERVICES (de Room)
    // ============================================================
    val assignedServices: StateFlow<List<AssignedServiceProgress>> =
        getAssignedServicesFlowUseCase()
            .catch { exception ->
                println("❌ [VM] Error en assignedServices Flow: ${exception.message}")
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )

    // ============================================================
    // SERVICE TYPES (de Room)
    // ============================================================
    val serviceTypes: StateFlow<List<ServiceType>> =
        getServiceTypesFlowUseCase()
            .catch { exception ->
                println("❌ [VM] Error en serviceTypes Flow: ${exception.message}")
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )

    // ============================================================
    // SYNC METADATA (de Room)
    // ============================================================
    val syncMetadata: StateFlow<SyncMetadata?> =
        getSyncMetadataFlowUseCase()
            .catch { exception ->
                println("❌ [VM] Error en syncMetadata Flow: ${exception.message}")
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = null
            )

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
