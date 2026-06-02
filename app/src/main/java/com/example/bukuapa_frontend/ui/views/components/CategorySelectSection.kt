package com.example.bukuapa_frontend.ui.views.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bukuapa_frontend.data.models.Category

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySelectSection(
    categories: List<Category>,
    selectedCategoryIds: List<Int>,
    onCategoryToggle: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Kategori",
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            categories.forEach { category ->
                val isSelected = selectedCategoryIds.contains(category.id)
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategoryToggle(category.id) },
                    label = {
                        Text(
                            text = category.name,
                            fontSize = 12.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF007AFF),
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = Color.Black
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = Color.LightGray,
                        selectedBorderColor = Color(0xFF007AFF),
                        borderWidth = 1.dp,
                        selectedBorderWidth = 1.dp,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }
    }
}