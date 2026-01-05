// presentation/ui/components/molecules/ServiceMolecules.kt

package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.atoms.ActionButtonAtom
import com.lossabinos.serviceapp.ui.components.atoms.LocationAtom
import com.lossabinos.serviceapp.ui.components.atoms.NoteBoxAtom
import com.lossabinos.serviceapp.ui.components.atoms.PriorityBadgeAtom
import com.lossabinos.serviceapp.ui.components.atoms.ServiceBadge
import com.lossabinos.serviceapp.ui.components.atoms.ServiceIcon
import com.lossabinos.serviceapp.ui.components.atoms.ServiceSubtitle
import com.lossabinos.serviceapp.ui.components.atoms.TimeSlotAtom
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.lossabinos.serviceapp.ui.components.atoms.SyncStatusBadge
import com.lossabinos.serviceapp.ui.theme.LosabosTheme


// ==========================================
// 1. SERVICE HEADER MOLECULE
// ==========================================
/**
 * Encabezado del servicio: Icono + Título + Badge
 * @param icon Icono del servicio
 * @param title Título del servicio
 * @param status Estado del servicio
 * @param clientName Nombre del cliente
 */
@Composable
fun ServiceHeaderMolecule(
    icon: ImageVector,
    title: String,
    status: String,
    statusBackgroundColor: Color = Color(0xFFE0E0E0),
    statusTextColor: Color = Color(0xFF424242),
    clientName: String,
    syncStatus: String = "SYNCED"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),  // ✅ Permite que crezca en altura
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Icono
        ServiceIcon(
            icon = icon,
            backgroundColor = Color(0xFFFFF3E0),
            iconColor = Color(0xFFFFC107),
            size = 56.dp
        )

        // Título, estado y cliente
        Column(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(),  // ✅ Permite que crezca
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // ← FILA 1: Solo título (más espacio)
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // ← FILA 2: Cliente
            ServiceSubtitle(text = "Cliente: $clientName")

            // ← FILA 3: Badges (en su propio renglón)
            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ServiceBadge(
                    text = status,
                    backgroundColor = statusBackgroundColor,
                    textColor = statusTextColor
                )
                if (syncStatus.isNotEmpty()) {
                    SyncStatusBadge(syncStatus = syncStatus)
                }
            }


            /*
            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ServiceBadge(
                    text = status,
                    backgroundColor = statusBackgroundColor,
                    textColor = statusTextColor
                )

                SyncStatusBadge(
                    syncStatus = syncStatus
                )
            }
            */

            /*
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),  // ✅ Permite que crezca en altura
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top  // ✅ Cambiar a Top para alinear mejor
                        ) {
                            // ✅ MEJORADO: Título con múltiples líneas
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),  // ✅ Espacio antes del badge
                                maxLines = 2,  // ✅ Permitir hasta 2 líneas
                                overflow = TextOverflow.Ellipsis  // ✅ ... si es muy largo
                            )

                            // ← AGREGAR: Ambos badges
                            Row(
                                modifier = Modifier.wrapContentWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                // Badge 1: Status del servicio
                                ServiceBadge(
                                    text = status,
                                    backgroundColor = statusBackgroundColor,
                                    textColor = statusTextColor
                                )

                                // Badge 2: Status de sincronización
                                SyncStatusBadge(
                                    syncStatus = syncStatus
                                )
                            }


                            /*
                            // ✅ MEJORADO: Badge flexible
                            ServiceBadge(
                                text = status,
                                backgroundColor = statusBackgroundColor,
                                textColor = statusTextColor,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(start = 4.dp)  // ✅ Pequeño padding
                            )
                            */
                        }
                        ServiceSubtitle(text = "Cliente: $clientName")
             */
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFFFFFF
)
@Composable
fun ServiceHeaderMoleculePreview(){
    LosabosTheme {
        ServiceHeaderMolecule(
            icon = Icons.Filled.Build,
            title = "Mantenimiento Preventivo",
            status = "Reprogramado",  // ← Texto largo
            clientName = "Global Logistics"
        )
    }
}

// ==========================================
// 2. SERVICE TIME LINE MOLECULE
// ==========================================
/**
 * Línea de tiempo: Hora + Duración
 * @param startTime Hora de inicio
 * @param endTime Hora de fin
 * @param duration Duración
 */
@Composable
fun ServiceTimeLineMolecule(
    startTime: String,
    endTime: String,
    duration: String
) {
    TimeSlotAtom(
        startTime = startTime,
        endTime = endTime,
        duration = duration
    )
}

// ==========================================
// 3. SERVICE LOCATION MOLECULE
// ==========================================
/**
 * Ubicación del servicio
 * @param address Dirección
 */
@Composable
fun ServiceLocationMolecule(
    address: String
) {
    LocationAtom(address = address)
}

// ==========================================
// 4. PRIORITY INDICATOR MOLECULE
// ==========================================
/**
 * Indicador de prioridad
 * @param priority Nivel de prioridad
 */
@Composable
fun PriorityIndicatorMolecule(
    priority: String
) {
    PriorityBadgeAtom(priority = priority)
}

// ==========================================
// 5. SERVICE INFO ROW MOLECULE
// ==========================================
/**
 * Fila con información del servicio (hora + ubicación)
 * @param startTime Hora inicio
 * @param endTime Hora fin
 * @param duration Duración
 * @param address Dirección
 */
@Composable
fun ServiceInfoRowMolecule(
    startTime: String,
    endTime: String,
    duration: String,
    address: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ServiceTimeLineMolecule(
            startTime = startTime,
            endTime = endTime,
            duration = duration
        )

        ServiceLocationMolecule(address = address)
    }
}

// ==========================================
// 6. SERVICE DETAILS MOLECULE
// ==========================================
/**
 * Detalles del servicio (ubicación + prioridad)
 * @param address Dirección
 * @param priority Prioridad
 */
@Composable
fun ServiceDetailsMolecule(
    address: String,
    priority: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ServiceLocationMolecule(address = address)
        PriorityIndicatorMolecule(priority = priority)
    }
}

// ==========================================
// 7. SERVICE NOTE MOLECULE
// ==========================================
/**
 * Nota del servicio
 * @param note Texto de la nota
 */
@Composable
fun ServiceNoteMolecule(
    note: String
) {
    NoteBoxAtom(text = note)
}

// ==========================================
// 8. ACTION BUTTONS GROUP MOLECULE
// ==========================================
/**
 * Grupo de botones de acción
 * @param onCompleteClick Callback para completar
 * @param onRescheduleClick Callback para reprogramar
 */
@Composable
fun ActionButtonsGroupMolecule(
    onCompleteClick: () -> Unit,
    onRescheduleClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Botón Completar
        ActionButtonAtom(
            text = "Completar",
            backgroundColor = Color(0xFFFFC107),  // Amarillo
            textColor = Color.Black,
            icon = Icons.Filled.Check,
            onClick = onCompleteClick,
            modifier = Modifier.fillMaxWidth()
        )

        // Botón Reprogramar
        ActionButtonAtom(
            text = "Reprogramar",
            backgroundColor = Color(0xFFE0E0E0),  // Gris
            textColor = Color.Black,
            icon = Icons.Filled.Schedule,
            onClick = onRescheduleClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ==========================================
// 9. SERVICE SUMMARY MOLECULE
// ==========================================
/**
 * Resumen del servicio (todo junto)
 * @param title Título
 * @param clientName Cliente
 * @param startTime Hora inicio
 * @param endTime Hora fin
 * @param duration Duración
 * @param address Dirección
 * @param priority Prioridad
 */
@Composable
fun ServiceSummaryMolecule(
    title: String,
    clientName: String,
    startTime: String,
    endTime: String,
    duration: String,
    address: String,
    priority: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Tiempo
        ServiceTimeLineMolecule(
            startTime = startTime,
            endTime = endTime,
            duration = duration
        )

        // Ubicación + Prioridad
        ServiceDetailsMolecule(
            address = address,
            priority = priority
        )
    }
}

