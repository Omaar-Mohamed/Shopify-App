package com.example.shopify_app.features.products.ui
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.shopify_app.core.widgets.ProductCard
import com.example.shopify_app.features.home.data.models.ProductsResponse.Image
import com.example.shopify_app.features.home.data.models.ProductsResponse.Option
import com.example.shopify_app.features.home.data.models.ProductsResponse.Product
import com.example.shopify_app.features.home.data.models.ProductsResponse.Variant
import com.example.shopify_app.features.home.ui.SearchBar

//data class FakeProduct(val name: String, val description: String, val price: String, val imageResId: Int)

@Composable
fun UpperSection(navController: NavController) {
    // State for the slider value
    var sliderValue by remember { mutableStateOf(0f) }

    // State for the selected chips, initializing with "Chip 1" selected by default
    var selectedChips by remember { mutableStateOf(setOf("Chip 1")) }

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
            SearchBar()
        }

        // Spacer for separation between the rows
        Spacer(modifier = Modifier.height(16.dp))

        // Second Row: Section Name, Slider Value as Text, and Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Section Name: "Clothes"
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
                    onValueChange = { sliderValue = it },
                    valueRange = 0f..100f, // Define the range of the slider
                    modifier = Modifier.width(100.dp), // Set the width of the slider
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Black,
                        activeTrackColor = Color.Black,
                        inactiveTrackColor = Color.Gray
                    )
                )
            }
        }

        // Spacer for separation between the slider and chips
        Spacer(modifier = Modifier.height(16.dp))

        // ChipRow
        ChipRow(
            items = listOf("Chip 1", "Chip 2", "Chip 3", "Chip 4"), // List of chip texts
            selectedItems = selectedChips.toList(),
            onChipClick = { chipText ->
                selectedChips = if (selectedChips.contains(chipText)) {
                    selectedChips - chipText
                } else {
                    selectedChips + chipText
                }
            }
        )
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

// Fake products data
val fakeProducts = listOf(
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
)

@Composable
fun ProductGridScreen( navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)

    ) {
        UpperSection(navController)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columns in the grid
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(fakeProducts) { product ->
                ProductCard(product = product , navController = navController)
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