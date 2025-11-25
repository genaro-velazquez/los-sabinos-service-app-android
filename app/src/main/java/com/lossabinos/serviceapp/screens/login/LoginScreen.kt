// presentation/screens/login/LoginScreen.kt
package com.lossabinos.serviceapp.screens.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lossabinos.serviceapp.ui.components.organisms.LoginForm
import com.lossabinos.serviceapp.ui.components.templates.LoginTemplate
import com.lossabinos.serviceapp.ui.theme.LosabosTheme
import com.lossabinos.serviceapp.viewmodel.LoginEvent
import com.lossabinos.serviceapp.viewmodel.LoginViewModel

/**
 * SCREEN - LoginScreen
 *
 * Pantalla completa de login que:
 * 1. Crea snackbarHostState
 * 2. Detecta cambios en errorMessage
 * 3. Muestra Snackbar rojo cuando hay error
 * 4. Muestra spinner amarillo cuando está cargando
 *
 * Flujo:
 * Usuario presiona botón
 *     ↓
 * ViewModel valida (LoginViewModel)
 *     ↓
 * ViewModel asigna errorMessage
 *     ↓
 * LoginScreen detecta cambio en errorMessage
 *     ↓
 * LaunchedEffect llama a showSnackbar()
 *     ↓
 * Snackbar rojo aparece en la parte inferior
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    // ✅ Estado del login
    val state = viewModel.state.collectAsState()

    // ✅ SnackbarHostState para mostrar alertas
    val snackbarHostState = remember { SnackbarHostState() }

    // ✅ LÓGICA: Mostrar Snackbar cuando hay error
    LaunchedEffect(state.value.errorMessage) {
        state.value.errorMessage?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            // Limpiar después de mostrar
            viewModel.onEvent(LoginEvent.ClearError)
        }
    }

    // ✅ PANTALLA: LoginTemplate con LoginForm
    LoginTemplate(
        modifier = Modifier.fillMaxSize(),
        snackbarHostState = snackbarHostState,  // ← Pasa el snackbarHostState

        // Contenido del formulario
        formContent = {
            LoginForm(
                email = state.value.email,
                password = state.value.password,

                // Cambios en los campos
                onEmailChange = { email ->
                    viewModel.onEvent(LoginEvent.EmailChanged(email))
                },
                onPasswordChange = { password ->
                    viewModel.onEvent(LoginEvent.PasswordChanged(password))
                },

                // Click en botón "Iniciar Sesión"
                onLoginClick = {
                    viewModel.onEvent(LoginEvent.LoginClicked)
                },

                // Click en "Olvide mi contraseña"
                onForgotPasswordClick = {
                    viewModel.onEvent(LoginEvent.ForgotPasswordClicked)
                },

                // Estados
                isLoading = state.value.isLoading,
                isError = state.value.isError,
                modifier = Modifier.fillMaxSize()
            )
        }

        // NO pasas snackbarContent → LoginTemplate usa el por defecto (rojo)
    )
}

/**
 * Preview para ver el diseño sin interactividad
 */
@Preview(
    name = "Login Screen",
    showBackground = true,
    showSystemUi = true,
    device = "id:pixel_5"
)
@Composable
fun LoginScreenPreview() {
    LosabosTheme {
        LoginScreen()
    }
}

/**
 * EXPLICACIÓN TÉCNICA:
 *
 * ¿Cómo funciona el Snackbar?
 *
 * 1. snackbarHostState = remember { SnackbarHostState() }
 *    ↓ Crea un estado que guarda los mensajes de snackbar
 *
 * 2. Lo pasas a LoginTemplate
 *    ↓ LoginTemplate lo usa en SnackbarHost()
 *
 * 3. ViewModel asigna errorMessage
 *    ↓ state.value.errorMessage = "Por favor ingresa un email"
 *
 * 4. LaunchedEffect detecta el cambio
 *    ↓ LaunchedEffect(state.value.errorMessage) { ... }
 *
 * 5. Llama a showSnackbar()
 *    ↓ snackbarHostState.showSnackbar(mensaje)
 *
 * 6. Snackbar aparece
 *    ↓ SnackbarHost renderiza el Snackbar rojo
 *
 * 7. Se muestra 4 segundos y desaparece automáticamente
 *    ↓ Comportamiento por defecto de Snackbar
 *
 * ¿Cuándo aparece?
 * - Cuando presiona botón SIN email → "Por favor ingresa un email"
 * - Cuando presiona botón CON email inválido → "Email inválido"
 * - Cuando presiona botón SIN contraseña → "Por favor ingresa una contraseña"
 * - Cuando presiona botón CON contraseña corta → "La contraseña debe..."
 * - Cuando presiona botón CON datos válidos → Muestra spinner 2s
 */