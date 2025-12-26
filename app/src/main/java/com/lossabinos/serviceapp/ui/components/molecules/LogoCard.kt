// presentation/ui/components/molecules/LogoCard.kt
package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.R
import com.lossabinos.serviceapp.ui.theme.ExtraLargeCorner
import com.lossabinos.serviceapp.ui.theme.GrayIconLight

/**
 * Componente Molecule - Tarjeta de Logo
 * Icono de cohete en un cuadrado redondeado
 */
@Composable
fun LogoCard(
    modifier: Modifier = Modifier,
    size: Int = 120,
    backgroundColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .background(backgroundColor, ExtraLargeCorner),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Los Sabinos",
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp), // 👈 ajusta este valor
            contentScale = ContentScale.Fit
        )
    }
}



@Preview(showBackground = true)
@Composable
fun LogCardPreview(){
    MaterialTheme{
        LogoCard(

        )
    }
}