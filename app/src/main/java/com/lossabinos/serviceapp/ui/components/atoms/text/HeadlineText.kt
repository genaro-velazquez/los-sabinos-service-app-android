// presentation/ui/components/atoms/texts/HeadlineText.kt
package com.lossabinos.serviceapp.ui.components.atoms.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.lossabinos.serviceapp.ui.theme.AppTypography
import com.lossabinos.serviceapp.ui.theme.TextBlack

/**
 * Componente Atom - Texto de titular
 * Usado para títulos grandes como "Inicia Sesión"
 */
@Composable
fun HeadlineText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextBlack,
    style: TextStyle = AppTypography.headlineLarge,
    textAlign: TextAlign = TextAlign.Center,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

/**
 * Variante mediana
 */
@Composable
fun HeadlineMediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextBlack,
    textAlign: TextAlign = TextAlign.Center
) {
    HeadlineText(
        text = text,
        modifier = modifier,
        color = color,
        style = AppTypography.headlineMedium,
        textAlign = textAlign
    )
}

/**
 * Variante pequeña
 */
@Composable
fun HeadlineSmallText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextBlack,
    textAlign: TextAlign = TextAlign.Center
) {
    HeadlineText(
        text = text,
        modifier = modifier,
        color = color,
        style = AppTypography.headlineSmall,
        textAlign = textAlign
    )
}
