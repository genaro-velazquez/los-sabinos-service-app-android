// presentation/ui/components/molecules/ModalButtonGroup.kt
package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.atoms.buttons.PrimaryButton
import com.lossabinos.serviceapp.ui.components.atoms.SecondaryButton

/**
 * Grupo de botones del modal (Primario + Secundario)
 *
 * @param primaryText Texto del botón primario
 * @param secondaryText Texto del botón secundario
 * @param onPrimaryClick Callback para botón primario
 * @param onSecondaryClick Callback para botón secundario
 * @param modifier Modifier para personalización
 */
@Composable
fun ModalButtonGroup(
    primaryText: String = "Eliminar",
    secondaryText: String = "Cancelar",
    onPrimaryClick: () -> Unit = {},
    onSecondaryClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Botón primario
        PrimaryButton(
            text = primaryText,
            onClick = onPrimaryClick
        )
        
        // Espaciador
        Spacer(modifier = Modifier.height(0.dp))
        
        // Botón secundario
        SecondaryButton(
            text = secondaryText,
            onClick = onSecondaryClick
        )
    }
}
