package com.example.shopify_app.features.categories.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.features.categories.data.model.CustomCategoriesResponse
import com.example.shopify_app.features.categories.data.repo.CategoriesRepo
import com.example.shopify_app.features.categories.data.repo.CategoriesRepoImpl
import com.example.shopify_app.features.categories.viewmodel.CategoriesViewModel
import com.example.shopify_app.features.categories.viewmodel.CategoriesViewModelFactory

//@Composable
//fun CategoryCardList() {
//    LazyColumn(
//        modifier = Modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(2.dp)
//    ) {
//        items(7) {
//            CategoryCard()
//        }
//    }
//}

@Composable
fun CategoryScreen(
    navController: NavHostController,
    repo: CategoriesRepo = CategoriesRepoImpl.getInstance(AppRemoteDataSourseImpl)
) {
    val factory = CategoriesViewModelFactory(repo)
    val viewModel: CategoriesViewModel = viewModel(factory = factory)

    LaunchedEffect(Unit) {
        viewModel.getCategories()
    }

    val categoriesState by viewModel.categories.collectAsState()

    when (categoriesState) {
        is ApiState.Loading -> {
            Log.i("getCategories", "CategoryScreen: loading...")
        }
        is ApiState.Failure -> {
            Log.i("getCategories", (categoriesState as ApiState.Failure).error.toString())
        }
        is ApiState.Success<CustomCategoriesResponse> -> {
            val categories = (categoriesState as ApiState.Success<CustomCategoriesResponse>).data.custom_collections
            Log.i("getCategories", "CategoryScreen: $categories")

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    CategoryTopSection(navController)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Categories",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(categories.drop(1)) { category ->
                    CategoryCard(category = category)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCategoryScreen() {
}