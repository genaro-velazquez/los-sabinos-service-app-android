// presentation/screens/home/HomeScreen.kt
package com.lossabinos.serviceapp.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.lossabinos.serviceapp.presentation.ui.components.organisms.ActionCardModel
import com.lossabinos.serviceapp.presentation.ui.components.organisms.ActionCardsSection
import com.lossabinos.serviceapp.ui.components.organisms.ConfirmationDialog
import com.lossabinos.serviceapp.ui.components.organisms.HomeHeaderSection
import com.lossabinos.serviceapp.ui.components.organisms.MetricsSection
import com.lossabinos.serviceapp.ui.components.organisms.ServiceCardData
import com.lossabinos.serviceapp.ui.components.organisms.ServiceListSectionOrganism
import com.lossabinos.serviceapp.ui.components.organisms.SyncSection
import com.lossabinos.serviceapp.ui.templates.HomeTemplate
import com.lossabinos.serviceapp.ui.theme.LosabosTheme
import com.lossabinos.serviceapp.viewmodel.HomeEvent
import com.lossabinos.serviceapp.viewmodel.HomeViewModel
import com.lossabinos.serviceapp.viewmodel.MechanicsViewModel
import com.lossabinos.serviceapp.viewmodel.Result
import kotlin.collections.emptyList
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.lossabinos.domain.responses.DetailedServiceResponse












/**
 * HomePage - Página principal de la aplicación ✨ ACTUALIZADA CON ROOM
 *
 * Estructura Atomic Design:
 * - Atoms: Avatar, MetricIcon, StatusBadge, ActionButton, PrimaryButton, StatusText
 * - Molecules: UserHeader, MetricCard, StatusSection, UnsyncSection, ModalButtonGroup
 * - Organisms: HomeHeaderSection, MetricsSection, SyncSection, ConfirmationDialog
 *             ActionCardsSection, ServiceListSectionOrganism
 * - Template: HomeTemplate (estructura sin datos)
 * - Page: HomePage (pantalla completa con datos)
 *
 * Flujo de datos:
 * 1. loadInitialData() → API → syncInitialDataState
 * 2. loadLocalData() → Room → localInitialDataState
 * 3. dataToDisplay selecciona: API primero, Room como fallback
 * 4. UI muestra dataToDisplay
 *
 * @param onLogoutConfirmed Callback cuando confirma logout
 * @param onSettingsClick Callback para settings
 * @param onSyncClick Callback para sincronizar
 * @param onSyncNowClick Callback para sincronizar ahora
 * @param onCameraClick Callback para cámara
 * @param onReportsClick Callback para reportes
 * @param onLocationClick Callback para ubicación
 * @param onServiceComplete Callback para completar servicio
 * @param onServiceReschedule Callback para reprogramar servicio
 * @param modifier Modifier para personalización
 * @param homeViewModel ViewModel del Home (inyectado por Hilt)
 * @param mechanicsViewModel ViewModel de Mecánicos (inyectado por Hilt)
 */
@Composable
fun HomeScreen(
    onLogoutConfirmed: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onSyncClick: () -> Unit = {},
    onSyncNowClick: () -> Unit = {},
    onCameraClick: () -> Unit = {},
    onReportsClick: () -> Unit = {},
    onLocationClick: () -> Unit = {},
    onServiceComplete: (String) -> Unit = {},
    onServiceReschedule: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    mechanicsViewModel: MechanicsViewModel = hiltViewModel()
) {

    // ==========================================
    // OBSERVAR ESTADOS
    // ==========================================

    // Observar estado (nombre, ubicación, etc.)
    val state = homeViewModel.state.collectAsState().value

    // Estado de la carga inicial desde API
    val syncInitialDataState = mechanicsViewModel.syncInitialData.collectAsState().value

    // Estado de la carga inicial desde Room (NUEVO)
    val localInitialDataState = mechanicsViewModel.localInitialData.collectAsState().value

    // Estado de servicio detallado
    val detailedServiceState = mechanicsViewModel.detailedService.collectAsState().value

    // ==========================================
    // LÓGICA: DECIDIR QUÉ DATOS MOSTRAR
    // ==========================================
    // Preferencia: API primero, luego Room como fallback
    val dataToDisplay = when {
        syncInitialDataState is Result.Success -> (syncInitialDataState as Result.Success).data
        localInitialDataState is Result.Success -> (localInitialDataState as Result.Success).data
        else -> null
    }

    // ==========================================
    // EFECTOS LATERALES
    // ==========================================

    // Cargar datos al abrir la pantalla
    LaunchedEffect(Unit) {
        mechanicsViewModel.loadInitialData()   // Intenta desde API
        mechanicsViewModel.loadLocalData()     // Carga fallback desde Room
    }

    // ==========================================
    // STATE PARA CONTROLAR MODAL DE DETALLES
    // ==========================================

    var selectedServiceId by remember { mutableStateOf<String?>(null) }
    var showDetailModal by remember { mutableStateOf(false) }

    // Observar cambios en detailedService
    LaunchedEffect(detailedServiceState) {
        when (detailedServiceState) {
            is Result.Success -> {
                // Mostrar modal cuando se cargue el servicio detallado
                showDetailModal = true
            }
            is Result.Error -> {
                // Manejar error
                println("❌ Error al cargar detalles del servicio: ${detailedServiceState.exception.message}")
            }
            else -> {
                // Loading o Idle
            }
        }
    }

    // ==========================================
    // DEFINIR ACCIONES RÁPIDAS
    // ==========================================
    val actionCards = listOf(
        ActionCardModel(
            id = "camera",
            title = "Cámara",
            icon = Icons.Filled.Camera,
            onClick = onCameraClick
        ),
        ActionCardModel(
            id = "reports",
            title = "Reportes",
            icon = Icons.Filled.BarChart,
            onClick = onReportsClick
        ),
        ActionCardModel(
            id = "location",
            title = "Ubicación",
            icon = Icons.Filled.LocationOn,
            onClick = onLocationClick
        )
    )

    // ==========================================
    // MODAL DE CONFIRMACIÓN (LOGOUT)
    // ==========================================

    if (state.showLogoutDialog) {
        ConfirmationDialog(
            title = "Cerrar Sesión",
            content = "¿Estás seguro de que deseas cerrar sesión?",
            primaryButtonText = "Cerrar Sesión",
            secondaryButtonText = "Cancelar",
            onPrimaryClick = {
                homeViewModel.onEvent(HomeEvent.ConfirmLogout)
                onLogoutConfirmed()
            },
            onSecondaryClick = {
                homeViewModel.onEvent(HomeEvent.CancelLogout)
            },
            onDismiss = {
                homeViewModel.onEvent(HomeEvent.CancelLogout)
            }
        )
    }

    // ==========================================
    // MODAL DE DETALLES DE SERVICIO
    // ==========================================

    if (showDetailModal && detailedServiceState is Result.Success) {
        ServiceDetailModal(
            detailedService = detailedServiceState.data,
            onDismiss = {
                showDetailModal = false
                selectedServiceId = null
            }
        )
    }

    // ==========================================
    // HOME TEMPLATE
    // ==========================================

    HomeTemplate(
        // 1. Header del usuario
        headerSection = {
            HomeHeaderSection(
                userName = dataToDisplay?.mechanic?.name ?: state.userName,
                userLocation = state.userLocation,
                isOnline = true,
                onSettingsClick = onSettingsClick,
                onLogoutClick = {
                    homeViewModel.onEvent(HomeEvent.LogoutClicked)
                }
            )
        },
        // 2. Sección de sincronización
        syncSection = {
            SyncSection(
                statusText = "Estás en línea",
                lastSyncText = "Última sincronización: Hoy 10:45 AM",
                unsyncTitle = "${dataToDisplay?.syncMetadata?.totalServices ?: 0} Servicios",
                unsyncDetails = "${dataToDisplay?.syncMetadata?.pendingServices ?: 0} Pendientes, ${dataToDisplay?.syncMetadata?.inProgressServices ?: 0} En Progreso",
                onSyncClick = onSyncClick,
                onSyncNowClick = onSyncNowClick
            )
        },
        // 3. Acciones rápidas (ActionCards)
        actionsSection = {
            ActionCardsSection(
                actions = actionCards,
                title = "Acciones Rápidas",
                onActionClick = { actionId ->
                    println("✅ Action selected: $actionId")
                },
                columns = 3
            )
        },
        // 4. Métricas
        metricsSection = {
            MetricsSection(
                completedCount = "0",
                inProgressCount = (dataToDisplay?.syncMetadata?.inProgressServices ?: 0).toString(),
                pendingCount = (dataToDisplay?.syncMetadata?.pendingServices ?: 0).toString(),
                efficiencyPercentage = "92%"
            )
        },
        // 5. Lista de servicios
        serviceListSection = {
            when {
                // Estado LOADING
                syncInitialDataState is Result.Loading || localInitialDataState is Result.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                // Estado SUCCESS - Datos disponibles
                dataToDisplay != null -> {
                    // Convertir AssignedService a ServiceCardData
                    val services = dataToDisplay.assignedServices.map { service ->
                        // Buscar tipo de servicio en la lista
                        val serviceTypeName = dataToDisplay.serviceTypes
                            .find { it.id == service.serviceTypeId }?.name
                            ?: "Servicio"

                        ServiceCardData(
                            id = service.id,
                            excecutionId = service.id,
                            title = serviceTypeName,
                            clientName = "Cliente",  // Falta en AssignedService
                            icon = Icons.Filled.Build,
                            status = service.status.replaceFirstChar { it.uppercase() },
                            startTime = service.scheduledStart,
                            endTime = service.scheduledEnd,
                            duration = "N/A",
                            address = "N/A",
                            priority = service.priority.replaceFirstChar { it.uppercase() },
                            note = ""
                        )
                    }

                    if (services.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✅ No hay servicios asignados")
                        }
                    } else {
                        ServiceListSectionOrganism(
                            title = "Servicios Asignados",
                            services = services,
                            onServiceClick = { serviceId ->
                                println("✅ Service clicked: $serviceId")
                            },
                            onCompleteClick = { serviceId ->
                                println("✅ Service completed: $serviceId")
                                selectedServiceId = serviceId
                                mechanicsViewModel.loadDetailedService(serviceId)
                            },
                            onRescheduleClick = { serviceId ->
                                println("✅ Service rescheduled: $serviceId")
                                onServiceReschedule(serviceId)
                            }
                        )
                    }
                }
                // Estado ERROR
                syncInitialDataState is Result.Error && localInitialDataState is Result.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "❌ Error al cargar servicios",
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = syncInitialDataState.let {
                                    if (it is Result.Error) it.exception.message ?: "Error desconocido"
                                    else "Error desconocido"
                                },
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                            )
                            Button(
                                onClick = {
                                    mechanicsViewModel.loadInitialData()
                                    mechanicsViewModel.loadLocalData()
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                // Estado por defecto
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Cargando servicios...")
                    }
                }
            }
        },
        modifier = modifier
    )
}

/**
 * Modal para mostrar detalles de un servicio
 *
 * @param detailedService Datos detallados del servicio
 * @param onDismiss Callback cuando se cierra el modal
 */
@Composable
fun ServiceDetailModal(
    detailedService: DetailedServiceResponse,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Detalles del Servicio",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("ID Ejecución: ${detailedService.serviceExecutionId}")
                Text("ID Servicio: ${detailedService.serviceId}")
                Text("Tipo: ${detailedService.serviceType.name}")
                Text("Progreso: ${detailedService.currentProgress.itemsCompleted}/${detailedService.currentProgress.itemTotal}")

                // Mostrar información del servicio
                Text(
                    text = "Información",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = detailedService.serviceInfo.status,
                    fontSize = 12.sp
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

/**
 * Preview de HomeScreen
 */
@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    LosabosTheme {
        HomeScreen()
    }
}