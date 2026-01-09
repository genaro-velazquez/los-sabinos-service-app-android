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
import com.lossabinos.serviceapp.ui.components.organisms.LoginForm
import com.lossabinos.serviceapp.ui.components.templates.LoginTemplate
import com.lossabinos.serviceapp.viewmodel.LoginEvent
import com.lossabinos.serviceapp.viewmodel.LoginState
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
    val state = viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.value.errorMessage) {
        state.value.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(LoginEvent.ClearError)
        }
    }

    // Estados y callbacks
    LoginScreenContent(
        state = state.value,
        snackbarHostState = snackbarHostState,
        onEmailChange = { viewModel.onEvent(LoginEvent.EmailChanged(it)) },
        onPasswordChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
        onLoginClick = { viewModel.onEvent(LoginEvent.LoginClicked) },
        onForgotPasswordClick = { viewModel.onEvent(LoginEvent.ForgotPasswordClicked) }
    )
}

@Composable
fun LoginScreenContent(
    state: LoginState,
    snackbarHostState: SnackbarHostState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    // Layout genérico
    LoginTemplate(
        modifier = Modifier.fillMaxSize(),
        snackbarHostState = snackbarHostState,
        formContent = {
            // UI
            LoginForm(
                email = state.email,
                password = state.password,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onLoginClick = onLoginClick,
                onForgotPasswordClick = onForgotPasswordClick,
                isLoading = state.isLoading,
                isError = state.isError,
                modifier = Modifier.fillMaxSize()
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        state = LoginState(
            email = "correo@ejemplo.com",
            password = "123456",
            isLoading = false,
            isError = false
        ),
        snackbarHostState = remember { SnackbarHostState() },
        onEmailChange = {},
        onPasswordChange = {},
        onLoginClick = {},
        onForgotPasswordClick = {}
    )
}