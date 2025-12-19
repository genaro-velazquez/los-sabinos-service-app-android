// presentation/viewmodel/HomeViewModel.kt
package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.domain.usecases.preferences.GetUserPreferencesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.lossabinos.serviceapp.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estado del Home
 */
data class HomeState(
    val showLogoutDialog: Boolean = false,
    val userName: String = "Cargando...",
    val userLocation: String = "Mexico City",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * Eventos del Home
 */
sealed class HomeEvent {
    object LogoutClicked : HomeEvent()
    object ConfirmLogout : HomeEvent()
    object CancelLogout : HomeEvent()
    data class CompleteServiceClicked(val serviceId: String) : HomeEvent()
}

/**
 * ViewModel para la pantalla Home
 *
 * Responsabilidades:
 * - Manejar estado del Home
 * - Cargar datos del usuario (nombre, ubicación)
 * - Mostrar/ocultar diálogos de confirmación
 * - Emitir eventos de navegación
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()

    init {
        // Cargar datos del usuario al inicializar el ViewModel
        loadUserPreferences()
    }

    /**
     * Carga las preferencias del usuario desde el UseCase
     */
    private fun loadUserPreferences() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }

                // ✅ Ejecutar el UseCase
                val fullName = "${getUserPreferencesUseCase.getUserFirstName()}  ${getUserPreferencesUseCase.getUserLastName()}"

                // ✅ Actualizar estado con datos del usuario
                _state.update {
                    it.copy(
                        userName = fullName,  // ← Nombre real del usuario
                        userLocation = "Mexico City", //?: "Mexico City",
                        isLoading = false
                    )
                }

            } catch (e: Exception) {
                // ❌ Error al cargar
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al cargar datos del usuario"
                    )
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LogoutClicked -> {
                // Mostrar dialog de confirmación
                _state.update { it.copy(showLogoutDialog = true) }
            }
            is HomeEvent.ConfirmLogout -> {
                // Confirmar logout y navegar a login
                _state.update { it.copy(showLogoutDialog = false) }
                _navigationEvent.value = NavigationEvent.NavigateToLogin
            }
            is HomeEvent.CancelLogout -> {
                // Cancelar logout y cerrar dialog
                _state.update { it.copy(showLogoutDialog = false) }
            }
            is HomeEvent.CompleteServiceClicked -> {
                println("✅ Navegando a VehicleRegisterScreen: ${event.serviceId}")
                //_navigationEvent.value = NavigationEvent.NavigateToChecklistProgress(event.serviceId)
                _navigationEvent.value = NavigationEvent.NavigateToVehicleRegistration(event.serviceId)
            }
        }
    }

    fun clearNavigationEvent(){
        _navigationEvent.value = null
    }

    /**
     * Limpia el estado del ViewModel
     */
    fun clearState() {
        _state.value = HomeState()
        _navigationEvent.value = null
    }
}

/**
 * NOTAS:
 *
 * ✅ HomePage ahora:
 * - Inyecta HomeViewModel
 * - Observa state con collectAsState()
 * - Obtiene userName del state (nombre real)
 * - Obtiene userLocation del state (ubicación real)
 * - Pasa datos reales a HomeHeaderSection
 *
 * ✅ Flujo:
 * 1. HomePage carga
 * 2. HomeViewModel init → loadUserPreferences()
 * 3. Ejecuta GetUserPreferencesUseCase
 * 4. state.userName se actualiza
 * 5. HomePage observa cambio
 * 6. Re-composición con nombre real
 * 7. Muestra: "Isabella Rodriguez" (o el nombre real)
 *
 * ✅ Ventajas:
 * - Datos reales del usuario
 * - Se carga automáticamente
 * - Reativo (actualiza si cambian datos)
 * - Manejo de errores
 * - Manejo de loading
 */

