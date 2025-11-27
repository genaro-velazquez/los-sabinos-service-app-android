// presentation/screens/home/HomePage.kt
package com.lossabinos.serviceapp.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.lossabinos.serviceapp.ui.components.organisms.ConfirmationDialog
import com.lossabinos.serviceapp.ui.components.organisms.HomeHeaderSection
import com.lossabinos.serviceapp.ui.components.organisms.MetricsSection
import com.lossabinos.serviceapp.ui.components.organisms.SyncSection
import com.lossabinos.serviceapp.ui.templates.HomeTemplate
import com.lossabinos.serviceapp.ui.theme.LosabosTheme
import com.lossabinos.serviceapp.viewmodel.HomeEvent
import com.lossabinos.serviceapp.viewmodel.HomeViewModel

/**
 * HomePage - Página principal de la aplicación
 *
 * Estructura Atomic Design:
 * - Atoms: Avatar, MetricIcon, StatusBadge, ActionButton, PrimaryButton, StatusText
 * - Molecules: UserHeader, MetricCard, StatusSection, UnsyncSection, ModalButtonGroup
 * - Organisms: HomeHeaderSection, MetricsSection, SyncSection, ConfirmationDialog
 * - Template: HomeTemplate (estructura sin datos)
 * - Page: HomePage (pantalla completa con datos)
 *
 * @param onLogoutConfirmed Callback cuando confirma logout (debería navegar a Login)
 * @param onSettingsClick Callback para settings
 * @param onSyncClick Callback para sincronizar
 * @param onSyncNowClick Callback para sincronizar ahora
 * @param modifier Modifier para personalización
 * @param viewModel ViewModel del Home (inyectado por Hilt)
 */
@Composable
fun HomePage(
    onLogoutConfirmed: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onSyncClick: () -> Unit = {},
    onSyncNowClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // ✅ Observar estado (nombre, ubicación, etc.)
    val state = viewModel.state.collectAsState().value

    // Mostrar modal de confirmación si es necesario
    if (state.showLogoutDialog) {
        ConfirmationDialog(
            title = "Cerrar Sesión",
            content = "¿Estás seguro de que deseas cerrar sesión?",
            primaryButtonText = "Cerrar Sesión",
            secondaryButtonText = "Cancelar",
            onPrimaryClick = {
                // Confirmar logout
                viewModel.onEvent(HomeEvent.ConfirmLogout)
                onLogoutConfirmed()
            },
            onSecondaryClick = {
                // Cancelar logout
                viewModel.onEvent(HomeEvent.CancelLogout)
            },
            onDismiss = {
                // Cerrar dialog
                viewModel.onEvent(HomeEvent.CancelLogout)
            }
        )
    }

    HomeTemplate(
        headerSection = {
            HomeHeaderSection(
                userName = state.userName,//"Isabella Rodriguez",
                userLocation = state.userLocation ,//"Mexico City",
                isOnline = true,
                onSettingsClick = onSettingsClick,
                onLogoutClick = {
                    // Mostrar dialog de confirmación
                    viewModel.onEvent(HomeEvent.LogoutClicked)
                }
            )
        },
        syncSection = {
            SyncSection(
                statusText = "Estás en línea",
                lastSyncText = "Última sincronización: Hoy 10:45 AM",
                unsyncTitle = "4 Servicios",
                unsyncDetails = "2 Firmas, 8 Fotos, 1 Observación",
                onSyncClick = onSyncClick,
                onSyncNowClick = onSyncNowClick
            )
        },
        metricsSection = {
            MetricsSection(
                completedCount = "12",
                inProgressCount = "3",
                pendingCount = "5",
                efficiencyPercentage = "92%"
            )
        },
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
fun HomePagePreview() {
    LosabosTheme {
        HomePage()
    }
}
