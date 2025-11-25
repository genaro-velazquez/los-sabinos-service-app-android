// presentation/ui/components/atoms/spacers/Spacers.kt
package com.lossabinos.serviceapp.ui.components.atoms.spacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Componente Atom - Espaciadores
 * Componentes para manejar espacios consistentes
 */

// Espacios verticales
@Composable
fun SpacerSmall() = Spacer(modifier = Modifier.height(8.dp))

@Composable
fun SpacerMedium() = Spacer(modifier = Modifier.height(16.dp))

@Composable
fun SpacerLarge() = Spacer(modifier = Modifier.height(24.dp))

@Composable
fun SpacerExtraLarge() = Spacer(modifier = Modifier.height(32.dp))

// Espacios horizontales
@Composable
fun SpacerHorizontalSmall() = Spacer(modifier = Modifier.width(8.dp))

@Composable
fun SpacerHorizontalMedium() = Spacer(modifier = Modifier.width(16.dp))

@Composable
fun SpacerHorizontalLarge() = Spacer(modifier = Modifier.width(24.dp))
