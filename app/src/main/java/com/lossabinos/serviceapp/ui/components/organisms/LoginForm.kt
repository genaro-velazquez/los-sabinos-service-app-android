// presentation/ui/components/organisms/LoginForm.kt
package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.atoms.buttons.PrimaryButton
import com.lossabinos.serviceapp.ui.components.atoms.spacers.SpacerLarge
import com.lossabinos.serviceapp.ui.components.atoms.spacers.SpacerMedium
import com.lossabinos.serviceapp.ui.components.atoms.text.HeadlineText
import com.lossabinos.serviceapp.ui.components.molecules.EmailTextField
import com.lossabinos.serviceapp.ui.components.molecules.ForgotPasswordLink
import com.lossabinos.serviceapp.ui.components.molecules.LogoCard
import com.lossabinos.serviceapp.ui.components.molecules.PasswordTextField
import com.lossabinos.serviceapp.ui.theme.PrimaryYellow

/**
 * Componente Organism - Formulario de Login
 * Combina todas las moléculas y átomos para crear el formulario completo
 */
@Composable
fun LoginForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isError: Boolean = false
) {

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // espacio top
        Spacer(modifier = Modifier.height(48.dp))

        // Logo
        LogoCard(
            size = 200,
            modifier = Modifier //Modifier.padding(top = 32.dp, bottom = 40.dp)
        )

        // Título
        HeadlineText(
            text = "",
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // Email Field
        EmailTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocusRequester),
                //.padding(horizontal = 16.dp),
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next  // ← SIGUIENTE
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    passwordFocusRequester.requestFocus()  // ← SALTAR A PASSWORD
                }
            )
        )

        SpacerMedium()

        // Password Field
        PasswordTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester),
                //.padding(horizontal = 16.dp),
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done  // ← HECHO/ACEPTAR
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onLoginClick()  // ← HACER LOGIN AL PRESIONAR DONE
                }
            )
        )

        SpacerMedium()

        // Forgot Password Link
        ForgotPasswordLink(
            onClick = onForgotPasswordClick,
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 16.dp),
            color = PrimaryYellow
        )

        SpacerLarge()

        // Login Button
        PrimaryButton(
            text = "Login",
            onClick = onLoginClick,
            enabled = !isLoading,
            isLoading = isLoading,
            modifier = Modifier
                .fillMaxWidth()
                //.padding(horizontal = 16.dp)
        )
    }
}
