package com.example.shopify_app.features.products.ui
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopify_app.core.models.ConversionResponse
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.utils.priceConversion
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.core.widgets.ProductCard
import com.example.shopify_app.features.home.data.models.ProductsResponse.Product
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.ProductsResponse.Variant
import com.example.shopify_app.features.home.ui.ErrorView
import com.example.shopify_app.features.home.ui.LoadingView
import com.example.shopify_app.features.home.ui.SearchBar
import com.example.shopify_app.features.products.data.model.ProductsByIdResponse
import com.example.shopify_app.features.products.data.repo.ProductsRepo
import com.example.shopify_app.features.products.data.repo.ProductsRepoImpl
import com.example.shopify_app.features.products.viewmodel.ProductsViewModel
import com.example.shopify_app.features.products.viewmodel.productsViewModelFactory

//data class FakeProduct(val name: String, val description: String, val price: String, val imageResId: Int)

@Composable
fun UpperSection(
    navController: NavController,
    onSearchQueryChange: (String) -> Unit,
    onSliderValueChange: (Float) -> Unit,
    maxSliderValue : Float,
    minSliderValue : Float
) {
    // Initialize the slider value to the middle (500f)
    var sliderValue by remember { mutableStateOf(maxSliderValue) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // First Row: Back Button and Search Icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back Icon
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            // Search Icon
            SearchBar(onSearchQueryChange)
        }

        // Spacer for separation between the rows
        Spacer(modifier = Modifier.height(16.dp))

        // Second Row: Section Name, Slider Value as Text, and Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Section Name: "Prices"
            Text(
                text = "Prices",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            // Right Side Content: Slider Value and Slider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                // Slider Value as Text
                Text(
                    text = sliderValue.toInt().toString(), // Convert the float value to integer and then to string
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Slider
                Slider(
                    value = sliderValue,
                    onValueChange = {
                        sliderValue = it
                        onSliderValueChange(it) // Notify about slider value changes
                    },
                    valueRange = minSliderValue..maxSliderValue, // Define the range of the slider
                    modifier = Modifier.width(100.dp), // Set the width of the slider
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Black,
                        activeTrackColor = Color.Black,
                        inactiveTrackColor = Color.Gray
                    )
                )
            }
        }
    }
}


@Composable
fun ChipRow(
    items: List<String>,
    selectedItems: List<String>,
    onChipClick: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Adjust vertical padding as needed
    ) {
        items(items) { item ->
            ProductsChip(
                text = item,
                selected = selectedItems.contains(item),
                onClick = { onChipClick(item) }
            )
        }
    }
}

@Composable
fun ProductsChip(
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(
                color = if (selected) Color.Black else Color.LightGray,
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
fun ProductGridScreen(
    navController: NavController,
    repo: ProductsRepo = ProductsRepoImpl.getInstance(AppRemoteDataSourseImpl),
    collectionId: String?,
    categoryTag: String?,
    fromWhatScreen: String?,
    sharedViewModel: SettingsViewModel = viewModel()
) {
    val currency by sharedViewModel.currency.collectAsState()
    var minPrice: Float? = null
    var maxPrice: Float? = null
    val factory = productsViewModelFactory(repo)
    val viewModel: ProductsViewModel = viewModel(factory = factory)

    // State for the search query
    var searchQuery by remember { mutableStateOf("") }
    var selectedChips by remember { mutableStateOf(setOf<String>()) }
    var sliderValue by remember { mutableStateOf(maxPrice) } // State to hold the slider value

    // Fetch products when the screen is first composed
    LaunchedEffect(collectionId) {
        collectionId?.let {
            viewModel.getProductsById(it)
        }
    }

    // Observe the products state
    val products by viewModel.ProductsById.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search bar and chips row section


        // Define the available chip options (product types)
        val chipOptions = listOf("SHOES", "ACCESSORIES", "T-SHIRTS")

        // Display the chip options


        // Product grid section
        when (products) {
            is ApiState.Loading -> {
                LoadingView()
            }
            is ApiState.Failure -> {
                ErrorView((products as ApiState.Failure).error)
            }
            is ApiState.Success -> {
                val productsList = (products as ApiState.Success<ProductsByIdResponse>).data.products.orEmpty()


// Iterate through each product and their variants to find the min and max price
                for (product in productsList) {
                    if (product.variants.isNotEmpty()) {
                        val price = product.variants[0].price.toFloat()
                        if (minPrice == null || price < minPrice!!) {
                            minPrice = price
                        }
                        if (maxPrice == null || price > maxPrice!!) {
                            maxPrice = price
                        }
                    }
                }
                val finalMinPrice = minPrice ?: 0f
                val finalMaxPrice = maxPrice ?: 0f
                var priceMaxValue by rememberSaveable {
                    mutableStateOf("")
                }
                var priceMinValue by rememberSaveable {
                    mutableStateOf("")
                }
//                var convertedPrice by rememberSaveable {
//                    mutableStateOf("")
//                }
                val conversionRate by sharedViewModel.conversionRate.collectAsState()
                when(conversionRate){
                    is ApiState.Failure -> {
                        priceMaxValue = finalMaxPrice.toString()
                        priceMinValue = finalMinPrice.toString()
//                        convertedPrice = finalMaxPrice.toString()
                    }
                    ApiState.Loading -> {

                    }
                    is ApiState.Success -> {
//                        priceValue = priceConversion(lineItem.price,currency,
//                            (conversionRate as ApiState.Success<ConversionResponse>).data)
                        priceMaxValue = priceConversion(finalMaxPrice.toString(),currency,
                            (conversionRate as ApiState.Success<ConversionResponse>).data)
                        priceMinValue = priceConversion(finalMinPrice.toString(),currency,
                            (conversionRate as ApiState.Success<ConversionResponse>).data)

                    }
                }

                UpperSection(
                    navController = navController,
                    onSearchQueryChange = { query -> searchQuery = query },
                    onSliderValueChange = { value -> sliderValue = value } ,
                    maxSliderValue = priceMaxValue.toFloat(),
                    minSliderValue = priceMinValue.toFloat() // Pass the max price value to the slider,
                    // Capture the slider value changes
                )
                ChipRow(
                    items = chipOptions,
                    selectedItems = selectedChips.toList(),
                    onChipClick = { chipText ->
                        selectedChips = if (selectedChips.contains(chipText)) {
                            selectedChips - chipText
                        } else {
                            selectedChips + chipText
                        }
                    }
                )
                val filteredProducts = productsList.filter { product ->
                    // Check if the product title matches the search query
                    val matchesSearchQuery = product.title.contains(searchQuery, ignoreCase = true)

                    // Check if the product type matches any of the selected chips
                    val matchesSelectedChips = if (selectedChips.isEmpty()) {
                        true // No chips selected means we show all products
                    } else {
                        selectedChips.contains(product.product_type)
                    }

                    // Check if the product price is within the range of the slider value
                    val convertedPrice: Float? = when(conversionRate) {
                        is ApiState.Failure -> {
                            null // Handle failure case appropriately
                        }
                        ApiState.Loading -> {
                            null // Handle loading case appropriately
                        }
                        is ApiState.Success -> {
                            priceConversion(product.variants[0].price, currency, (conversionRate as ApiState.Success<ConversionResponse>).data)?.toFloatOrNull()
                        }
                    }

                    // Check if the product price is within the range of the slider value
                    val matchesSliderValue = convertedPrice?.let { price ->
                        price <= (sliderValue ?: finalMaxPrice)
                    } ?: false // If conversion fails, exclude the product


                    // Return true if all conditions are satisfied
                    matchesSearchQuery && matchesSelectedChips && matchesSliderValue
                }


                // Display the filtered products in a grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
//                    contentPadding = PaddingValues(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp),
//                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredProducts) { product ->
                        ProductCard(product = product, navController = navController , currency = currency, sharedViewmodel = sharedViewModel)
                    }
                }
            }
            else -> {
                Text(
                    text = "No products available.",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ProductGridScreenPreview() {

        // Add more products as needed

//    ProductGridScreen(fakeProducts = fakeProducts)
}