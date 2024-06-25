    package com.example.shopify_app.features.ProductDetails.viewmodel

    import android.util.Log
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.shopify_app.core.networking.ApiState
    import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepo
    import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRule
    import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.AppliedDiscount
    import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.LineItem
    import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrder
    import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.delay
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.flow.catch
    import kotlinx.coroutines.flow.debounce
    import kotlinx.coroutines.flow.first
    import kotlinx.coroutines.launch
    import kotlin.math.log

    class DraftViewModel(
        val repo : ProductsDetailsRepo
    ) : ViewModel() {
        private val _cartDraft = MutableStateFlow<ApiState<DraftOrderResponse>>(ApiState.Loading)
        val cartDraft : StateFlow<ApiState<DraftOrderResponse>> = _cartDraft

        private val _favoriteProduct = MutableStateFlow<ApiState<DraftOrderResponse>>(ApiState.Loading)
        val favoriteProduct : StateFlow<ApiState<DraftOrderResponse>> = _favoriteProduct

        private val _updateDraftResponse = MutableStateFlow<ApiState<DraftOrderResponse>>(ApiState.Loading)
        val updateDraftResponse : StateFlow<ApiState<DraftOrderResponse>> = _updateDraftResponse

        private val _inFavorite = MutableStateFlow<ApiState<Boolean>>(ApiState.Loading)
        val inFavorite : StateFlow<ApiState<Boolean>> = _inFavorite

        private val _inCart = MutableStateFlow<ApiState<Boolean>>(ApiState.Loading)
        val inCart : StateFlow<ApiState<Boolean>> = _inCart

    fun getDraftOrder(id : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDraftOrder(id)
                .catch {
                    _cartDraft.value = ApiState.Failure(it)
                }.collect {
                    val newLineItem = it.draft_order.line_items.filterNot { item->
                        item.variant_id == null
                    }
                    val newDraftOrder = it.draft_order.copy(
                        line_items = newLineItem
                    )

                    _cartDraft.value = ApiState.Success(DraftOrderResponse(newDraftOrder))
                }
        }
    }

    fun getFavoriteProduct(id : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDraftOrder(id)
                .catch {
                    _favoriteProduct.value = ApiState.Failure(it)
                }.collect {
                    val newLineItem = it.draft_order.line_items.filterNot { item->
                        item.variant_id == null
                    }
                    val newDraftOrder = it.draft_order.copy(
                        line_items = newLineItem
                    )

                    _favoriteProduct.value = ApiState.Success(DraftOrderResponse(newDraftOrder))
                }
        }
    }


        fun addLineItemToDraft(id: String, lineItem: LineItem)
        {
//            Log.i("TAG", "addLineItemToDraft: $lineItem")
            getDraftOrder(id)
            viewModelScope.launch(Dispatchers.IO) {
                cartDraft.collect{
                    when(it){
                        is ApiState.Failure -> {
                            it.error.printStackTrace()
//                            Log.i("tag", "addLineItemToDraft: couldn't add ")
                        }
                        ApiState.Loading -> {
//                            Log.i("TAG", "addLineItemToDraft: adding")
                        }
                        is ApiState.Success -> {
//                            Log.i("TAG", "addLineItemToDraft: successfull")
                            val draftOrder : DraftOrder = it.data.draft_order
                            val oldLineItemList  = it.data.draft_order.line_items.toMutableList()
                            oldLineItemList.add(lineItem)
                            val newLineItemList = oldLineItemList.filterNot {item ->
                                item.title.equals("dummy",ignoreCase = true)
                            }
                            Log.i("TAG", "addLineItemToDraft: old is $draftOrder ")
                            val newDraftOrder = draftOrder.copy(
                                line_items = newLineItemList
                            )
                            Log.i("TAG", "addLineItemToDraft: new is $newDraftOrder ")
                            repo.updateDraftOrder(id,newDraftOrder).catch { e ->
                                e.printStackTrace()
                                _updateDraftResponse.value = ApiState.Failure(e)
                            }.collect{response ->
                                Log.i("TAG", "addLineItemToDraft: $response")
                                _updateDraftResponse.value = ApiState.Success(response)
                                isInCart(id,lineItem)
                                isFavoriteLineItem(id,lineItem)
                            }
                        }
                    }
                }
            }
        }

        fun addLineItemToFavorite(id: String, lineItem: LineItem)
        {
            Log.i("TAG", "addLineItemToDraft: $lineItem")
            getFavoriteProduct(id)
            viewModelScope.launch(Dispatchers.IO) {
                favoriteProduct.collect{
                    when(it){
                        is ApiState.Failure -> {
                            it.error.printStackTrace()
                            Log.i("tag", "addLineItemToDraft: couldn't add ")
                        }
                        ApiState.Loading -> {
                            Log.i("TAG", "addLineItemToDraft: adding")
                        }
                        is ApiState.Success -> {
                            Log.i("TAG", "addLineItemToDraft: successfull")
                            val draftOrder : DraftOrder = it.data.draft_order
                            val oldLineItemList  = it.data.draft_order.line_items.toMutableList()
                            oldLineItemList.add(lineItem)
                            val newLineItemList = oldLineItemList.filterNot {item ->
                                item.variant_id == null
                            }
                            Log.i("TAG", "addLineItemToDraft: old is $draftOrder ")
                            val newDraftOrder = draftOrder.copy(
                                line_items = newLineItemList
                            )
                            Log.i("TAG", "addLineItemToDraft: new is $newDraftOrder ")
                            repo.updateDraftOrder(id,newDraftOrder).catch { e ->
                                e.printStackTrace()
                                _updateDraftResponse.value = ApiState.Failure(e)
                            }.collect{response ->
                                Log.i("TAG", "addLineItemToDraft: $response")
                                _updateDraftResponse.value = ApiState.Success(response)
                                isInCart(id,lineItem)
                                isFavoriteLineItem(id,lineItem)
                            }
                        }
                    }
                }
            }
        }

        fun removeLineItemFromDraft(id: String, lineItem: LineItem)
        {
//            Log.i("TAG", "addLineItemToDraft: $lineItem")
            getDraftOrder(id)
            viewModelScope.launch(Dispatchers.IO) {
                val state = cartDraft.first()
                when(state){
                    is ApiState.Failure -> {
                        state.error.printStackTrace()
//                        Log.i("tag", "addLineItemToDraft: couldn't add ")
                    }
                    ApiState.Loading -> {
//                        Log.i("TAG", "addLineItemToDraft: adding")
                    }
                    is ApiState.Success -> {
//                        Log.i("TAG", "addLineItemToDraft: successfull")
                        val draftOrder : DraftOrder = state.data.draft_order
                        val oldLineItemList  = state.data.draft_order.line_items.toMutableList()
                        var newLineItemList = oldLineItemList
                        if (oldLineItemList.count() <= 1)
                        {
                            newLineItemList[0].apply {
                                variant_id = null
                                product_id = null
                                delay(10)
                                price = "0"
                            }

                        }else{
                            newLineItemList = oldLineItemList.filterNot { item->
                                item.variant_id == lineItem.variant_id
                            }.toMutableList()

                        }
//                        Log.i("TAG", "addLineItemToDraft:the count is ${oldLineItemList.count()} old is $oldLineItemList ")
                        val newDraftOrder = draftOrder.copy(
                            line_items = newLineItemList
                        )
//                        Log.i("TAG", "addLineItemToDraft: new is $newDraftOrder ")
                        repo.updateDraftOrder(id,newDraftOrder).catch { e ->
                            e.printStackTrace()
                            _updateDraftResponse.value = ApiState.Failure(e)
                        }.collect{response ->
//                            Log.i("TAG", "addLineItemToDraft: $response")
                            _updateDraftResponse.value = ApiState.Success(response)
                            getDraftOrder(id)
                            isFavoriteLineItem(id,lineItem)
                        }
                    }
                }
            }
        }

        fun removeLineItemFromFavorite(id: String, lineItem: LineItem)
        {
//            Log.i("TAG", "addLineItemToDraft: $lineItem")
            viewModelScope.launch(Dispatchers.IO) {
                getFavoriteProduct(id)
                val state = favoriteProduct.first()
                when(state){
                    is ApiState.Failure -> {
                        state.error.printStackTrace()
//                        Log.i("tag", "addLineItemToDraft: couldn't add ")
                    }
                    ApiState.Loading -> {
//                        Log.i("TAG", "addLineItemToDraft: adding")
                    }
                    is ApiState.Success -> {
                        Log.i("TAG", "addLineItemToDraft: successfull")
                        val draftOrder : DraftOrder = state.data.draft_order
                        val oldLineItemList  = state.data.draft_order.line_items.toMutableList()
                        var newLineItemList = oldLineItemList
                        if (oldLineItemList.count() <= 1)
                        {
                            newLineItemList[0].apply {
                                variant_id = null
                                product_id = null
                            }
                        }else{
                            newLineItemList = oldLineItemList.filterNot { item->
                                item.variant_id == lineItem.variant_id
                            }.toMutableList()

                        }
                        Log.i("TAG", "addLineItemToDraft:the count is ${oldLineItemList.count()} old is $oldLineItemList ")
                        val newDraftOrder = draftOrder.copy(
                            line_items = newLineItemList
                        )
                        Log.i("TAG", "addLineItemToDraft: new is $newDraftOrder ")
                        repo.updateDraftOrder(id,newDraftOrder).catch { e ->
                            e.printStackTrace()
                            _updateDraftResponse.value = ApiState.Failure(e)
                        }.collect{response ->
                            Log.i("TAG", "addLineItemToDraft: $response")
                            _updateDraftResponse.value = ApiState.Success(response)
                            getFavoriteProduct(id)
                            isFavoriteLineItem(id,lineItem)
                        }
                    }
                }
            }
        }

        fun clearAllInDraft(id : String){
            getDraftOrder(id)
            viewModelScope.launch(Dispatchers.IO) {
                val state = cartDraft.first()
                when(state){
                    is ApiState.Failure -> {
                        state.error.printStackTrace()
                        Log.i("tag", "addLineItemToDraft: couldn't add ")
                    }
                    ApiState.Loading -> {
                        Log.i("TAG", "addLineItemToDraft: adding")
                    }
                    is ApiState.Success -> {
                        Log.i("TAG", "addLineItemToDraft: successfull")
                        val draftOrder : DraftOrder = state.data.draft_order
                        val oldLineItemList  = state.data.draft_order.line_items.toMutableList()
                        val newLineItemList = listOf(
                            oldLineItemList[0].apply {
                                variant_id = null
                                product_id = null
                                delay(10)
                                price = "0"
                            }
                        )

                        Log.i("TAG", "addLineItemToDraft:the count is ${oldLineItemList.count()} old is $oldLineItemList ")
                        val newDraftOrder = draftOrder.copy(
                            line_items = newLineItemList
                        )
                        Log.i("TAG", "addLineItemToDraft: new is $newDraftOrder ")
                        repo.updateDraftOrder(id,newDraftOrder).catch { e ->
                            e.printStackTrace()
                            _updateDraftResponse.value = ApiState.Failure(e)
                        }.collect{response ->
                            Log.i("TAG", "addLineItemToDraft: $response")
                            _updateDraftResponse.value = ApiState.Success(response)
                            getDraftOrder(id)
                        }
                    }
                }
            }
        }
        fun addCoupon(id: String, priceRule: PriceRule)
        {
            getDraftOrder(id)
            viewModelScope.launch(Dispatchers.IO) {
            val state = cartDraft.first()
                when(state){
                    is ApiState.Failure -> {
                        state.error.printStackTrace()
                        Log.i("tag", "addLineItemToDraft: couldn't add ")
                    }
                    ApiState.Loading -> {
                        Log.i("TAG", "addLineItemToDraft: adding")
                    }
                    is ApiState.Success -> {
                        Log.i("TAG", "addLineItemToDraft: successfull")
                        val draftOrder : DraftOrder = state.data.draft_order
                        val amount  = priceRule.value.substring(1)
                        Log.i("TAG", "addCoupon: amount value is : $amount")
                        val newDraftOrder = draftOrder.copy(
                            applied_discount = AppliedDiscount(
                                title = priceRule.title,
                                value = amount,
                                amount = amount,
                                value_type = priceRule.value_type,
                                description = ""
                            )
                        )
                        Log.i("TAG", "addLineItemToDraft: new is $newDraftOrder ")
                        repo.updateDraftOrder(id,newDraftOrder).catch { e ->
                            e.printStackTrace()
                            _updateDraftResponse.value = ApiState.Failure(e)
                        }.collect{response ->
                            Log.i("TAG", "addLineItemToDraft: $response")
                            _updateDraftResponse.value = ApiState.Success(response)
                            getDraftOrder(id)
                        }
                    }
                }

            }
        }

        fun isFavoriteLineItem(id: String, lineItem: LineItem){
            Log.i("TAG", "isFavoriteLineItem: enter is favorite")
            viewModelScope.launch(Dispatchers.IO) {
                repo.getDraftOrder(id)
                    .catch {
                        Log.i("TAG", "isFavoriteLineItem: failed")
                        _inFavorite.value = ApiState.Failure(it)
                    }.collect {
                        val lineItems = it.draft_order.line_items.toMutableList()
                        val containsItem = lineItems.any { item -> item.variant_id == lineItem.variant_id }
                        if (containsItem){
                            _inFavorite.value = ApiState.Success(true)
                            Log.i("TAG", "isFavoriteLineItem: successfull true ")
                        }else{
                            _inFavorite.value = ApiState.Success(false)
                            Log.i("TAG", "isFavoriteLineItem: successfull false ")

                        }
                    }
            }
        }
        fun isInCart(id: String, lineItem: LineItem){
            Log.i("TAG", "isFavoriteLineItem: enter is favorite")
            viewModelScope.launch(Dispatchers.IO) {
                repo.getDraftOrder(id)
                    .catch {
                        Log.i("TAG", "isFavoriteLineItem: failed")
                        _inCart.value = ApiState.Failure(it)
                    }.collect {
                        val lineItems = it.draft_order.line_items.toMutableList()
                        val containsItem = lineItems.any { item -> item.variant_id == lineItem.variant_id }
                        if (containsItem){
                            _inCart.value = ApiState.Success(true)
                            Log.i("TAG", "isFavoriteLineItem: successfull true ")
                        }else{
                            _inCart.value = ApiState.Success(false)
                            Log.i("TAG", "isFavoriteLineItem: successfull false ")

                        }
                    }
            }
        }

        fun changeQuantity(id: String, lineItem: LineItem,quantity : Int)
        {
            Log.i("TAG", "addLineItemToDraft: $lineItem")
            getDraftOrder(id)
            viewModelScope.launch(Dispatchers.IO) {
                val state = cartDraft.first()
                when(state){
                    is ApiState.Failure -> {
                        state.error.printStackTrace()
                        Log.i("tag", "addLineItemToDraft: couldn't add ")
                    }
                    ApiState.Loading -> {
                        Log.i("TAG", "addLineItemToDraft: adding")
                    }
                    is ApiState.Success -> {
                        Log.i("TAG", "addLineItemToDraft: successfull")
                        val draftOrder : DraftOrder = state.data.draft_order
                        val oldLineItemList  = state.data.draft_order.line_items.toMutableList()
                        var newLineItemList = oldLineItemList
    //                    newLineItemList = oldLineItemList.filterNot { item->
    //                        item.variant_id == lineItem.variant_id
    //                    }.toMutableList()
                        newLineItemList.find { item ->
                            item.variant_id == lineItem.variant_id
                        }?.quantity = quantity

                        Log.i("TAG", "addLineItemToDraft:the count is ${oldLineItemList.count()} old is $oldLineItemList ")
                        val newDraftOrder = draftOrder.copy(
                            line_items = newLineItemList
                        )
                        Log.i("TAG", "addLineItemToDraft: new is $newDraftOrder ")
                        repo.updateDraftOrder(id,newDraftOrder)
                            .catch { e ->
                            e.printStackTrace()
                            _updateDraftResponse.value = ApiState.Failure(e)
                        }.collect{response ->
                            Log.i("TAG", "addLineItemToDraft: $response")
                            _updateDraftResponse.value = ApiState.Success(response)
                            getDraftOrder(id)
                        }
                    }
                }
            }
        }
    }