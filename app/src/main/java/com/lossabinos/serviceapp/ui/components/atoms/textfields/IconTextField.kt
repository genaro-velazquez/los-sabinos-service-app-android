// presentation/ui/components/atoms/textfields/IconTextField.kt
package com.lossabinos.serviceapp.ui.components.atoms.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.theme.BackgroundWhite
import com.lossabinos.serviceapp.ui.theme.GrayIcon
import com.lossabinos.serviceapp.ui.theme.MediumCorner
import com.lossabinos.serviceapp.ui.theme.TextGrayLight

/**
 * Componente Atom - Campo de texto con icono
 * Used para campos como email y contraseña
 */
@Composable
fun IconTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .padding(horizontal = 16.dp),
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions(),
    isError: Boolean = false,
    backgroundColor: Color = BackgroundWhite,
    borderColor: Color = Color.Transparent,
    iconColor: Color = GrayIcon,
    visualTransformation: VisualTransformation = VisualTransformation.None  // ✅ AGREGAR ESTO
) {
    Row(
        modifier = modifier
            .background(backgroundColor, MediumCorner)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.padding(end = 12.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            visualTransformation = visualTransformation,  // ✅ AGREGAR ESTO
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = TextGrayLight,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                innerTextField()
            }
        )

        trailingIcon?.let {
            it()
        }
    }
}