package com.example.shopify_app.features.wishList.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.shopify_app.R

@Composable
fun SearchBar(onSearchQueryChange: (String) -> Unit){
    var searchText by remember { mutableStateOf("") }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color(0xFFF0F0F0),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        onSearchQueryChange(it) // Update the parent composable with the new search query
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (searchText.isEmpty()) {
                            Text(text = "Search...", color = Color.Gray)
                        }
                        innerTextField()
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}