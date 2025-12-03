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
 * HomePage - Página principal de la aplicación ✨ ACTUALIZADA
 *
 * Estructura Atomic Design:
 * - Atoms: Avatar, MetricIcon, StatusBadge, ActionButton, PrimaryButton, StatusText
 * - Molecules: UserHeader, MetricCard, StatusSection, UnsyncSection, ModalButtonGroup
 * - Organisms: HomeHeaderSection, MetricsSection, SyncSection, ConfirmationDialog
 *             ActionCardsSection, ServiceListSectionOrganism ✨ NUEVO
 * - Template: HomeTemplate (estructura sin datos) ✨ ACTUALIZADO
 * - Page: HomePage (pantalla completa con datos) ✨ ACTUALIZADO
 *
 * @param onLogoutConfirmed Callback cuando confirma logout
 * @param onSettingsClick Callback para settings
 * @param onSyncClick Callback para sincronizar
 * @param onSyncNowClick Callback para sincronizar ahora
 * @param onCameraClick Callback para cámara
 * @param onReportsClick Callback para reportes
 * @param onLocationClick Callback para ubicación
 * @param onServiceComplete Callback para completar servicio ✨ NUEVO
 * @param onServiceReschedule Callback para reprogramar servicio ✨ NUEVO
 * @param modifier Modifier para personalización
 * @param viewModel ViewModel del Home (inyectado por Hilt)
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
    onServiceComplete: (String) -> Unit = {},      // ✨ NUEVO
    onServiceReschedule: (String) -> Unit = {},    // ✨ NUEVO
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    mechanicsViewModel: MechanicsViewModel = hiltViewModel()
) {

    // ==========================================
    // OBSERVAR ESTADOS
    // ==========================================

    // Observar estado (nombre, ubicación, etc.)
    val state = homeViewModel.state.collectAsState().value

    // Estado de servicios asignados (lista)
    val servicesState = mechanicsViewModel.assignedServices.collectAsState().value

    // Estado de servicio detallado
    val detailedServiceState = mechanicsViewModel.detailedService.collectAsState().value

    // ==========================================
    // EFECTOS LATERALES
    // ==========================================

    // Cargar servicios asignados al abrir la pantalla
    LaunchedEffect(Unit) {
        mechanicsViewModel.loadAssignedServices()
    }

    // ==========================================
    // STATE PARA CONTROLAR MODAL DE DETALLES
    // ==========================================

    var selectedServiceId by  remember { mutableStateOf<String?>(null) }
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
                println("Error al cargar detalles del servicio: ${detailedServiceState.exception.message}")
            }
            else -> {
                // Loading o Idle
            }
        }
    }

    // ==========================================
    // DEFINIR ACCIONES RÁPIDAS
    // ==========================================
    // Definir acciones para las tarjetas de ActionCards
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
    // CONVERTIR RESPUESTA A SERVICECARD DATA
    // ==========================================
    // Convertir datos reales a ServiceCardData
    /*
    val services = when (servicesState) {
        is Result.Loading -> {
            // Mostrar lista vacía mientras carga
            emptyList<ServiceCardData>()  // ✨ ESPECIFICAR TIPO
        }
        is Result.Success -> {
            servicesState.data.workOrder
                .flatMap { workOrder ->
                    workOrder.assignedServices.map { service ->
                        ServiceCardData(
                            id = service.id,
                            excecutionId = service.executionId,
                            title = service.serviceType.name,
                            clientName = workOrder.vehicle.licensePlate,
                            icon = Icons.Filled.Build,
                            status = service.status.replaceFirstChar { it.uppercase() },
                            startTime = service.scheduledStart ?: "N/A",
                            endTime = service.scheduledEnd ?: "N/A",
                            duration = "${service.serviceType.estimatedDurationMinutes} min",
                            address = workOrder.zone.name,
                            priority = service.priority.replaceFirstChar { it.uppercase() } ?: "Media",
                            note = service.notes,
                            onCompleteClick = { onServiceComplete(service.executionId) },
                            onRescheduleClick = { onServiceReschedule(service.id) }
                        )
                    }
                }
        }
        is Result.Error -> {
            // En caso de error, mostrar lista vacía (manejaremos el error abajo)
            emptyList<ServiceCardData>()  // ✨ ESPECIFICAR TIPO
        }
        else -> {
            emptyList<ServiceCardData>()  // ✨ ESPECIFICAR TIPO
        }
    }
    */

    // ==========================================
    // MODAL DE CONFIRMACIÓN (LOGOUT)
    // ==========================================

    // Mostrar modal de confirmación si es necesario
    if (state.showLogoutDialog) {
        ConfirmationDialog(
            title = "Cerrar Sesión",
            content = "¿Estás seguro de que deseas cerrar sesión?",
            primaryButtonText = "Cerrar Sesión",
            secondaryButtonText = "Cancelar",
            onPrimaryClick = {
                // Confirmar logout
                homeViewModel.onEvent(HomeEvent.ConfirmLogout)
                onLogoutConfirmed()
            },
            onSecondaryClick = {
                // Cancelar logout
                homeViewModel.onEvent(HomeEvent.CancelLogout)
            },
            onDismiss = {
                // Cerrar dialog
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
                userName = state.userName,
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
                unsyncTitle = "4 Servicios",
                unsyncDetails = "2 Firmas, 8 Fotos, 1 Observación",
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
                    println("Action selected: $actionId")
                },
                columns = 3
            )
        },
        // 4. Métricas
        metricsSection = {
            MetricsSection(
                completedCount = "12",
                inProgressCount = "3",
                pendingCount = "5",
                efficiencyPercentage = "92%"
            )
        },
        // 5. Lista de servicios ✨ NUEVO
        serviceListSection = {
            when (servicesState) {
                is Result.Loading -> {
                    // Mostrar loading
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Result.Success -> {

                    // Convertir datos reales a ServiceCardData
                    val services = servicesState.data.workOrder
                        .flatMap { workOrder ->
                            workOrder.assignedServices.map { service ->
                                ServiceCardData(
                                    id              = service.id,
                                    excecutionId    = service.executionId,
                                    title           = service.serviceType.name,
                                    clientName      = workOrder.vehicle.licensePlate,
                                    icon            = Icons.Filled.Build,
                                    status          = service.status.replaceFirstChar { it.uppercase() },
                                    startTime       = service.scheduledStart ?: "N/A",
                                    endTime         = service.scheduledEnd ?: "N/A",
                                    duration        = "${service.serviceType.estimatedDurationMinutes} min",
                                    address         = workOrder.zone.name,
                                    priority        = service.priority.replaceFirstChar { it.uppercase() } ?: "Media",
                                    note            = service.notes
                                )
                            }
                        }

                    if (services.isEmpty()) {
                        // Mostrar mensaje si no hay servicios
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay servicios asignados")
                        }
                    } else {
                        // Mostrar servicios
                        ServiceListSectionOrganism(
                            title = "Servicios Asignados",
                            services = services,
                            onServiceClick = { serviceId ->
                                println("Service clicked: $serviceId")
                            },
                            onCompleteClick = { serviceId ->
                                println("Service completed: $serviceId")
                                selectedServiceId = serviceId
                                mechanicsViewModel.loadDetailedService(serviceId)
                                //onServiceComplete(serviceId)
                            },
                            onRescheduleClick = { serviceId ->
                                println("Service rescheduled: $serviceId")
                                onServiceReschedule(serviceId)
                            }
                        )
                    }
                }
                is Result.Error -> {
                    // Mostrar error
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
                                text = "Error al cargar servicios",
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = servicesState.exception.message ?: "Error desconocido",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                            )
                            Button(
                                onClick = {
                                    mechanicsViewModel.loadAssignedServices()
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }

                else -> {}
            }
        },
        modifier = modifier
    )
}

/**
* Modal para mostrar detalles de un servicio ✨ NUEVO
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
 * Preview de HomePage
 */
@Preview(showSystemUi = true)
@Composable
fun HomePagePreview() {
    LosabosTheme {
        HomeScreen()
    }
}

/**
 * NOTAS DE ACTUALIZACIÓN:
 *
 * ✨ CAMBIOS REALIZADOS:
 *
 * 1. Agregado parámetro serviceListSection a HomeTemplate
 *    - Integra la lista de servicios
 *    - Posicionada después de Metrics
 *
 * 2. Creado listado de ejemplo de servicios
 *    - 3 servicios de ejemplo con datos realistas
 *    - Diferentes tipos (Mantenimiento, Reparación, Inspección)
 *    - Estados variados (Programado, En Progreso, Pendiente)
 *    - Prioridades diferentes (Alta, Media, Baja)
 *
 * 3. Callbacks para acciones de servicios
 *    - onServiceComplete: cuando se completa un servicio
 *    - onServiceReschedule: cuando se reprograma un servicio
 *
 * 4. ServiceListSectionOrganism integrado
 *    - Muestra la lista de servicios
 *    - Maneja clicks en servicios
 *    - Maneja clicks en botones de acción
 *
 * PRÓXIMOS PASOS:
 *
 * 1. Conectar con ViewModel para obtener servicios reales
 * 2. Implementar lógica para completar/reprogramar servicios
 * 3. Agregar navegación a detalles de servicio
 * 4. Implementar escaneo de QR
 * 5. Agregar panel de tareas dentro de cada servicio
 */

/*
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
    onCameraClick: () -> Unit = {},
    onReportsClick: () -> Unit = {},
    onLocationClick: () -> Unit = {},
    onServiceComplete: (String) -> Unit = {},      // ✨ NUEVO
    onServiceReschedule: (String) -> Unit = {},    // ✨ NUEVO
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // ✅ Observar estado (nombre, ubicación, etc.)
    val state = viewModel.state.collectAsState().value

    // ✅ NUEVO: Definir acciones para las tarjetas de ActionCards
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
        actionsSection = {
            ActionCardsSection(
                actions = actionCards,
                title = "Acciones Rápidas",
                onActionClick = { actionId ->
                    println("Action selected: $actionId")
                    //viewModel.onActionSelected(actionId)
                },
                columns = 3  // Grid de 3 columnas
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
*/