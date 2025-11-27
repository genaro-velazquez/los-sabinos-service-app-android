// presentation/viewmodel/SplashViewModel.kt
package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.domain.usecases.preferences.GetUserPreferencesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.lossabinos.serviceapp.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Estado del Splash
 */
data class SplashState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

/**
 * ViewModel para la pantalla Splash
 *
 * Responsabilidades:
 * - Validar si el usuario está logado
 * - Si está logado → navegar a Home
 * - Si no está logado → navegar a Login
 * - Usar GetUserPreferencesUseCase.getIsLogged()
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()

    init {
        // Validar sesión al inicializar
        validateSession()
    }

    /**
     * Valida si el usuario está logado
     * 
     * Flujo:
     * 1. Espera 1 segundo (para que se vea el splash)
     * 2. Llama a GetUserPreferencesUseCase.getIsLogged()
     * 3. Si true → emitir NavigateToHome
     * 4. Si false → emitir NavigateToLogin
     */
    private fun validateSession() {
        viewModelScope.launch {
            try {
                _state.value = SplashState(isLoading = true)

                // ✅ Esperar 1 segundo (animación del splash)
                delay(1000)

                // ✅ Validar si está logado
                val isLogged = getUserPreferencesUseCase.getIsLogged()

                // ✅ Emitir evento de navegación según resultado
                if (isLogged) {
                    // Usuario logado → ir a Home
                    _navigationEvent.value = NavigationEvent.NavigateToHome
                } else {
                    // Usuario no logado → ir a Login
                    _navigationEvent.value = NavigationEvent.NavigateToLogin
                }

                _state.value = SplashState(isLoading = false)

            } catch (e: Exception) {
                // ❌ Error en validación → ir a Login (por seguridad)
                _state.value = SplashState(
                    isLoading = false,
                    errorMessage = e.message ?: "Error desconocido"
                )
                
                // Por seguridad, ir a Login si hay error
                _navigationEvent.value = NavigationEvent.NavigateToLogin
            }
        }
    }

    fun clearState() {
        _state.value = SplashState()
        _navigationEvent.value = null
    }
}

/**
 * NOTAS:
 *
 * ✅ El ViewModel:
 * - Inyecta GetUserPreferencesUseCase
 * - Ejecuta validateSession() en init
 * - Llama a getUserPreferencesUseCase.getIsLogged()
 * - Emite NavigationEvent según resultado
 *
 * ✅ Flujo:
 * 1. Splash carga
 * 2. SplashViewModel init
 * 3. validateSession()
 * 4. delay(1000) - para que se vea el splash
 * 5. getIsLogged() → API call o caché
 * 6. Si true → NavigateToHome
 * 7. Si false → NavigateToLogin
 * 8. NavGraph escucha y navega
 *
 * ✅ Ventajas:
 * - Valida automáticamente
 * - Respeta sesiones guardadas
 * - Muestra splash mientras valida
 * - Manejo de errores
 * - Seguro (si hay error → Login)
 */
