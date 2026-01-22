package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.util.Locale
import java.util.Date
import android.icu.text.SimpleDateFormat
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@Composable
fun NotificationDateAtom(
    timestamp: Long,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("dd MMM HH:mm", Locale("es", "MX"))
    val formattedDate = dateFormat.format(Date(timestamp))

    Text(
        text = formattedDate,
        fontSize = 12.sp,
        color = Color(0xFF9E9E9E),
        fontWeight = FontWeight.Normal,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun NotificationDateAtomPreview() {
    MaterialTheme {
        NotificationDateAtom(
            timestamp = System.currentTimeMillis()
        )
    }
}
