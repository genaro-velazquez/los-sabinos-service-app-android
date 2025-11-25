// presentation/ui/components/templates/LoginTemplate.kt
package com.lossabinos.serviceapp.ui.components.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.theme.BackgroundLight
import com.lossabinos.serviceapp.ui.components.molecules.ErrorSnackbar

/**
 * Template de Login
 *
 * Una TEMPLATE define la estructura/layout de la página sin contenido específico.
 * Es una estructura reutilizable que recibe slots (composables) como parámetros.
 *
 * Flujo de contenido:
 * 1. Box principal (fondo)
 *    ├── Column con scroll (contenido)
 *    │   ├── headerContent (opcional - ícono, título, etc)
 *    │   ├── formContent (obligatorio - formulario)
 *    │   └── footerContent (opcional - links, etc)
 *    ├── loadingOverlay (opcional - spinner)
 *    └── snackbarContent (opcional - alertas/errores)
 */
@Composable
fun LoginTemplate(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    headerContent: (@Composable () -> Unit)? = null,
    formContent: @Composable () -> Unit = {},
    footerContent: (@Composable () -> Unit)? = null,
    loadingOverlay: (@Composable () -> Unit)? = null,
    snackbarContent: (@Composable BoxScope.() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // ====================== CONTENIDO PRINCIPAL ======================
        // Column con scroll para que el contenido sea scrolleable

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                // ✅ Header (opcional) - Ícono, título, etc
                headerContent?.let {
                    it()
                }

                // ✅ Formulario/Contenido principal (obligatorio)
                formContent()

                // ✅ Footer (opcional) - Links de "Olvide mi contraseña", registrarse, etc
                footerContent?.let {
                    it()
                }
            }
        )

        // ====================== OVERLAY DE CARGA ======================
        // Se muestra ENCIMA del contenido (spinner amarillo)

        loadingOverlay?.let {
            it()
        }

        // ====================== SNACKBAR (ALERTAS/ERRORES) ======================
        // Se muestra en la PARTE INFERIOR (rojo para errores)

        if (snackbarContent != null) {
            // Si se pasa un snackbarContent personalizado, usarlo
            snackbarContent()
        } else {
            // Si NO se pasa, mostrar SnackbarHost con estilos por defecto
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 30.dp)
                    .align(Alignment.TopCenter)
            ) { data ->
                ErrorSnackbar(snackbarData = data)  // ← AQUÍ USAS EL MOLECULE
            }
        }
    }
}

/**
 * EXPLICACIÓN:
 *
 * Este Template ahora tiene DOS opciones:
 *
 * OPCIÓN 1: Usar el Snackbar por defecto (rojo)
 * ```
 * LoginTemplate(
 *     formContent = { LoginForm(...) }
 *     // No pasas snackbarContent → usa el rojo por defecto
 * )
 * ```
 *
 * OPCIÓN 2: Pasar un Snackbar personalizado
 * ```
 * LoginTemplate(
 *     formContent = { LoginForm(...) },
 *     snackbarContent = {
 *         SnackbarHost(snackbarHostState) { ... custom design ... }
 *     }
 * )
 * ```
 *
 * ESTRUCTURA VISUAL:
 *
 * Box (fondo beige)
 * ├── Column + scroll (contenido scrolleable)
 * │   ├── [Ícono]
 * │   ├── [Título]
 * │   ├── [Email]
 * │   ├── [Contraseña]
 * │   └── [Botón]
 * ├── [Spinner amarillo] (si está loading)
 * └── [Snackbar rojo] (si hay error)
 */