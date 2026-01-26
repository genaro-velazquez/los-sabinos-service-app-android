package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

// ═══════════════════════════════════════════════════════
// EXTRA COST CARD MOLECULE
// ═══════════════════════════════════════════════════════
@Composable
fun ExtraCostCard(
    extraCost: ExtraCostUIModel,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ═══════════════════════════════════════════════════════
            // LEFT SIDE: Icon + Description
            // ═══════════════════════════════════════════════════════
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                // Icon + Category name in same line
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = extraCost.category.icon,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = extraCost.category.displayName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF666666)
                    )
                }

                // Description
                Text(
                    text = extraCost.description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // ═══════════════════════════════════════════════════════
            // RIGHT SIDE: Price + Action Buttons
            // ═══════════════════════════════════════════════════════
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(start = 12.dp)
            ) {
                // Price
                Text(
                    text = extraCost.getFormattedQuantity(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Action buttons
                Row {
                    // Edit button
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier
                            .background(
                                color = Color(0xFFE3F2FD),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit cost",
                            tint = Color(0xFF1976D2),
                            modifier = Modifier.padding(4.dp)
                        )
                    }

                    // Delete button
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .background(
                                color = Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete cost",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════
// PREVIEW - Single cost
// ═══════════════════════════════════════════════════════
@Preview(
    showBackground = true,
    backgroundColor = 0xFFF5F5F5,
    widthDp = 360
)
@Composable
fun PreviewExtraCostCard() {
    val mockCost = ExtraCostUIModel(
        quantity = 45.50,
        category = ExtraCostCategory.SPARE_PARTS,
        description = "Synthetic oil 5W-30"
    )

    ExtraCostCard(
        extraCost = mockCost,
        onEditClick = {},
        onDeleteClick = {},
        modifier = Modifier.padding(16.dp)
    )
}

// ═══════════════════════════════════════════════════════
// PREVIEW - Multiple costs
// ═══════════════════════════════════════════════════════
@Preview(
    showBackground = true,
    backgroundColor = 0xFFF5F5F5,
    widthDp = 360
)
@Composable
fun PreviewExtraCostCardMultiple() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ExtraCostCard(
            extraCost = ExtraCostUIModel(
                quantity = 45.50,
                category = ExtraCostCategory.SPARE_PARTS,
                description = "Synthetic oil 5W-30"
            ),
            onEditClick = {},
            onDeleteClick = {},
            modifier = Modifier.padding(bottom = 12.dp)
        )

        ExtraCostCard(
            extraCost = ExtraCostUIModel(
                quantity = 20.00,
                category = ExtraCostCategory.LABOR,
                description = "Oil and filter change"
            ),
            onEditClick = {},
            onDeleteClick = {},
            modifier = Modifier.padding(bottom = 12.dp)
        )

        ExtraCostCard(
            extraCost = ExtraCostUIModel(
                quantity = 15.75,
                category = ExtraCostCategory.TRANSPORTATION,
                description = "Delivery to workshop"
            ),
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}
