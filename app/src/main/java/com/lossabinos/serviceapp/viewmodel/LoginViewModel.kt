// presentation/viewmodel/LoginViewModel.kt
package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import android.util.Patterns

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

/**
 * Eventos de navegación
 */
sealed class NavigationEvent {
    object NavigateToHome : NavigationEvent()
    object NavigateToForgotPassword : NavigationEvent()
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
 * - Simular login
 * - Navegar a otras pantallas
 */
class LoginViewModel : ViewModel() {

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

                // ✅ VALIDAR FORMULARIO (nueva función)
                val validationError = validateForm()
                if (validationError != null) {
                    throw Exception(validationError)
                }

                // ✅ SIMULAR LLAMADA A API
                delay(2000)

                // ✅ ÉXITO
                _state.update {
                    it.copy(
                        isLoading = false,
                        loginSuccess = true
                    )
                }
                _navigationEvent.value = NavigationEvent.NavigateToHome

            } catch (e: Exception) {
                // ❌ ERROR
                _state.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    /**
     * Valida todos los campos del formulario
     * Retorna null si es válido, o el mensaje de error si no lo es
     */
    private fun validateForm(): String? {
        val email = _state.value.email.trim()  // ✅ TRIM espacios
        val password = _state.value.password

        // ✅ Validar email vacío
        if (email.isEmpty()) {
            return "Por favor ingresa un email"
        }

        // ✅ Validar email válido
        if (!isEmailValid(email)) {
            return "Email inválido"
        }

        // ✅ Validar contraseña vacía
        if (password.isEmpty()) {
            return "Por favor ingresa una contraseña"
        }

        // ✅ Validar longitud contraseña
        if (password.length < 6) {
            return "La contraseña debe tener al menos 6 caracteres"
        }

        // ✅ VALIDAR CONEXIÓN A INTERNET (NUEVA - pero sin contexto por ahora)
        // TODO: Inyectar ConnectivityManager si tienes acceso
        // if (!isInternetAvailable()) {
        //     return "Sin conexión a internet"
        // }

        // ✅ Todo OK
        return null
    }

    /**
     * Valida el formato del email
     */
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun clearState() {
        _state.value = LoginState()
        _navigationEvent.value = null
    }
}


/**
 * NOTAS:
 *
 * ✅ Este archivo tiene TODO lo que necesitas:
 * - LoginState (con loginSuccess)
 * - LoginEvent (sealed class)
 * - NavigationEvent (sealed class)
 * - LoginViewModel (ÚNICA definición)
 *
 * ✅ Características:
 * - Validación de email con Patterns
 * - Validación de contraseña (>= 6 caracteres)
 * - Try-catch para manejo de errores
 * - NavigationEvent para navegar
 * - clearState() para limpiar
 *
 * ✅ Flujo de login:
 * 1. Usuario presiona botón
 * 2. onLoginClick() → login()
 * 3. Validar campos
 * 4. Si error → mostrar en Snackbar
 * 5. Si OK → mostrar spinner 2s
 * 6. Si OK → navegar a Home
 */