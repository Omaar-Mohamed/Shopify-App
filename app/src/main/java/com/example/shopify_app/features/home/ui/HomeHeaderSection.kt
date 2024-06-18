package com.example.shopify_app.features.home.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.shopify_app.R
import com.example.shopify_app.core.datastore.StoreCustomerEmail
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.home.data.models.LoginCustomer.LoginCustomer
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeTopSection(customerState: ApiState<LoginCustomer>, navController: NavHostController,onSearchQueryChange: (String) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreCustomerEmail(context)

    when (customerState) {
        is ApiState.Loading -> {
            LoadingView()
        }

        is ApiState.Failure -> {
            ErrorView(customerState.error)
        }

        is ApiState.Success<LoginCustomer> -> {
            val customer = customerState.data.customers
            val name : String?
            if(customer.isEmpty()){
                name = "Guest"
            }else{
                name = customer[0].first_name
                scope.launch {
                    dataStore.setCustomerId(customer[0].id)
                    dataStore.setFavoriteId(customer[0].note.toString())
                    dataStore.setOrderId(customer[0].multipass_identifier.toString())
                }
            }

            //Log.i("customer", "HomeTopSection: ${customer[0].first_name}")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigate("category") }, // Navigate to CategoryScreen
                        modifier = Modifier.size(40.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.Black,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                            )
                        }
                    }
                    Image(
                        painter = painterResource(id = R.drawable.img),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Welcome, $name",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )
                Text(
                    text = "Our Fashions App",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SearchBar(onSearchQueryChange = onSearchQueryChange)
                    IconButton(onClick = { /* TODO: Handle click */ }) {
                        Surface(
                            shape = CircleShape,
                            color = Color.Black,
                            modifier = Modifier.size(40.dp)
                        ) {
                            // Replace R.drawable.your_image with the image resource from your drawable folder
                            Image(
                                painter = painterResource(id = R.drawable.filtter),
                                contentDescription = "Menu",
                                contentScale = ContentScale.Fit, // Adjust the content scale as needed
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun SearchBar(onSearchQueryChange: (String) -> Unit) {
    var searchText by remember { mutableStateOf("") }

    Surface(
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFFF0F0F0),
        modifier = Modifier
            .fillMaxWidth(0.85f)
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


@Preview
@Composable
fun HomeTopSectionPreview() {
//    HomeTopSection()
}