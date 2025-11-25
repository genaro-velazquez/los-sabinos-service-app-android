// presentation/ui/components/molecules/PasswordTextField.kt
package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import com.lossabinos.serviceapp.ui.components.atoms.icons.AppIcons
import com.lossabinos.serviceapp.ui.components.atoms.textfields.IconTextField
import com.lossabinos.serviceapp.ui.theme.GrayIcon

/**
 * Componente Molecule - Campo de Contraseña
 * Combina el IconTextField con toggle de visibilidad
 */

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    val passwordVisible = remember { mutableStateOf(false) }

    IconTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = "Contraseña",
        leadingIcon = AppIcons.Lock,
        modifier = modifier,

        // ✅ AGREGAR ESTO - Cambiar visualTransformation según visibilidad
        visualTransformation = if (passwordVisible.value) {
            VisualTransformation.None  // ← Muestra: MiPassword123
        } else {
            PasswordVisualTransformation()  // ← Muestra: •••••••••
        },

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            Icon(
                imageVector = if (passwordVisible.value) {
                    AppIcons.Visibility
                } else {
                    AppIcons.VisibilityOff
                },
                contentDescription = if (passwordVisible.value) "Ocultar" else "Mostrar",
                tint = GrayIcon,
                modifier = Modifier.clickable {
                    passwordVisible.value = !passwordVisible.value
                }
            )
        },
        isError = isError
    )
}
