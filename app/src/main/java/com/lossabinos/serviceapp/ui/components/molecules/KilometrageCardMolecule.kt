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
import androidx.compose.material.icons.filled.Edit
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
import com.lossabinos.serviceapp.ui.components.atoms.SubtextAtom

@Composable
fun KilometrageCardMolecule(
    value: String,
    onValueChange: (String) -> Unit,
    lastKilometers: String = "45,230 km"
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
                iconRes = Icons.Filled.Edit,
                backgroundColor = Color(0xFFFFF3CD),
                iconColor = Color(0xFFFFC107),
                size = 48.dp
            )
            CardTitleAtom(title = "Kilometraje")
        }

        // Divider
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = Color(0xFFE0E0E0)
        )

        // Input field
        InputFieldAtom(
            value = value,
            onValueChange = onValueChange,
            placeholder = "000,000",
            keyboardType = KeyboardType.Number,
            suffix = "km"
        )

        // Subtext con último registro
        SubtextAtom(text = "Último registro: $lastKilometers")
    }
}