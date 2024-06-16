package com.example.shopify_app.features.wishList.data

import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.DraftOderRequest
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import kotlinx.coroutines.flow.Flow

class WishListRopeImpl private constructor(
    private val appRemoteDataSourse: AppRemoteDataSourse
)  : WishListRope {

    companion object {
        private var instance: WishListRopeImpl? = null
        fun getInstance(appRemoteDataSourse: AppRemoteDataSourse): WishListRopeImpl {
            if (instance == null) {
                instance = WishListRopeImpl(appRemoteDataSourse)
            }
            return instance!!
        }
    }
    override suspend fun updateDraftOrder(
        id: String,
        draftOrder: DraftOrderResponse
    ): Flow<DraftOrderResponse> {
        return appRemoteDataSourse.updateDraftOrder(id,draftOrder)
    }

    override suspend fun getDraftOrder(id: String): Flow<DraftOrderResponse> {
        return appRemoteDataSourse.getDraftOrder(id)
    }
}