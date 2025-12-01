// presentation/navigation/NavGraph.kt
package com.lossabinos.serviceapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lossabinos.serviceapp.screens.home.HomePage
import com.lossabinos.serviceapp.screens.login.LoginScreen
import com.lossabinos.serviceapp.viewmodel.HomeViewModel
import com.lossabinos.serviceapp.viewmodel.LoginViewModel
import com.lossabinos.serviceapp.viewmodel.SplashViewModel
import kotlinx.coroutines.flow.collectLatest
import com.lossabinos.serviceapp.screens.splash.SplashScreen

/**
 * Rutas disponibles en la aplicación
 */
object Routes {
    const val SPLASH = "splash"      // ✅ NUEVO
    const val LOGIN = "login"
    const val HOME = "home"
}

/**
 * NavGraph - Define la navegación entre pantallas
 *
 * Flujo de rutas:
 * SPLASH (inicio) → LOGIN o HOME → (HOME) → LOGIN
 *
 * Destino inicial: SPLASH (valida sesión)
 *
 * @param navController Controlador de navegación
 * @param loginViewModel ViewModel del login
 * @param modifier Modifier para personalización
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    // ============================================================================
    // 1. Escuchar eventos de navegación del LoginViewModel
    // (Login → Home)
    // ============================================================================
    LaunchedEffect(key1 = loginViewModel) {
        loginViewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateToHome -> {
                    // Navegar a Home y eliminar Login del stack
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
                is NavigationEvent.NavigateToForgotPassword -> {
                    // Futura implementación
                    println("Navigate to ForgotPassword")
                }
                is NavigationEvent.NavigateToLogin -> {
                    // No hacer nada aquí (se maneja en HomeViewModel)
                }
                else -> {}
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,  // ✅ SPLASH es el destino inicial
        modifier = modifier
    ) {
        // ============================================================================
        // SPLASH SCREEN - Valida sesión y navega a HOME o LOGIN
        // ============================================================================
        composable(Routes.SPLASH) {
            val splashViewModel: SplashViewModel = hiltViewModel()

            // ✅ Escuchar eventos de navegación del SplashViewModel
            LaunchedEffect(key1 = splashViewModel) {
                splashViewModel.navigationEvent.collectLatest { event ->
                    when (event) {
                        is NavigationEvent.NavigateToHome -> {
                            // Sesión válida → ir a Home
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                        }
                        is NavigationEvent.NavigateToLogin -> {
                            // Sin sesión → ir a Login
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                        }
                        else -> {}
                    }
                }
            }

            SplashScreen()
        }

        // ============================================================================
        // LOGIN SCREEN
        // ============================================================================
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = loginViewModel
            )
        }

        // ============================================================================
        // HOME SCREEN
        // ============================================================================
        composable(Routes.HOME) {
            // ✅ Obtener HomeViewModel
            val homeViewModel: HomeViewModel = hiltViewModel()

            // ============================================================================
            // 2. Escuchar eventos de navegación del HomeViewModel
            // (Home → Login)
            // ============================================================================
            LaunchedEffect(key1 = homeViewModel) {
                homeViewModel.navigationEvent.collectLatest { event ->
                    when (event) {
                        is NavigationEvent.NavigateToLogin -> {
                            // ✅ Navegar a Login cuando confirma logout
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.HOME) { inclusive = true }
                            }
                            // Limpiar estado del LoginViewModel
                            loginViewModel.clearState()
                        }
                        else -> {}
                    }
                }
            }

            HomePage(
                onLogoutConfirmed = {
                    // La navegación se maneja mediante NavigationEvent
                    println("Logout confirmed - navegación manejada por NavGraph")
                },
                onSettingsClick = {
                    // TODO: Navegar a settings
                    println("Settings clicked")
                },
                onSyncClick = {
                    // TODO: Implementar sincronización
                    println("Sync clicked")
                },
                onSyncNowClick = {
                    // TODO: Implementar sincronización
                    println("Sync now clicked")
                },
                homeViewModel = homeViewModel
            )
        }
    }
}

/**
 * NOTAS:
 *
 * ✅ Flujo Splash → Home:
 * 1. App inicia
 * 2. NavGraph con startDestination = Routes.SPLASH
 * 3. SplashScreen muestra
 * 4. SplashViewModel.validateSession()
 * 5. getIsLogged() retorna true
 * 6. _navigationEvent = NavigateToHome
 * 7. NavGraph escucha (LaunchedEffect del SplashViewModel)
 * 8. navController.navigate(Routes.HOME)
 * 9. popUpTo(Routes.SPLASH) { inclusive = true }
 * 10. Se muestra HomePage
 *
 * ✅ Flujo Splash → Login:
 * 1. App inicia
 * 2. NavGraph con startDestination = Routes.SPLASH
 * 3. SplashScreen muestra
 * 4. SplashViewModel.validateSession()
 * 5. getIsLogged() retorna false
 * 6. _navigationEvent = NavigateToLogin
 * 7. NavGraph escucha (LaunchedEffect del SplashViewModel)
 * 8. navController.navigate(Routes.LOGIN)
 * 9. popUpTo(Routes.SPLASH) { inclusive = true }
 * 10. Se muestra LoginScreen
 *
 * ✅ Ventajas:
 * - Splash es el punto de entrada
 * - Valida sesión automáticamente
 * - Si logado → Home
 * - Si no logado → Login
 * - Respeta sesiones guardadas
 * - Usuario no ve Login si ya tiene sesión
 */

