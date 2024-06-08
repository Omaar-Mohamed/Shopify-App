package com.example.shopify_app.features.products.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import com.example.shopify_app.core.widgets.ProductCard
import com.example.shopify_app.R
import com.example.shopify_app.features.home.data.models.ProductsResponse.Image
import com.example.shopify_app.features.home.data.models.ProductsResponse.Option
import com.example.shopify_app.features.home.data.models.ProductsResponse.Product
import com.example.shopify_app.features.home.data.models.ProductsResponse.Variant

//data class FakeProduct(val name: String, val description: String, val price: String, val imageResId: Int)

@Composable
fun UpperSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { /* Handle back icon click */ }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
        Text(
            text = "Clothes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "XXX",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { /* Handle search icon click */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun ProductGridScreen(fakeProducts: List<Product>) {
    Column {
        UpperSection()
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columns in the grid
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(fakeProducts) { product ->
                ProductCard(product = product)
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
    ProductGridScreen(fakeProducts = fakeProducts)
}