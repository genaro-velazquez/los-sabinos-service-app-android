// presentation/screens/splash/SplashScreen.kt
package com.lossabinos.serviceapp.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lossabinos.serviceapp.ui.theme.BackgroundLight
import com.lossabinos.serviceapp.ui.theme.PrimaryBlue

/**
 * SplashScreen - Pantalla de carga inicial
 *
 * Se muestra mientras se valida si el usuario está logado:
 * - Si logado → navega a Home
 * - Si no logado → navega a Login
 *
 * @param modifier Modifier para personalización
 */
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentAlignment = Alignment.Center
    ) {
        // Spinner de carga
        CircularProgressIndicator(
            color = PrimaryBlue,
            strokeWidth = androidx.compose.material3.ProgressIndicatorDefaults.CircularStrokeWidth
        )
    }
}

/**
 * NOTAS:
 *
 * ✅ Este Screen:
 * - Es simple: solo muestra un spinner
 * - No necesita datos
 * - La lógica está en SplashViewModel
 * - Se muestra mientras se valida la sesión
 *
 * ✅ Flujo:
 * 1. App inicia
 * 2. NavGraph con startDestination = Routes.SPLASH
 * 3. SplashScreen muestra spinner
 * 4. SplashViewModel inicia en background
 * 5. Valida sesión con getIsLogged()
 * 6. Emite NavigationEvent
 * 7. NavGraph navega a HOME o LOGIN
 * 8. SplashScreen desaparece
 */
