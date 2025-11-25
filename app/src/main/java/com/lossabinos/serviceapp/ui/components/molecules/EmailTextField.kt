// presentation/ui/components/molecules/EmailTextField.kt
package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import com.lossabinos.serviceapp.ui.components.atoms.icons.AppIcons
import com.lossabinos.serviceapp.ui.components.atoms.textfields.IconTextField

/**
 * Componente Molecule - Campo de Email
 * Combina el IconTextField con configuración específica para email
 */
@Composable
fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    IconTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = "Correo electrónico",
        leadingIcon = AppIcons.Email,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        isError = isError
    )
}
