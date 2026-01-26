package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.models.ui.ExtraCostCategory

// ═══════════════════════════════════════════════════════
// CATEGORY DROPDOWN ATOM
// ═══════════════════════════════════════════════════════
@Composable
fun CategoryDropdown(
    selectedCategory: ExtraCostCategory,
    onCategorySelected: (ExtraCostCategory) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        // Button that opens dropdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = if (isError) Color(0xFFD32F2F) else Color(0xFFBBBBBB),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                )
                .background(Color.White)
                .clickable { expandedMenu = true }
                .padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "▼ ${selectedCategory.icon}  ${selectedCategory.displayName}",
                fontSize = 16.sp,
                color = Color.Black
            )
        }

        // Dropdown Menu
        DropdownMenu(
            expanded = expandedMenu,
            onDismissRequest = { expandedMenu = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.White)
        ) {
            ExtraCostCategory.values().forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "${category.icon}  ${category.displayName}",
                            fontSize = 16.sp
                        )
                    },
                    onClick = {
                        onCategorySelected(category)
                        expandedMenu = false
                    }
                )
            }
        }
    }
}
