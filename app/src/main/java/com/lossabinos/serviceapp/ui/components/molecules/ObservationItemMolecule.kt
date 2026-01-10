package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.ui.components.atoms.ObservationCheckboxAtom
import com.lossabinos.serviceapp.ui.components.atoms.ObservationNumberInputAtom

@Composable
fun ObservationItemMolecule(
    id: String,
    description: String,
    responseType: String,
    value: String,
    onValueChange: (String) -> Unit,
    requiresResponse: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Card(
            modifier = modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(
                width = 1.dp,
                color = Color(0xFFE0E0E0)
            )
        ){
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                // Título de la observación
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )

                    if (requiresResponse) {
                        Text(
                            text = "*Requerido",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFC62828),
                            fontSize = 10.sp
                        )
                    }
                }

                // Renderizar según tipo
                when (responseType.lowercase()) {
                    "textarea" -> {
                        ObservationInputMolecule(
                            value = value,
                            onValueChange = onValueChange,
                            maxCharacters = 500
                        )
                    }

                    "number" -> {
                        ObservationNumberInputAtom(
                            value = value,
                            onValueChange = onValueChange,
                            placeholder = "Ingresa un número"
                        )
                    }

                    "boolean" -> {
                        ObservationCheckboxAtom(
                            label = "Completado / Confirmado",
                            checked = value.toBoolean(),
                            onCheckedChange = { checked ->
                                onValueChange(checked.toString())
                            }
                        )
                    }

                    else -> {
                        Text(
                            text = "Tipo de respuesta no soportado: $responseType",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }


        /*
                // Título de la observación
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )

                    if (requiresResponse) {
                        Text(
                            text = "*Requerido",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFC62828),
                            fontSize = 10.sp
                        )
                    }
                }

                // Renderizar según tipo
                when (responseType.lowercase()) {
                    "textarea" -> {
                        ObservationInputMolecule(
                            value = value,
                            onValueChange = onValueChange,
                            maxCharacters = 500
                        )
                    }

                    "number" -> {
                        ObservationNumberInputAtom(
                            value = value,
                            onValueChange = onValueChange,
                            placeholder = "Ingresa un número"
                        )
                    }

                    "boolean" -> {
                        ObservationCheckboxAtom(
                            label = "Completado / Confirmado",
                            checked = value.toBoolean(),
                            onCheckedChange = { checked ->
                                onValueChange(checked.toString())
                            }
                        )
                    }

                    else -> {
                        Text(
                            text = "Tipo de respuesta no soportado: $responseType",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

         */
    }
}

