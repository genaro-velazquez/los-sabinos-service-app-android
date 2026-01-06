// presentation/ui/components/organisms/ServiceOrganisms.kt

package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp  // ✅ AGREGADO: Import para sp()
import androidx.compose.ui.unit.times
import com.lossabinos.domain.valueobjects.ServiceStatus
import com.lossabinos.serviceapp.ui.components.molecules.ActionButtonsGroupMolecule
import com.lossabinos.serviceapp.ui.components.molecules.ServiceHeaderMolecule
import com.lossabinos.serviceapp.ui.components.molecules.ServiceNoteMolecule
import com.lossabinos.serviceapp.ui.components.molecules.ServiceSummaryMolecule
import com.lossabinos.serviceapp.ui.theme.LosabosTheme

// ==========================================
// SERVICE CARD DATA CLASS
// ==========================================
data class ServiceCardData(
    val id: String,
    val excecutionId:String,
    val icon: ImageVector,
    val title: String,
    val status: String,
    val statusBackgroundColor: Color = Color(0xFFE0E0E0),  // 🆕
    val statusTextColor: Color = Color(0xFF424242),        // 🆕
    val clientName: String,
    val startTime: String,
    val endTime: String,
    val duration: String,
    val address: String,
    val priority: String,
    val note: String,
    val syncStatus: String = "SYNCED",         // "SYNCED", "PENDING", "ERROR"
    val serviceStatus: ServiceStatus = ServiceStatus.PENDING,
    val onCompleteClick: () -> Unit = {},
    val onRescheduleClick: () -> Unit = {},
    val onSyncClick: () -> Unit = {}  // ← AGREGAR

)

// ==========================================
// 1. SERVICE CARD ORGANISM
// ==========================================
/**
 * Tarjeta completa de servicio
 * Combina: Header + Timeline + Details + Note + Actions
 */
@Composable
fun ServiceCardOrganism(
    service: ServiceCardData,
    serviceStatus: ServiceStatus,      // ← AGREGAR
    syncStatus: String = "SYNCED",     // ← AGREGAR
    onSyncClick: () -> Unit = {}       // ← AGREGAR
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // 1. Encabezado (Icono + Título + Badge)
            ServiceHeaderMolecule(
                icon = service.icon,
                title = service.title,
                status = service.status,
                statusBackgroundColor = service.statusBackgroundColor,
                statusTextColor = service.statusTextColor,
                clientName = service.clientName,
                syncStatus = service.syncStatus
            )

            // 2. Separador
            HorizontalDivider(
                color = Color(0xFFE0E0E0),
                thickness = 1.dp
            )

            // 3. Información: Hora + Ubicación + Prioridad
            ServiceSummaryMolecule(
                title = service.title,
                clientName = service.clientName,
                startTime = service.startTime,
                endTime = service.endTime,
                duration = service.duration,
                address = service.address,
                priority = service.priority
            )

            // 4. Separador
            HorizontalDivider(
                color = Color(0xFFE0E0E0),
                thickness = 1.dp
            )

            // 5. Nota (si existe)
            if (service.note.isNotEmpty()) {
                ServiceNoteMolecule(note = service.note)
            }

            // 6. Botones de acción
            ActionButtonsGroupMolecule(
                serviceStatus = serviceStatus,           // ← PASAR
                syncStatus = syncStatus,                 // ← PASAR
                onCompleteClick = service.onCompleteClick,
                onSyncClick = onSyncClick,               // ← PASAR
                onRescheduleClick = service.onRescheduleClick
            )
        }
    }

    /*
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // 1. Encabezado (Icono + Título + Badge)
                ServiceHeaderMolecule(
                    icon = service.icon,
                    title = service.title,
                    status = service.status,
                    statusBackgroundColor = service.statusBackgroundColor,
                    statusTextColor = service.statusTextColor,
                    clientName = service.clientName,
                    syncStatus = service.syncStatus
                )

                // 2. Separador
                HorizontalDivider(
                    color = Color(0xFFE0E0E0),
                    thickness = 1.dp
                )

                // 3. Información: Hora + Ubicación + Prioridad
                ServiceSummaryMolecule(
                    title = service.title,
                    clientName = service.clientName,
                    startTime = service.startTime,
                    endTime = service.endTime,
                    duration = service.duration,
                    address = service.address,
                    priority = service.priority
                )

                // 4. Separador
                HorizontalDivider(
                    color = Color(0xFFE0E0E0),
                    thickness = 1.dp
                )

                // 5. Nota (si existe)
                if (service.note.isNotEmpty()) {
                    ServiceNoteMolecule(note = service.note)
                }

                // 6. Botones de acción
                ActionButtonsGroupMolecule(
                    onCompleteClick = service.onCompleteClick,
                    onRescheduleClick = service.onRescheduleClick
                )
            }
        }

     */
}

// ==========================================
// 2. SERVICE LIST ITEM ORGANISM (Versión Compacta)
// ==========================================
/**
 * Elemento compacto de lista de servicios
 * Muestra: Icono + Título + Cliente + Hora + Botones rápidos
 */
@Composable
fun ServiceListItemOrganism(
    service: ServiceCardData,
    onCardClick: (String) -> Unit = {},
    onQuickCompleteClick: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Encabezado compacto
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Icono
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFF3E0))
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = service.icon,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.padding(4.dp)
                    )
                }

                // Info compacta
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = service.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = service.clientName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9E9E9E)
                    )
                }

                // Badge de estado
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE0E0E0))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = service.status,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = Color(0xFF424242)
                    )
                }
            }

            // Hora
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Speed,
                    contentDescription = "Hora",
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier
                        .height(14.dp)
                )
                Text(
                    text = "${service.startTime} - ${service.endTime}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9E9E9E),
                    fontSize = 11.sp
                )
            }
        }
    }
}

// ==========================================
// 3. SERVICE LIST SECTION ORGANISM
// ==========================================
/**
 * Sección completa de lista de servicios
 * Incluye: Título + Lista de tarjetas
 */
@Composable
fun ServiceListSectionOrganism(
    title: String = "Servicios Asignados",
    services: List<ServiceCardData>,
    onServiceClick: (String) -> Unit = {},
    onCompleteClick: (String) -> Unit = {},
    onRescheduleClick: (String) -> Unit = {},
    onSyncClick: (String) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Título
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,  // ✅ CORREGIDO
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp)
        )

        // Lista de servicios
        if (services.isEmpty()) {
            // Mensaje vacío
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE3F2FD))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay servicios asignados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1976D2)
                )
            }
        } else {
            // Listado de servicios
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                services.forEach { service ->
                    ServiceCardOrganism(
                        service = service.copy(
                            onCompleteClick = { onCompleteClick(service.excecutionId) },
                            onRescheduleClick = { onRescheduleClick(service.excecutionId) },
                            onSyncClick = { onSyncClick(service.excecutionId) }
                        ),
                        /*
                        service = service.copy(
                            onCompleteClick = { onCompleteClick(service.excecutionId) },
                            onRescheduleClick = { onRescheduleClick(service.excecutionId) },
                            onSyncClick = { onSyncClick(service.excecutionId) }
                        ),
                        */
                        serviceStatus = service.serviceStatus,
                        syncStatus = service.syncStatus,
                        onSyncClick = { onSyncClick(service.excecutionId) }
                    )

                    /*
                    ServiceCardOrganism(
                        service = service.copy(
                            onCompleteClick = { onCompleteClick(service.excecutionId) },
                            onRescheduleClick = { onRescheduleClick(service.excecutionId) }
                        )
                    )

                     */
                }
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun ServiceListSectionOrganismPreview(){
    LosabosTheme {
        val services = listOf(
            ServiceCardData(
                id = "service_1",
                excecutionId = "execution_service_1",
                title = "Mantenimiento Preventivo",
                clientName = "Global Logistics",
                icon = Icons.Filled.Build,
                status = "Programado",
                startTime = "13:00",
                endTime = "14:00",
                duration = "1 hr",
                address = "Calle Falsa 123",
                priority = "Media",
                note = "No olvidar llevar equipo de seguridad adicional solicitado por el cliente.",
                onCompleteClick = {  },
                onRescheduleClick = {  }
            ),
            ServiceCardData(
                id = "service_2",
                excecutionId = "execution_service_2",
                title = "Reparación de Tubería",
                clientName = "Tech Solutions Inc.",
                icon = Icons.Filled.Plumbing,
                status = "En Progreso",
                startTime = "15:00",
                endTime = "16:30",
                duration = "1.5 hrs",
                address = "Avenida Principal 456",
                priority = "Alta",
                note = "Cliente requiere atención especial al sistema de refrigeración.",
                onCompleteClick = {  },
                onRescheduleClick = {  }
            ),
            ServiceCardData(
                id = "service_3",
                excecutionId = "execution_service_3",
                title = "Inspección General",
                clientName = "Manufacturing Corp",
                icon = Icons.Filled.Speed,
                status = "Pendiente",
                startTime = "10:00",
                endTime = "11:00",
                duration = "1 hr",
                address = "Calle Industrial 789",
                priority = "Baja",
                note = "Realizar revisión completa del sistema.",
                onCompleteClick = {  },
                onRescheduleClick = { }
            )
        )

        ServiceListSectionOrganism(
            title = "Servicios Asignados",
            services = services,
            onServiceClick = { serviceId ->

            },
            onCompleteClick = { serviceId ->
            },
            onRescheduleClick = { serviceId ->
            }
        )
    }
}

// ==========================================
// 4. SERVICE STATS CARD ORGANISM
// ==========================================
/**
 * Tarjeta de estadísticas de servicios
 */
@Composable
fun ServiceStatsCardOrganism(
    totalServices: Int,
    completedServices: Int,
    pendingServices: Int,
    inProgressServices: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Resumen de Servicios",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Total
                StatItemOrganism(
                    label = "Total",
                    value = totalServices.toString(),
                    color = Color(0xFF2196F3)
                )

                // Completados
                StatItemOrganism(
                    label = "Completados",
                    value = completedServices.toString(),
                    color = Color(0xFF4CAF50)
                )

                // En Progreso
                StatItemOrganism(
                    label = "En Progreso",
                    value = inProgressServices.toString(),
                    color = Color(0xFFFFC107)
                )

                // Pendientes
                StatItemOrganism(
                    label = "Pendientes",
                    value = pendingServices.toString(),
                    color = Color(0xFFF44336)
                )
            }
        }
    }
}

// ==========================================
// 5. STAT ITEM ORGANISM (Auxiliar)
// ==========================================
@Composable
fun StatItemOrganism(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF9E9E9E),
            fontSize = 10.sp
        )
    }
}

