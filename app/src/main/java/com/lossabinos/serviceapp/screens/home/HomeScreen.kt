// presentation/screens/home/HomeScreen.kt
package com.lossabinos.serviceapp.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lossabinos.domain.responses.DetailedServiceResponse
import com.lossabinos.domain.enums.ServiceStatus
import com.lossabinos.serviceapp.mappers.toServiceCardUiModel
import com.lossabinos.serviceapp.models.ui.ServiceCardUiModel
import com.lossabinos.serviceapp.utils.getStatusColor


/**
 * HomePage - Página principal de la aplicación ✨ ACTUALIZADA v1.7.0
 *
 * CAMBIOS PRINCIPALES:
 * =====================
 * ✅ Room es la fuente PRINCIPAL (siempre se usa)
 * ✅ API es SOLO para actualizar Room en background
 * ✅ dataToDisplay SIEMPRE muestra Room (si está disponible)
 * ✅ Si API falla → Sigue funcionando con Room
 * ✅ Si no hay conexión → Funciona perfectamente con Room
 *
 * Estructura Atomic Design:
 * - Atoms: Avatar, MetricIcon, StatusBadge, ActionButton, PrimaryButton, StatusText
 * - Molecules: UserHeader, MetricCard, StatusSection, UnsyncSection, ModalButtonGroup
 * - Organisms: HomeHeaderSection, MetricsSection, SyncSection, ConfirmationDialog
 *             ActionCardsSection, ServiceListSectionOrganism
 * - Template: HomeTemplate (estructura sin datos)
 * - Page: HomePage (pantalla completa con datos)
 *
 * FLUJO DE DATOS CORRECTO:
 * ========================
 * 1. LaunchedEffect ejecuta:
 *    ├─ loadLocalData() → Lee de Room (PRIMERO - rápido)
 *    └─ loadInitialData() → Llama API (SEGUNDO - background)
 *
 * 2. dataToDisplay decisión:
 *    ├─ Si Room disponible → Usar Room ✅
 *    ├─ Si Room falla → Usar API como fallback
 *    └─ Sino → null (mostrar loading)
 *
 * 3. Si API trae datos:
 *    ├─ saveToRoom(apiData) → Actualizar Room
 *    └─ dataToDisplay sigue siendo Room (pero actualizado)
 *
 * 4. Si API falla:
 *    ├─ Continuar mostrando Room
 *    └─ Sin cambios en UI
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
    // 1️⃣ OBSERVAR DATOS
    // ==========================================
    // Estado general del home (nombre, ubicación, logout, etc.)
    val state = homeViewModel.state.collectAsState().value
    val mechanic    = mechanicsViewModel.mechanic.collectAsStateWithLifecycle().value
    val services    = mechanicsViewModel.assignedServices.collectAsStateWithLifecycle().value
    val types       = mechanicsViewModel.serviceTypes.collectAsStateWithLifecycle().value
    val metrics =
        mechanicsViewModel.homeMetrics.collectAsStateWithLifecycle().value
    val errorMessage = homeViewModel.errorMessage.collectAsStateWithLifecycle().value
    val isLoading = homeViewModel.isLoading.collectAsStateWithLifecycle().value
    val uiMessage = homeViewModel.uiMessage.collectAsStateWithLifecycle().value

    // ==========================================
    // 1️⃣ ESTADOS WEBSOCKET
    // ==========================================
    val isWebSocketConnected = homeViewModel.isWebSocketConnected.collectAsStateWithLifecycle().value
    val webSocketNotification = homeViewModel.webSocketNotification.collectAsStateWithLifecycle().value
    val webSocketAlert = homeViewModel.webSocketAlert.collectAsStateWithLifecycle().value
    val webSocketError = homeViewModel.webSocketError.collectAsStateWithLifecycle().value


    // ==========================================
    // 2️⃣ CARGAR DATOS AL ABRIR PANTALLA
    // ==========================================
    LaunchedEffect(Unit) {
        println("\n📱 ═══════════════════════════════════════════════════════")
        println("📱 HomeScreen abierto - Iniciando carga de datos")
        println("📱 ═══════════════════════════════════════════════════════\n")

        // ✨ SOLO cargar API (los Flows se auto-observan de Room)
        mechanicsViewModel.loadInitialData()
    }

    // ==========================================
    // ALERTA DE ERROR
    // ==========================================
    if (!errorMessage.isNullOrEmpty()) {
        AlertDialog(
            onDismissRequest = { homeViewModel.clearError() },
            title = {
                Text(
                    "❌ Error en la Sincronización",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = errorMessage,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            },
            confirmButton = {
                Button(
                    onClick = { homeViewModel.clearError() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F)
                    )
                ) {
                    Text("Entendido", color = Color.White)
                }
            }
        )
    }

    // =================================
    // ALERTA DE NOTIFICACIÓN WEBSOCKET
    // =================================
    if (!webSocketNotification.isNullOrEmpty()) {
        AlertDialog(
            onDismissRequest = { homeViewModel.clearWebSocketNotification() },
            title = {
                Text(
                    "🔔 Notificación",
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = webSocketNotification,
                    fontSize = 16.sp,
                    color = Color.Black,
                    lineHeight = 22.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { homeViewModel.clearWebSocketNotification() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2)
                    )
                ) {
                    Text("Entendido", color = Color.White)
                }
            }
        )
    }

    // =================================
    // ALERTA DE MANTENIMIENTO WEBSOCKET
    // ==================================
    if (!webSocketAlert.isNullOrEmpty()) {
        AlertDialog(
            onDismissRequest = { homeViewModel.clearWebSocketAlert() },
            title = {
                Text(
                    "⚠️ Alerta de Mantenimiento",
                    color = Color(0xFFD32F2F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = webSocketAlert,
                    fontSize = 16.sp,
                    color = Color.Black,
                    lineHeight = 22.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { homeViewModel.clearWebSocketAlert() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F)
                    )
                ) {
                    Text("Entendido", color = Color.White)
                }
            }
        )
    }

    // ==========================================
    //  ERROR DE WEBSOCKET
    // ==========================================
    if (!webSocketError.isNullOrEmpty()) {
        AlertDialog(
            onDismissRequest = { homeViewModel.clearWebSocketError() },
            title = {
                Text(
                    "❌ Error WebSocket",
                    color = Color(0xFFD32F2F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = webSocketError,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            },
            confirmButton = {
                Button(
                    onClick = { homeViewModel.clearWebSocketError() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F)
                    )
                ) {
                    Text("Entendido", color = Color.White)
                }
            }
        )
    }



    // ==========================================
    // LOADING OVERLAY
    // ==========================================
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(50.dp)
            )
        }
    }

    // ==========================================
    // 3️⃣ MODAL DE LOGOUT
    // ==========================================
    if (state.showLogoutDialog) {
        ConfirmationDialog(
            title = "Cerrar Sesión",
            content = "¿Estás seguro de que deseas cerrar sesión?",
            primaryButtonText = "Cerrar Sesión",
            secondaryButtonText = "Cancelar",
            onPrimaryClick = {
                println("📤 [LOGOUT] Confirmar logout")
                homeViewModel.onEvent(HomeEvent.ConfirmLogout)
                onLogoutConfirmed()
            },
            onSecondaryClick = {
                println("🚫 [LOGOUT] Cancelar logout")
                homeViewModel.onEvent(HomeEvent.CancelLogout)
            },
            onDismiss = {
                homeViewModel.onEvent(HomeEvent.CancelLogout)
            }
        )
    }

    if (!uiMessage.isNullOrEmpty()) {
        AlertDialog(
            onDismissRequest = { homeViewModel.clearMessage() },
            title = { Text("Información") },
            text = { Text(uiMessage) },
            confirmButton = {
                Button(onClick = { homeViewModel.clearMessage() }) {
                    Text("OK")
                }
            }
        )
    }

    // ==========================================
    // 4️⃣ ACCIONES RÁPIDAS
    // ==========================================
    val actionCards = listOf(
        /*
        ActionCardModel(
            id = "camera",
            title = "Cámara",
            icon = Icons.Filled.Camera,
            onClick = onCameraClick
        ),*/
        ActionCardModel(
            id = "reports",
            title = "Reportes",
            icon = Icons.Filled.BarChart,
            onClick = onReportsClick
        )/*,
        ActionCardModel(
            id = "location",
            title = "Ubicación",
            icon = Icons.Filled.LocationOn,
            onClick = onLocationClick
        )*/
    )

/*
    // ===================
    // % Eficiencia
    // ==================
    val total       = metadata?.totalServices ?: 0
    val pending     = metadata?.pendingServices ?: 0
    val inProgress  = metadata?.inProgressServices ?: 0

    val completed   = (total - (pending + inProgress)).coerceAtLeast(0)

    val efficiencyPercentage: String =
        if (total > 0) {
            val efficiency =
                inProgress.toDouble() / total.toDouble() * 100

            "%.0f".format(efficiency)
        } else {
            "0"
        }
*/


    // ==========================================
    // 5️⃣ TEMPLATE PRINCIPAL
    // ==========================================
    HomeTemplate(
        // ─────────────────────────────────────────
        // Header del usuario
        // ─────────────────────────────────────────
        headerSection = {
            HomeHeaderSection(
                userName = mechanic?.name ?: state.userName,
                userLocation = mechanic?.zoneName ?: state.userLocation,
                isOnline = true,
                onSettingsClick = {
                    println("📬 Notifications clicked")
                    homeViewModel.onEvent(HomeEvent.NavigateToNotificationsClicked)  // ← O el evento que uses
                },
                onLogoutClick = {
                    println("🔐 [LOGOUT] Usuario presionó logout")
                    homeViewModel.onEvent(HomeEvent.LogoutClicked)
                }
            )
        },

        // ─────────────────────────────────────────
        // Sección de sincronización
        // ─────────────────────────────────────────
        syncSection = {
            SyncSection(
                statusText = "Estás en línea",
                lastSyncText = "Última sincronización: Hoy 10:45 AM",
                unsyncTitle = "${metrics.totalServices} servicios", //"${metadata?.totalServices ?: 0} Servicios",
                unsyncDetails = "${metrics.pendingServices} pendientes, ${metrics.inProgressServices} en progreso",
                isLoading = isLoading,
                    //"${metadata?.pendingServices ?: 0} Pendientes, ${metadata?.inProgressServices ?: 0} En Progreso",
                onSyncClick = {
                    println("🔄 [SYNC] Usuario presionó sincronizar")
                    onSyncClick()
                },
                onSyncNowClick = {
                    println("⚡ [SYNC] Usuario presionó sincronizar ahora")
                    mechanicsViewModel.loadInitialData()
                    onSyncNowClick()
                }
            )
        },

        // ─────────────────────────────────────────
        // Acciones rápidas
        // ─────────────────────────────────────────
        actionsSection = {
            ActionCardsSection(
                actions = actionCards,
                title = "Acciones Rápidas",
                onActionClick = { actionId ->
                    println("✅ [ACTION] Action selected: $actionId")
                },
                columns = 3
            )
        },

        // ─────────────────────────────────────────
        // Métricas
        // ─────────────────────────────────────────
        metricsSection = {
            MetricsSection(
                completedCount = metrics.completedServices.toString() ,//completed.toString(),
                inProgressCount = metrics.inProgressServices.toString() ,//inProgress.toString(),
                pendingCount = metrics.pendingServices.toString() ,//pending.toString(),
                efficiencyPercentage = "${metrics.efficiencyPercentage} %", //"$efficiencyPercentage %"
            )
        },

        // ─────────────────────────────────────────
        // Lista de servicios
        // ─────────────────────────────────────────
        serviceListSection = {
            when {
                // ESTADO 1: Sin datos
                services.isEmpty() && mechanic == null -> {
                    println("⏳ [UI] Estado: Cargando")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                // ESTADO 2: Con datos
                services.isNotEmpty() -> {
                    println("✅ [UI] Mostrando ${services.size} servicios")

                    val serviceCards = services.map { service ->
                        val serviceTypeName = types
                            .find { it.id == service.assignedService.serviceTypeId }?.name
                            ?: "Servicio"

                        // 🆕 Obtener colores basados en el estado
                        val statusColor = getStatusColor(service.assignedService.status)

                        service.toServiceCardUiModel(
                            serviceTypeName = serviceTypeName,
                            statusColor = statusColor
                        )
                    }

                    ServiceListSectionOrganism(
                        title = "Servicios Asignados",
                        services = serviceCards,
                        onServiceClick = { serviceId ->
                            println("👆 [UI] Service clicked: $serviceId")
                        },
                        onCompleteClick = { serviceId ->
                            println("✓ Service complete: $serviceId")
                            // ✨ NUEVO: Emitir evento
                            homeViewModel.onEvent(
                                HomeEvent.CompleteServiceClicked(serviceId)
                            )
                        },
                        onSyncClick = { serviceId ->
                            println("🔄 Sincronizando: $serviceId")
                            homeViewModel.onEvent(
                                HomeEvent.SyncServiceClicked(serviceId)
                            )
                        },
                        onRescheduleClick = { serviceId ->
                            println("📅 [UI] Service reschedule: $serviceId")
                            onServiceReschedule(serviceId)
                        }
                    )
                }

                // ESTADO 3: Sin servicios pero con datos
                true -> {
                    println("ℹ️  [UI] No hay servicios asignados")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "✅ No hay servicios asignados",
                            fontSize = 14.sp
                        )
                    }
                }

                // ESTADO 4: Por defecto
                else -> {
                    println("⏳ [UI] Estado: Esperando datos...")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Cargando servicios...",
                            fontSize = 14.sp
                        )
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
 * Se abre cuando el usuario presiona "Completar" en una tarjeta
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