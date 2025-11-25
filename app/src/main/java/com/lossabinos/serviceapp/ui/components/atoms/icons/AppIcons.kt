// presentation/ui/components/atoms/icons/AppIcons.kt
package com.lossabinos.serviceapp.ui.components.atoms.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock



/**
 * Componente Atom - Iconos de la aplicación
 * Centraliza todos los iconos usados en la app
 */
object AppIcons {
    // Iconos de autenticación
    val Email = Icons.Filled.Email
    val EmailOutlined = Icons.Outlined.Email
    
    val Lock = Icons.Filled.Lock
    val LockOutlined = Icons.Outlined.Lock
    
    // Iconos de visibilidad
    val Visibility = Icons.Filled.Visibility
    val VisibilityOff = Icons.Filled.VisibilityOff
}
