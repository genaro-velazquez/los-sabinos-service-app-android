// presentation/ui/components/atoms/ServiceAtoms.kt

package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCode
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.utils.DateUtils

// ==========================================
// 1. SERVICE ICON ATOM
// ==========================================
/**
 * Icono de servicio en círculo con fondo
 * @param icon Icono a mostrar
 * @param backgroundColor Color de fondo
 * @param iconColor Color del icono
 * @param size Tamaño del icono
 */
@Composable
fun ServiceIcon(
    icon: ImageVector,
    backgroundColor: Color = Color(0xFFFFF3E0),  // Naranja claro
    iconColor: Color = Color(0xFFFFC107),        // Naranja
    size: Dp = 56.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}


// ==========================================
// SERVICE BADGE (MEJORADO)
// ==========================================
/**
 * Badge de estado flexible que no corta el texto
 *
 * ✅ Cambios:
 * - Agrega maxLines y overflow para textos largos
 * - Soporta modifier personalizado
 * - Padding automático según el contenido
 */
@Composable
fun ServiceBadge(
    text: String,
    backgroundColor: Color = Color(0xFFE0E0E0),
    textColor: Color = Color(0xFF424242),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp),  // ✅ Padding interno
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = textColor,
            maxLines = 1,  // ✅ Mantener en una línea
            overflow = TextOverflow.Ellipsis,  // ✅ ... si es muy largo
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ServiceBadgePreview(){
    MaterialTheme{
        ServiceBadge(
            text = "Pending"
        )
    }
}


// ==========================================
// 3. SERVICE TITLE ATOM
// ==========================================
/**
 * Título principal del servicio
 * @param text Texto del título
 */
@Composable
fun ServiceTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        fontSize = 18.sp,
        modifier = modifier
    )
}

// ==========================================
// 4. SERVICE SUBTITLE ATOM (Cliente)
// ==========================================
/**
 * Subtítulo con información del cliente
 * @param text Texto del subtítulo
 */
@Composable
fun ServiceSubtitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF9E9E9E),  // Gris
        fontSize = 12.sp,
        modifier = modifier
    )
}

// ==========================================
// 5. TIME SLOT ATOM
// ==========================================
/**
 * Muestra la hora del servicio
 * @param startTime Hora de inicio
 * @param endTime Hora de fin
 * @param duration Duración
 */
@Composable
fun TimeSlotAtom(
    startTime: String,
    endTime: String,
    duration: String
) {
    // 🆕 Calcula la duración automáticamente
    val calculatedDuration = DateUtils.calculateDuration(startTime, endTime)

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.AccessTime,
                contentDescription = "Hora",
                tint = Color.Black,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "${DateUtils.formatIsoToReadable(startTime)} - ${DateUtils.formatIsoToReadable(endTime)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(start = 26.dp)
        ) {
            Text(
                text = "Duración: $calculatedDuration",  // 🆕 Usa la calculada
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9E9E9E)
            )
        }
    }
}

// ==========================================
// 6. LOCATION ATOM
// ==========================================
/**
 * Muestra la ubicación del servicio
 * @param address Dirección
 */
@Composable
fun LocationAtom(
    address: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "Ubicación",
            tint = Color.Black,
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = address,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black
        )
    }
}

// ==========================================
// 7. PRIORITY BADGE ATOM
// ==========================================
/**
 * Badge de prioridad
 * @param priority Nivel de prioridad (Alta, Media, Baja)
 */
@Composable
fun PriorityBadgeAtom(
    priority: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Punto de color según prioridad
        val priorityColor = when (priority.lowercase()) {
            "alta" -> Color(0xFFF44336)        // Rojo
            "media" -> Color(0xFFFF9800)       // Naranja
            "baja" -> Color(0xFF4CAF50)        // Verde
            else -> Color(0xFF9E9E9E)          // Gris
        }

        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(priorityColor)
        )

        Text(
            text = "Prioridad $priority",
            style = MaterialTheme.typography.bodySmall,
            color = priorityColor,
            fontWeight = FontWeight.Medium
        )
    }
}

// ==========================================
// 8. NOTE BOX ATOM
// ==========================================
/**
 * Caja de nota con fondo azul
 * @param text Texto de la nota
 */
@Composable
fun NoteBoxAtom(
    text: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE3F2FD))  // Azul muy claro
            .padding(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF1976D2),  // Azul
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// ==========================================
// 9. ACTION BUTTON ATOM
// ==========================================
/**
 * Botón de acción para servicios
 * @param text Texto del botón
 * @param backgroundColor Color de fondo
 * @param textColor Color del texto
 * @param icon Icono opcional
 * @param onClick Callback de click
 */
@Composable
fun ActionButtonAtom(
    text: String,
    backgroundColor: Color = Color(0xFFFFC107),  // Amarillo
    textColor: Color = Color.Black,
    icon: ImageVector? = null,
    enabled: Boolean = true,              
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()  // ← Asegurar que ocupa todo el ancho
            .height(48.dp)   // ← AGREGAR ESTO (altura consistente)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(enabled = true) {  // ✨ AGREGA ESTO
                println("🔵 ActionButtonAtom: '$text' clicked")  // Debug
                onClick()
            }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = text,
                color = textColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

// ==========================================
// 10. QR SCAN BUTTON ATOM
// ==========================================
/**
 * Botón flotante para escanear QR
 * @param onClick Callback de click
 */
@Composable
fun QRScanButtonAtom(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE0E0E0)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.QrCode,
            contentDescription = "Escanear QR",
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
    }
}

// Para el icono QR, necesitas importar:
// import androidx.compose.material.icons.filled.QrCode
// O crear un icono SVG personalizado

