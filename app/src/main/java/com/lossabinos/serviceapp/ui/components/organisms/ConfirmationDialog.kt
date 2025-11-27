// presentation/ui/components/organisms/ConfirmationDialog.kt
package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lossabinos.serviceapp.ui.components.atoms.ModalContent
import com.lossabinos.serviceapp.ui.components.atoms.ModalTitle
import com.lossabinos.serviceapp.ui.components.molecules.ModalButtonGroup
import com.lossabinos.serviceapp.ui.theme.CardBackground
import com.lossabinos.serviceapp.ui.theme.LosabosTheme

/**
 * Dialog de confirmación
 *
 * Muestra un modal con:
 * - Título
 * - Contenido/descripción
 * - Botones (Primario + Secundario)
 *
 * @param title Título del modal
 * @param content Contenido/descripción
 * @param primaryButtonText Texto del botón primario
 * @param secondaryButtonText Texto del botón secundario
 * @param onPrimaryClick Callback cuando presiona botón primario
 * @param onSecondaryClick Callback cuando presiona botón secundario
 * @param onDismiss Callback cuando se cierra el dialog
 * @param modifier Modifier para personalización
 */
@Composable
fun ConfirmationDialog(
    title: String = "Confirmar Acción",
    content: String = "¿Estás seguro de que deseas continuar? Esta acción no se puede deshacer.",
    primaryButtonText: String = "Confirmar",
    secondaryButtonText: String = "Cancelar",
    onPrimaryClick: () -> Unit = {},
    onSecondaryClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
    horizontalPadding: androidx.compose.ui.unit.Dp = 30.dp, // Ando del dialog
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        // ✅ Wrapper Box con padding horizontal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding),
            contentAlignment = Alignment.Center
        ) {
            // Card del modal
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(CardBackground, RoundedCornerShape(24.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Título
                    ModalTitle(text = title)

                    // Contenido
                    ModalContent(text = content)

                    // Espaciador
                    Spacer(modifier = Modifier.height(8.dp))

                    // Botones
                    ModalButtonGroup(
                        primaryText = primaryButtonText,
                        secondaryText = secondaryButtonText,
                        onPrimaryClick = {
                            onPrimaryClick()
                            onDismiss()
                        },
                        onSecondaryClick = {
                            onSecondaryClick()
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun dialogPreview(){
    LosabosTheme {
        ConfirmationDialog()
    }
}
