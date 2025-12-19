package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.atoms.CardTitleAtom
import com.lossabinos.serviceapp.ui.components.atoms.IconWithBackgroundAtom
import com.lossabinos.serviceapp.ui.components.atoms.InputFieldAtom

@Composable
fun OilTypeCardMolecule(
    value: String,
    onValueChange: (String) -> Unit,
    oilTypes: List<String> = listOf("5W-30", "10W-40", "15W-40", "0W-20")
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header con icono y título
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconWithBackgroundAtom(
                iconRes = Icons.Filled.Settings,
                backgroundColor = Color(0xFFFFF3CD),
                iconColor = Color(0xFFFFC107),
                size = 48.dp
            )
            CardTitleAtom(title = "Tipo de Aceite")
        }

        // Divider
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = Color(0xFFE0E0E0)
        )

        // Dropdown o Input field
        InputFieldAtom(
            value = value,
            onValueChange = onValueChange,
            placeholder = "Selecciona o ingresa tipo",
            keyboardType = KeyboardType.Text
        )
    }
}