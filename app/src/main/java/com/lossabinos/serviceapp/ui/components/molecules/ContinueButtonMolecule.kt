package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.atoms.ArrowRightIcon
import com.lossabinos.serviceapp.ui.components.atoms.ContinueButtonAtom

@Composable
fun ContinueButtonMolecule(
    text: String = "Continuar",
    onClick: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    ContinueButtonAtom(
        text = text,
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier,
        icon = {
            if (isLoading) {
                // Mostrar spinner si está cargando
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.Black,
                    strokeWidth = 2.dp
                )
            } else {
                // Mostrar flecha
                ArrowRightIcon(
                    size = 24.dp,
                    color = Color.Black
                )
            }
        }
    )
}