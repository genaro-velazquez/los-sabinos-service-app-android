// presentation/viewmodel/LoginViewModel.kt
package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Patterns
import com.lossabinos.domain.usecases.authentication.EmailPasswordLoginUseCase
import com.lossabinos.domain.usecases.preferences.GetUserPreferencesUseCase
import com.lossabinos.serviceapp.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// ============================================================================
// 1. DATA CLASSES - Estado y Eventos
// ============================================================================

/**
 * Estado del LoginScreen
 */
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isError: Boolean = false,
    val loginSuccess: Boolean = false
)

/**
 * Eventos que pueden ocurrir en LoginScreen
 */
sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object LoginClicked : LoginEvent()
    object ForgotPasswordClicked : LoginEvent()
    object ClearError : LoginEvent()
}

// ============================================================================
// 2. VIEWMODEL - Lógica de Login
// ============================================================================

/**
 * ViewModel para la pantalla de login
 *
 * Responsabilidades:
 * - Manejar estado del login
 * - Validar campos
 * - Conectar con UseCase
 * - Emitir eventos de navegación
 * - Limpiar sesión al logout
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val emailPasswordLoginUseCase: EmailPasswordLoginUseCase,
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase  // ✅ NUEVO
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email, isError = false) }
            }
            is LoginEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password, isError = false) }
            }
            is LoginEvent.LoginClicked -> {
                login()
            }
            is LoginEvent.ForgotPasswordClicked -> {
                _navigationEvent.value = NavigationEvent.NavigateToForgotPassword
            }
            is LoginEvent.ClearError -> {
                _state.update { it.copy(isError = false, errorMessage = null) }
            }
        }
    }

    /**
     * Realiza el login con validación
     */
    private fun login() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, isError = false) }

                val email = _state.value.email.trim()
                val password = _state.value.password

                // ✅ VALIDAR FORMULARIO
                val validationError = validateForm(email = email, password = password)
                if (validationError != null) {
                    throw Exception(validationError)
                }

                // ✅ EJECUTAR USE CASE (conecta con backend)
                val response = emailPasswordLoginUseCase.execute(
                    email = email,
                    password = password
                )

                // ✅ ÉXITO: Actualizar estado y navegar
                _state.update {
                    it.copy(
                        isLoading = false,
                        loginSuccess = true
                    )
                }

                // 🎯 EMITIR EVENTO DE NAVEGACIÓN
                _navigationEvent.value = NavigationEvent.NavigateToHome

            } catch (e: Exception) {
                // ❌ ERROR
                val errorMessage = e.message ?: "Error desconocido"

                _state.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = errorMessage
                    )
                }
            }
        }
    }

    /**
     * Valida todos los campos del formulario
     */
    private fun validateForm(email: String, password: String): String? {

        if (email.isEmpty()) {
            return "Por favor ingresa un email"
        }

        if (!isEmailValid(email)) {
            return "Email inválido"
        }

        if (password.isEmpty()) {
            return "Por favor ingresa una contraseña"
        }

        if (password.length < 6) {
            return "La contraseña debe tener al menos 6 caracteres"
        }

        return null
    }

    /**
     * Valida el formato del email
     */
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Limpia el estado del ViewModel cuando se logout
     *
     * ✅ NUEVO: Ejecuta getUserPreferencesUseCase.clear()
     */
    fun clearState() {
        // ✅ Ejecutar clear() en UseCase para limpiar sesión
        viewModelScope.launch {
            try {
                getUserPreferencesUseCase.clear()  // ← Limpiar sesión
            } catch (e: Exception) {
                println("Error al limpiar sesión: ${e.message}")
            }
        }

        // Limpiar estado local
        _state.value = LoginState()
        _navigationEvent.value = null
    }
}

/**
 * NOTAS:
 *
 * ✅ LoginViewModel ahora:
 * - Inyecta GetUserPreferencesUseCase
 * - clearState() ejecuta getUserPreferencesUseCase.clear()
 * - Se llama al logout (desde NavGraph)
 *
 * ✅ Flujo:
 * 1. Usuario presiona "Cerrar Sesión" en Home
 * 2. HomeViewModel emite NavigateToLogin
 * 3. NavGraph navega a Login
 * 4. NavGraph llama a loginViewModel.clearState()
 * 5. clearState() ejecuta getUserPreferencesUseCase.clear()
 * 6. Se limpia la sesión en el backend/caché
 * 7. Usuario ve LoginScreen
 *
 * ✅ Ventajas:
 * - Sesión se limpia correctamente
 * - No hay datos de usuario restos en caché
 * - Próxima vez que abre app, irá a Login
 */
