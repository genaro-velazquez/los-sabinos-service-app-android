package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.models.ui.UrgencyUI

@Composable
fun UrgencySelector(
    selected: UrgencyUI,
    onChange: (UrgencyUI) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = UrgencyUI.values().toList()

    Column(modifier = modifier) {
        options.forEach { urgency ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onChange(urgency) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = urgency == selected,
                    onClick = { onChange(urgency) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = urgency.name.lowercase().replaceFirstChar { it.uppercase() })
            }
        }
    }
}
