// presentation/ui/templates/HomeTemplate.kt
package com.lossabinos.serviceapp.ui.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.theme.BackgroundLight
import com.lossabinos.serviceapp.ui.theme.PrimaryBlue


/**
 * Template para la pantalla Home
 *
 * Define la estructura/layout sin lógica de datos
 *
 * Estructura:
 * ┌───────────────────────────────────┐
 * │                                    │
 * │     [HEADER - Usuario]             │
 * │                                    │
 * ├───────────────────────────────────┤
 * │     [SYNC STATUS]                  │
 * │     [UNSYNC ITEMS]                 │
 * ├───────────────────────────────────┤
 * │     [ACTION CARDS - Grid]          │
 * │  [Cám]  [Rep]  [Ub]               │
 * ├───────────────────────────────────┤
 * │     [METRICS - 2x2 Grid]           │
 * │  [Completadas] [Proceso]          │
 * │  [Pendientes]  [Eficiencia]        │
 * │                                    │
 * └───────────────────────────────────┘
 *
 * @param headerSection Composable del header (UserHeader)
 * @param syncSection Composable de sincronización (StatusSection + UnsyncSection)
 * @param actionsSection Composable de acciones (ActionCardsSection) ✅ NUEVO
 * @param metricsSection Composable de métricas (Grid 2x2)
 * @param modifier Modifier para personalización
 * @param topPadding Padding superior
 * @param sectionSpacing Espaciado entre secciones
 */
@Composable
fun HomeTemplate(
    headerSection: @Composable () -> Unit,
    syncSection: @Composable () -> Unit,
    metricsSection: @Composable () -> Unit,
    actionsSection: @Composable (() -> Unit)? = null,  // ✅ NUEVO
    serviceListSection: @Composable (() -> Unit)? = null,  // ✨ NUEVO
    modifier: Modifier = Modifier,
    topPadding: Dp = 20.dp,
    sectionSpacing: Dp = 20.dp,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // Container principal con border azul
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .border(8.dp, PrimaryBlue, RoundedCornerShape(24.dp))
                .background(BackgroundLight),
            contentPadding = PaddingValues(16.dp)
        ) {
            // ✅ ESPACIADOR en la parte superior
            item {
                Spacer(modifier = Modifier.height(topPadding))
            }

            // 1. Header del usuario
            item {
                headerSection()
            }

            // ✅ Espaciador entre secciones
            item {
                Spacer(modifier = Modifier.height(sectionSpacing))
            }

            // 2. Secciones de sincronización
            item {
                syncSection()
            }

            // ✅ Espaciador entre secciones
            item {
                Spacer(modifier = Modifier.height(sectionSpacing))
            }

            // 3. Métricas
            item {
                metricsSection()
            }

            // ✅ Espaciador al final
            item {
                Spacer(modifier = Modifier.height(sectionSpacing))
            }

            // 4. Tarjetas de acción (ActionCards) ✅ NUEVO
            if (actionsSection != null) {
                item {
                    actionsSection()
                }

                // ✅ Espaciador entre secciones
                item {
                    Spacer(modifier = Modifier.height(sectionSpacing))
                }
            }

            // 5. Lista de servicios ✨ NUEVO - OPCIONAL
            if (serviceListSection != null) {
                item {
                    serviceListSection()
                }

                // ✅ Espaciador al final
                item {
                    Spacer(modifier = Modifier.height(sectionSpacing))
                }
            }

        }
    }
}
