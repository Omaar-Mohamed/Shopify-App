package com.example.shopify_app.features.ProductDetails.data.repo

import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.features.ProductDetails.data.model.ProductDetailResponse
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrder
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import kotlinx.coroutines.flow.Flow

class ProductsDetailsRepoImpl(
    private val appRemoteDataSourse: AppRemoteDataSourse
) : ProductsDetailsRepo {
    companion object {
        private var instance: ProductsDetailsRepoImpl? = null
        fun getInstance(appRemoteDataSourse: AppRemoteDataSourse): ProductsDetailsRepoImpl {
            if (instance == null) {
                instance = ProductsDetailsRepoImpl(appRemoteDataSourse)
            }
            return instance!!
        }
    }
    override suspend fun getProductsDetails(id: String): Flow<ProductDetailResponse> {
        return appRemoteDataSourse.getProductsDetails(id)
    }

    override suspend fun getDraftOrder(id: String): Flow<DraftOrderResponse> {
        return appRemoteDataSourse.getDraftOrder(id)
    }

    override suspend fun updateDraftOrder(id: String, newDraftOrder : DraftOrder): Flow<DraftOrderResponse> {
        return appRemoteDataSourse.updateDraftOrder(id, newDraftOrder)
    }
}