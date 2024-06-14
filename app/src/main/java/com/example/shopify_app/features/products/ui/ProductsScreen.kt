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
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.core.widgets.ProductCard
import com.example.shopify_app.features.home.data.models.ProductsResponse.Image
import com.example.shopify_app.features.home.data.models.ProductsResponse.Option
import com.example.shopify_app.features.home.data.models.ProductsResponse.Product
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.ProductsResponse.Variant
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
    onSliderValueChange: (Float) -> Unit
) {
    // Initialize the slider value to the middle (500f)
    var sliderValue by remember { mutableStateOf(500f) }

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
                    valueRange = 0f..1000f, // Define the range of the slider
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
    val factory = productsViewModelFactory(repo)
    val viewModel: ProductsViewModel = viewModel(factory = factory)

    // State for the search query
    var searchQuery by remember { mutableStateOf("") }
    var selectedChips by remember { mutableStateOf(setOf<String>()) }
    var sliderValue by remember { mutableStateOf(500f) } // State to hold the slider value

    // Fetch products when the screen is first composed
    LaunchedEffect(collectionId) {
        collectionId?.let {
            viewModel.getProducts()
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
        UpperSection(
            navController = navController,
            onSearchQueryChange = { query -> searchQuery = query },
            onSliderValueChange = { value -> sliderValue = value } // Capture the slider value changes
        )

        // Define the available chip options (product types)
        val chipOptions = listOf("SHOES", "ACCESSORIES", "T-SHIRTS")

        // Display the chip options
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

        // Product grid section
        when (products) {
            is ApiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is ApiState.Failure -> {
                val error = (products as ApiState.Failure).error
                Log.i("getProductsById", "ProductGridScreen: $error")
                Text(
                    text = "Failed to load products: $error",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is ApiState.Success -> {
                val productsList = (products as ApiState.Success<ProductsResponse>).data.products.orEmpty()

                // Apply filtering with the slider value as an additional condition
                val filteredProducts = productsList.filter { product ->
                    // Additional filtering based on fromWhatScreen and categoryTag
                    val matchesCategoryTag = if (fromWhatScreen == "brands") {
                        categoryTag?.let { tag -> product.vendor.contains(tag, ignoreCase = true) } ?: true
                    } else {
                        categoryTag?.let { tag -> product.tags.orEmpty().contains(tag, ignoreCase = true) } ?: true
                    }

                    // Proceed with other checks if matchesCategoryTag is true
                    if (matchesCategoryTag) {
                        // Check if the product title matches the search query
                        val matchesSearchQuery = product.title.contains(searchQuery, ignoreCase = true)

                        // Check if the product type matches any of the selected chips
                        val matchesSelectedChips = if (selectedChips.isEmpty()) {
                            true // No chips selected means we show all products
                        } else {
                            selectedChips.contains(product.product_type)
                        }

                        // Check if the product price is within the range of the slider value
                        val matchesSliderValue = product.variants[0].price.toFloatOrNull()?.let { price ->
                            price <= sliderValue
                        } ?: false // If conversion fails, exclude the product
                        // Return true if all conditions are satisfied
                        matchesSearchQuery && matchesSelectedChips && matchesSliderValue
                    } else {
                        // If matchesCategoryTag is false, exclude the product
                        false
                    }
                }

                // Display the filtered products in a grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredProducts) { product ->
                        ProductCard(product = product, navController = navController)
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
    val fakeProducts = listOf(
//        FakeProduct("The Marc Jacobs", "Traveler Tote", "$195.00", R.drawable.img),
//        FakeProduct("Another Product", "Description", "$99.00", R.drawable.img),
//        FakeProduct("Third Product", "Description", "$250.00", R.drawable.img),
      Product(
            admin_graphql_api_id = "gid://shopify/Product/1",
    body_html = "<p>Great product 1</p>",
    created_at = "2024-01-01T00:00:00Z",
    handle = "product-1",
    id = 1L,
    image = Image(
        id = 1L,
        product_id = 1L,
        position = 1,
        created_at = "2024-01-01T00:00:00Z",
        updated_at = "2024-01-01T00:00:00Z",
        alt = "Product 1 Image",
        width = 640,
        height = 480,
        src = "https://example.com/image1.jpg",
        admin_graphql_api_id = "gid://shopify/ProductImage/1"
        ,
    variant_ids = listOf(1L)
    ),
    images = listOf(
        Image(
            id = 1L,
            product_id = 1L,
            position = 1,
            created_at = "2024-01-01T00:00:00Z",
            updated_at = "2024-01-01T00:00:00Z",
            alt = "Product 1 Image",
            width = 640,
            height = 480,
            src = "https://www.searchenginejournal.com/wp-content/uploads/2022/08/google-shopping-ads-6304dccb7a49e-sej.png",
                    admin_graphql_api_id = "gid://shopify/ProductImage/1"
            ,
            variant_ids = listOf(1L)
        )
    ),
    options = listOf(
        Option(
            id = 1L,
            product_id = 1L,
            name = "Size",
            position = 1,
            values = listOf("S", "M", "L")
        )
    ),
    product_type = "Clothing",
    published_at = "2024-01-01T00:00:00Z",
    published_scope = "global",
    status = "active",
    tags = "tag1,tag2",
    template_suffix = "null",
    title = "Product 1",
    updated_at = "2024-01-01T00:00:00Z",
    variants = listOf(
        Variant(
            id = 1L,
            product_id = 1L,
            title = "Small",
            price = "19.99",
            sku = "sku1",
            position = 1,
            inventory_policy = "deny",
            compare_at_price = "null",
            fulfillment_service = "manual",
            inventory_management = "shopify",
            option1 = "S",
            option2 = "null",
            option3 = "null",
            created_at = "2024-01-01T00:00:00Z",
            updated_at = "2024-01-01T00:00:00Z",
            taxable = true,
            barcode = "null",
            grams = 200,
            image_id = "null",
            weight = 0.5,
            weight_unit = "kg",
            inventory_item_id = 1L,
            inventory_quantity = 100,
            old_inventory_quantity = 100,
            requires_shipping = true,
            admin_graphql_api_id = "gid://shopify/ProductVariant/1"
        )
    ),
    vendor = "Vendor 1"
    ),
        Product(
            admin_graphql_api_id = "gid://shopify/Product/1",
            body_html = "<p>Great product 1</p>",
            created_at = "2024-01-01T00:00:00Z",
            handle = "product-1",
            id = 1L,
            image = Image(
                id = 1L,
                product_id = 1L,
                position = 1,
                created_at = "2024-01-01T00:00:00Z",
                updated_at = "2024-01-01T00:00:00Z",
                alt = "Product 1 Image",
                width = 640,
                height = 480,
                src = "https://example.com/image1.jpg",
                admin_graphql_api_id = "gid://shopify/ProductImage/1"
                ,
                variant_ids = listOf(1L)
            ),
            images = listOf(
                Image(
                    id = 1L,
                    product_id = 1L,
                    position = 1,
                    created_at = "2024-01-01T00:00:00Z",
                    updated_at = "2024-01-01T00:00:00Z",
                    alt = "Product 1 Image",
                    width = 640,
                    height = 480,
                    src = "https://www.searchenginejournal.com/wp-content/uploads/2022/08/google-shopping-ads-6304dccb7a49e-sej.png",
                    admin_graphql_api_id = "gid://shopify/ProductImage/1"
                    ,
                    variant_ids = listOf(1L)
                )
            ),
            options = listOf(
                Option(
                    id = 1L,
                    product_id = 1L,
                    name = "Size",
                    position = 1,
                    values = listOf("S", "M", "L")
                )
            ),
            product_type = "Clothing",
            published_at = "2024-01-01T00:00:00Z",
            published_scope = "global",
            status = "active",
            tags = "tag1,tag2",
            template_suffix = "null",
            title = "Product 1",
            updated_at = "2024-01-01T00:00:00Z",
            variants = listOf(
                Variant(
                    id = 1L,
                    product_id = 1L,
                    title = "Small",
                    price = "19.99",
                    sku = "sku1",
                    position = 1,
                    inventory_policy = "deny",
                    compare_at_price = "null",
                    fulfillment_service = "manual",
                    inventory_management = "shopify",
                    option1 = "S",
                    option2 = "null",
                    option3 = "null",
                    created_at = "2024-01-01T00:00:00Z",
                    updated_at = "2024-01-01T00:00:00Z",
                    taxable = true,
                    barcode = "null",
                    grams = 200,
                    image_id = "null",
                    weight = 0.5,
                    weight_unit = "kg",
                    inventory_item_id = 1L,
                    inventory_quantity = 100,
                    old_inventory_quantity = 100,
                    requires_shipping = true,
                    admin_graphql_api_id = "gid://shopify/ProductVariant/1"
                )
            ),
            vendor = "Vendor 1"
        ) ,
        Product(
            admin_graphql_api_id = "gid://shopify/Product/1",
            body_html = "<p>Great product 1</p>",
            created_at = "2024-01-01T00:00:00Z",
            handle = "product-1",
            id = 1L,
            image = Image(
                id = 1L,
                product_id = 1L,
                position = 1,
                created_at = "2024-01-01T00:00:00Z",
                updated_at = "2024-01-01T00:00:00Z",
                alt = "Product 1 Image",
                width = 640,
                height = 480,
                src = "https://example.com/image1.jpg",
                admin_graphql_api_id = "gid://shopify/ProductImage/1"
                ,
                variant_ids = listOf(1L)
            ),
            images = listOf(
                Image(
                    id = 1L,
                    product_id = 1L,
                    position = 1,
                    created_at = "2024-01-01T00:00:00Z",
                    updated_at = "2024-01-01T00:00:00Z",
                    alt = "Product 1 Image",
                    width = 640,
                    height = 480,
                    src = "https://www.searchenginejournal.com/wp-content/uploads/2022/08/google-shopping-ads-6304dccb7a49e-sej.png",
                    admin_graphql_api_id = "gid://shopify/ProductImage/1"
                    ,
                    variant_ids = listOf(1L)
                )
            ),
            options = listOf(
                Option(
                    id = 1L,
                    product_id = 1L,
                    name = "Size",
                    position = 1,
                    values = listOf("S", "M", "L")
                )
            ),
            product_type = "Clothing",
            published_at = "2024-01-01T00:00:00Z",
            published_scope = "global",
            status = "active",
            tags = "tag1,tag2",
            template_suffix = "null",
            title = "Product 1",
            updated_at = "2024-01-01T00:00:00Z",
            variants = listOf(
                Variant(
                    id = 1L,
                    product_id = 1L,
                    title = "Small",
                    price = "19.99",
                    sku = "sku1",
                    position = 1,
                    inventory_policy = "deny",
                    compare_at_price = "null",
                    fulfillment_service = "manual",
                    inventory_management = "shopify",
                    option1 = "S",
                    option2 = "null",
                    option3 = "null",
                    created_at = "2024-01-01T00:00:00Z",
                    updated_at = "2024-01-01T00:00:00Z",
                    taxable = true,
                    barcode = "null",
                    grams = 200,
                    image_id = "null",
                    weight = 0.5,
                    weight_unit = "kg",
                    inventory_item_id = 1L,
                    inventory_quantity = 100,
                    old_inventory_quantity = 100,
                    requires_shipping = true,
                    admin_graphql_api_id = "gid://shopify/ProductVariant/1"
                )
            ),
            vendor = "Vendor 1"
        )

        // Add more products as needed
    )
//    ProductGridScreen(fakeProducts = fakeProducts)
}