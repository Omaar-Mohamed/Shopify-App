package com.example.shopify_app.features.ProductDetails.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign

@Composable
fun Chip(
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(
                color = if (selected) Color(0xFF6200EE) else Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SingleSelectChipsGroup(
    items: List<String>,
    selectedItem: String?,
    onItemSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            Chip(
                text = item,
                selected = selectedItem == item,
                onClick = { onItemSelected(item) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SingleSelectChips() {
    var selectedItem by remember { mutableStateOf<String?>(null) }

    Column {
        Text(
            text = "Size & Color",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(bottom = 10.dp)
        )
        SingleSelectChipsGroup(
            items = listOf("OS / black", "5 / white", "6 / white"),
            selectedItem = selectedItem,
            onItemSelected = { item ->
                selectedItem = item
            }
        )
    }
}