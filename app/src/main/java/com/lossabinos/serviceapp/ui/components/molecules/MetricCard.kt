// presentation/ui/components/molecules/MetricCard.kt
package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.ui.components.atoms.MetricIcon
import com.lossabinos.serviceapp.ui.theme.CardBackground
import com.lossabinos.serviceapp.ui.theme.StatusYellow
import com.lossabinos.serviceapp.ui.theme.StatusYellowLight
import com.lossabinos.serviceapp.ui.theme.TextBlack
import com.lossabinos.serviceapp.ui.theme.TextGray

/**
 * Card de métrica con icono, etiqueta y número
 *
 * @param label Etiqueta (ej: "Completado")
 * @param value Valor a mostrar (ej: "12")
 * @param icon Icono del metric
 * @param iconBackgroundColor Color de fondo del icono
 * @param iconColor Color del icono
 * @param modifier Modifier para personalización
 */
@Composable
fun MetricCard(
    label: String = "Completado",
    value: String = "12",
    icon: ImageVector = Icons.Default.CheckCircle,
    iconBackgroundColor: Color = StatusYellowLight,
    iconColor: Color = StatusYellow,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(CardBackground, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            MetricIcon(
                icon = icon,
                backgroundColor = iconBackgroundColor,
                iconColor = iconColor
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = label,
                fontSize = 14.sp,
                color = TextGray,
                fontWeight = FontWeight.Normal
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = value,
                fontSize = 28.sp,
                color = TextBlack,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
