package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.atoms.ErrorIconAtom
import com.lossabinos.serviceapp.ui.components.atoms.ErrorMessageAtom
import com.lossabinos.serviceapp.ui.components.atoms.ErrorTitleAtom

@Composable
fun ErrorCardMolecule(
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFFF3E0),  // Fondo naranja claro
                shape = RoundedCornerShape(12.dp)
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Icono
        ErrorIconAtom()

        // Título
        ErrorTitleAtom(title = title)

        // Mensaje
        ErrorMessageAtom(message = message)
    }
}
