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
    // 1️⃣ OBSERVAR ESTADOS
    // ==========================================

    // Estado general del home (nombre, ubicación, etc.)
    val state = homeViewModel.state.collectAsState().value

    // ✅ NUEVO: Prioridad a Room
    // Estado de la carga desde Room (PRIMARIO)
    val localInitialDataState = mechanicsViewModel.localInitialData.collectAsState().value

    // Estado de la carga desde API (SECUNDARIO - para actualizar Room)
    val syncInitialDataState = mechanicsViewModel.syncInitialData.collectAsState().value

    // Estado de servicio detallado (para modal)
    val detailedServiceState = mechanicsViewModel.detailedService.collectAsState().value

    // ==========================================
    // 2️⃣ DECISIÓN: ¿QUÉ DATOS MOSTRAR?
    // ==========================================
    // ✅ ROOM SIEMPRE PRIMERO (si está disponible)
    val dataToDisplay = when {
        // 1️⃣ Prioridad 1: ROOM (SIEMPRE)
        localInitialDataState is Result.Success -> {
            println("✅ [DISPLAY] Mostrando datos de ROOM (fuente principal)")
            localInitialDataState.data
        }
        // 2️⃣ Prioridad 2: API (FALLBACK)
        syncInitialDataState is Result.Success -> {
            println("⚠️ [DISPLAY] Mostrando datos de API (ROOM no disponible)")
            syncInitialDataState.data
        }
        // 3️⃣ Sin datos
        else -> {
            println("⏳ [DISPLAY] Esperando datos de ROOM...")
            null
        }
    }

    // ==========================================
    // 3️⃣ LÓGICA: DETECTAR CAMBIOS EN ROOM
    // ==========================================
    LaunchedEffect(localInitialDataState) {
        when (localInitialDataState) {
            is Result.Success -> {
                val serviceCount = localInitialDataState.data.assignedServices.size
                println("✅ [ROOM] Datos de Room cargados:")
                println("   📊 Servicios: $serviceCount")
                println("   🚗 Mecánico: ${localInitialDataState.data.mechanic.name}")
                println("   📋 Total: ${localInitialDataState.data.syncMetadata.totalServices}")
            }
            is Result.Loading -> {
                println("⏳ [ROOM] Cargando datos de Room desde SQLite...")
            }
            is Result.Error -> {
                println("❌ [ROOM] Error al cargar Room: ${localInitialDataState.exception.message}")
            }
            else -> {
                println("🔄 [ROOM] Estado: Idle")
            }
        }
    }

    // ==========================================
    // 4️⃣ LÓGICA: DETECTAR CAMBIOS EN API
    // ==========================================
    // La API SOLO se usa para actualizar Room en background
    LaunchedEffect(syncInitialDataState) {
        when (syncInitialDataState) {
            is Result.Success -> {
                val serviceCount = syncInitialDataState.data.assignedServices.size
                println("📱 [API] Datos de API recibidos:")
                println("   📊 Servicios: $serviceCount")
                println("   🔄 Actualizando Room...")
                // Aquí se guarda automáticamente en Room via repositorio
                // No hace falta hacer nada, el repository ya lo hace
            }
            is Result.Loading -> {
                println("⏳ [API] Llamando API en background...")
            }
            is Result.Error -> {
                println("❌ [API] Error al llamar API: ${syncInitialDataState.exception.message}")
                println("✅ [API] Continuando con datos de Room (offline-first)")
            }
            else -> {
                println("🔄 [API] Estado: Idle")
            }
        }
    }

    // ==========================================
    // 5️⃣ EFECTOS LATERALES: CARGAR DATOS
    // ==========================================
    LaunchedEffect(Unit) {
        println("\n📱 ═══════════════════════════════════════════════════════")
        println("📱 HomeScreen abierto - Iniciando carga de datos")
        println("📱 ═══════════════════════════════════════════════════════\n")

        // 1️⃣ PRIMERO: Cargar Room (instantáneo - ~50ms)
        println("1️⃣ [LOAD] Iniciando carga de ROOM (fuente principal)")
        mechanicsViewModel.loadLocalData()
        println("1️⃣ [LOAD] ✅ ROOM cargado (instantáneo - ~50ms)\n")

        // 2️⃣ SEGUNDO: Cargar API en background (200-500ms)
        println("2️⃣ [LOAD] Iniciando carga de API en background (para actualizar)")
        mechanicsViewModel.loadInitialData()
        println("2️⃣ [LOAD] ✅ API en progreso (resultado en saveToRoom)\n")
    }

    // ==========================================
    // 6️⃣ STATE: MODAL DE DETALLES DE SERVICIO
    // ==========================================

    var selectedServiceId by remember { mutableStateOf<String?>(null) }
    var showDetailModal by remember { mutableStateOf(false) }

    // Observar cambios en detailedService para abrir modal
    LaunchedEffect(detailedServiceState) {
        when (detailedServiceState) {
            is Result.Success -> {
                println("✅ [DETAIL] Detalles del servicio cargados - Abriendo modal")
                showDetailModal = true
            }
            is Result.Error -> {
                println("❌ [DETAIL] Error: ${detailedServiceState.exception.message}")
            }
            else -> {
                // Loading o Idle
            }
        }
    }

    // ==========================================
    // 7️⃣ DEFINIR ACCIONES RÁPIDAS
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
    // 8️⃣ MODAL: CONFIRMACIÓN DE LOGOUT
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

    // ==========================================
    // 9️⃣ MODAL: DETALLES DE SERVICIO
    // ==========================================

    if (showDetailModal && detailedServiceState is Result.Success) {
        ServiceDetailModal(
            detailedService = detailedServiceState.data,
            onDismiss = {
                println("❌ [DETAIL] Cerrando modal")
                showDetailModal = false
                selectedServiceId = null
            }
        )
    }

    // ==========================================
    // 🔟 HOME TEMPLATE: ESTRUCTURA PRINCIPAL
    // ==========================================

    HomeTemplate(
        // ─────────────────────────────────────────
        // 1. Header del usuario
        // ─────────────────────────────────────────
        headerSection = {
            HomeHeaderSection(
                // ✅ Usar datos de ROOM (dataToDisplay)
                userName = dataToDisplay?.mechanic?.name ?: state.userName,
                userLocation = dataToDisplay?.mechanic?.zoneName ?: state.userLocation,
                isOnline = true,
                onSettingsClick = onSettingsClick,
                onLogoutClick = {
                    println("🔐 [LOGOUT] Usuario presionó logout")
                    homeViewModel.onEvent(HomeEvent.LogoutClicked)
                }
            )
        },

        // ─────────────────────────────────────────
        // 2. Sección de sincronización
        // ─────────────────────────────────────────
        syncSection = {
            SyncSection(
                statusText = "Estás en línea",
                lastSyncText = "Última sincronización: Hoy 10:45 AM",
                unsyncTitle = "${dataToDisplay?.syncMetadata?.totalServices ?: 0} Servicios",
                unsyncDetails = "${dataToDisplay?.syncMetadata?.pendingServices ?: 0} Pendientes, ${dataToDisplay?.syncMetadata?.inProgressServices ?: 0} En Progreso",
                onSyncClick = {
                    println("🔄 [SYNC] Usuario presionó sincronizar")
                    onSyncClick()
                },
                onSyncNowClick = {
                    println("⚡ [SYNC] Usuario presionó sincronizar ahora")
                    mechanicsViewModel.loadInitialData()  // Forzar sincronización
                    onSyncNowClick()
                }
            )
        },

        // ─────────────────────────────────────────
        // 3. Acciones rápidas (ActionCards)
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
        // 4. Métricas (datos de ROOM)
        // ─────────────────────────────────────────
        metricsSection = {
            MetricsSection(
                completedCount = "0",
                // ✅ Usar datos de ROOM
                inProgressCount = (dataToDisplay?.syncMetadata?.inProgressServices ?: 0).toString(),
                pendingCount = (dataToDisplay?.syncMetadata?.pendingServices ?: 0).toString(),
                efficiencyPercentage = "92%"
            )
        },

        // ─────────────────────────────────────────
        // 5. Lista de servicios (la más importante)
        // ─────────────────────────────────────────
        serviceListSection = {
            when {
                // ESTADO 1: Cargando desde Room o API
                localInitialDataState is Result.Loading || syncInitialDataState is Result.Loading -> {
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

                // ESTADO 2: Datos disponibles (de ROOM)
                dataToDisplay != null -> {
                    println("✅ [UI] Estado: Mostrando servicios de ROOM")

                    // Convertir AssignedService → ServiceCardData para UI
                    val services = dataToDisplay.assignedServices.map { service ->
                        // Buscar el nombre del tipo de servicio
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
                        println("ℹ️  [UI] No hay servicios asignados")
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✅ No hay servicios asignados")
                        }
                    } else {
                        println("📋 [UI] Mostrando ${services.size} servicios")
                        ServiceListSectionOrganism(
                            title = "Servicios Asignados",
                            services = services,
                            onServiceClick = { serviceId ->
                                println("👆 [UI] Service clicked: $serviceId")
                            },
                            onCompleteClick = { serviceId ->
                                println("✓ [UI] Service completed (detail): $serviceId")
                                selectedServiceId = serviceId
                                mechanicsViewModel.loadDetailedService(serviceId)
                            },
                            onRescheduleClick = { serviceId ->
                                println("📅 [UI] Service reschedule: $serviceId")
                                onServiceReschedule(serviceId)
                            }
                        )
                    }
                }

                // ESTADO 3: Error en ambos (Room Y API fallaron)
                localInitialDataState is Result.Error && syncInitialDataState is Result.Error -> {
                    println("❌ [UI] Estado: Error en ROOM y API")
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
                                text = localInitialDataState.let {
                                    if (it is Result.Error) it.exception.message ?: "Error desconocido"
                                    else "Error desconocido"
                                },
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                            )
                            Button(
                                onClick = {
                                    println("🔄 [UI] Usuario presionó reintentar")
                                    mechanicsViewModel.loadLocalData()
                                    mechanicsViewModel.loadInitialData()
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }

                // ESTADO 4: Por defecto (esperando)
                else -> {
                    println("⏳ [UI] Estado: Esperando datos...")
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