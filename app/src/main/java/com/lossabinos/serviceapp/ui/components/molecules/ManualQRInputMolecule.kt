package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ManualQRInputMolecule(
    qrValue: String,
    onValueChange: (String) -> Unit,
    onValidateClick: () -> Unit,
    isValidating: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Etiqueta
        Text(
            text = "Ingresa los últimos 6 digitos del NIV",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF424242),
            fontSize = 12.sp
        )

        // TextField
        TextField(
            value = qrValue,
            onValueChange = { newValue ->
                // 1️⃣ Solo letras y números
                val filtered = newValue
                    .uppercase()
                    .filter { it.isLetterOrDigit() }
                    .take(6)

                onValueChange(filtered)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = {
                Text(
                    text = "Ingresa el código QR aquí",
                    color = Color(0xFFBDBDBD),
                    fontSize = 14.sp
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF1976D2)
            )
        )

        // Botón Validar
        Button(
            onClick = onValidateClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = qrValue.length == 6 && !isValidating,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2),
                disabledContainerColor = Color(0xFFBDBDBD)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (isValidating) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Text(
                        "Validando...",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            } else {
                Text(
                    "Validar Código",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManualQRInputMoleculePreview(){
    MaterialTheme{
        ManualQRInputMolecule(
            qrValue = "124",
            onValueChange = {},
            onValidateClick = {},
            isValidating = false
        )
    }
}