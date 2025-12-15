package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.atoms.CharacterCounterAtom
import com.lossabinos.serviceapp.ui.components.atoms.ObservationTextInputAtom

@Composable
fun ObservationInputMolecule(
    value: String,
    onValueChange: (String) -> Unit,
    maxCharacters: Int = 300,
    modifier: Modifier = Modifier
) {
    val currentCount = value.length

    // Validar que no exceda el máximo
    val finalValue = if (currentCount > maxCharacters) {
        value.substring(0, maxCharacters)
    } else {
        value
    }

    // Actualizar si fue truncado
    if (finalValue != value) {
        onValueChange(finalValue)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1️⃣ Input Field
        ObservationTextInputAtom(
            value = finalValue,
            onValueChange = { newValue ->
                // Solo actualizar si no excede el máximo
                if (newValue.length <= maxCharacters) {
                    onValueChange(newValue)
                }
            }
        )

        // 2️⃣ Counter (alineado a la derecha)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            CharacterCounterAtom(
                currentCount = finalValue.length,
                maxCount = maxCharacters
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ObservationInputMoleculePreview(){
    MaterialTheme{
        ObservationInputMolecule(
            value = "value.....",
            onValueChange = { _ ->}
        )
    }
}