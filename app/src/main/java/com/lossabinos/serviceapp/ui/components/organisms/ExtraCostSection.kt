package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.models.ui.ExtraCostCategory
import com.lossabinos.serviceapp.models.ui.ExtraCostUIModel
import com.lossabinos.serviceapp.ui.components.molecules.ExtraCostCard

// ═══════════════════════════════════════════════════════
// EXTRA COST SECTION ORGANISM
// ═══════════════════════════════════════════════════════
@Composable
fun ExtraCostSection(
    extraCosts: List<ExtraCostUIModel>,
    onAddCostClick: () -> Unit,
    onEditCostClick: (ExtraCostUIModel) -> Unit,
    onDeleteCostClick: (ExtraCostUIModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalCosts = extraCosts.sumOf { it.quantity }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        // ═══════════════════════════════════════════════════════
        // HEADER
        // ═══════════════════════════════════════════════════════
        Text(
            text = "💰 COSTOS EXTRA",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ═══════════════════════════════════════════════════════
        // ADD BUTTON
        // ═══════════════════════════════════════════════════════
        Button(
            onClick = onAddCostClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add cost",
                tint = Color.White,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Agregar Costo Extra",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // ═══════════════════════════════════════════════════════
        // COST LIST
        // ═══════════════════════════════════════════════════════
        if (extraCosts.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                extraCosts.forEachIndexed { index, cost ->
                    ExtraCostCard(
                        extraCost = cost,
                        onEditClick = { onEditCostClick(cost) },
                        onDeleteClick = { onDeleteCostClick(cost) },
                        modifier = if (index < extraCosts.size - 1) {
                            Modifier.padding(bottom = 12.dp)
                        } else {
                            Modifier
                        }
                    )
                }
            }
/*
            // ═══════════════════════════════════════════════════════
            // TOTAL SECTION
            // ═══════════════════════════════════════════════════════
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = "TOTAL EXTRA COSTS:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF666666),
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                Text(
                    text = String.format("$%.2f", totalCosts),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
 */
        } else {
            // ═══════════════════════════════════════════════════════
            // EMPTY STATE
            // ═══════════════════════════════════════════════════════
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "(Aún no se han agregado costos extra)",
                    fontSize = 14.sp,
                    color = Color(0xFFAAAAAA)
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════
// PREVIEW - EMPTY STATE
// ═══════════════════════════════════════════════════════
@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    widthDp = 360
)
@Composable
fun PreviewExtraCostSectionEmpty() {
    ExtraCostSection(
        extraCosts = emptyList(),
        onAddCostClick = {},
        onEditCostClick = {},
        onDeleteCostClick = {},
        modifier = Modifier.padding(16.dp)
    )
}

// ═══════════════════════════════════════════════════════
// PREVIEW - WITH COSTS
// ═══════════════════════════════════════════════════════
@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    widthDp = 360
)
@Composable
fun PreviewExtraCostSectionWithCosts() {
    val mockCosts = listOf(
        ExtraCostUIModel(
            quantity = 45.50,
            category = ExtraCostCategory.SPARE_PARTS,
            description = "Synthetic oil 5W-30"
        ),
        ExtraCostUIModel(
            quantity = 20.00,
            category = ExtraCostCategory.LABOR,
            description = "Oil and filter change"
        ),
        ExtraCostUIModel(
            quantity = 15.75,
            category = ExtraCostCategory.TRANSPORTATION,
            description = "Delivery to workshop"
        )
    )

    ExtraCostSection(
        extraCosts = mockCosts,
        onAddCostClick = {},
        onEditCostClick = {},
        onDeleteCostClick = {},
        modifier = Modifier.padding(16.dp)
    )
}
