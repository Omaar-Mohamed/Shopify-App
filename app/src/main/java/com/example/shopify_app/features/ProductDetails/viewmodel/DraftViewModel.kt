package com.example.shopify_app.features.ProductDetails.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepo
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.LineItem
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrder
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.log

class DraftViewModel(
    val repo : ProductsDetailsRepo
) : ViewModel() {
    private val _cartDraft = MutableStateFlow<ApiState<DraftOrderResponse>>(ApiState.Loading)
    val cartDraft : StateFlow<ApiState<DraftOrderResponse>> = _cartDraft

    private val _updateDraftResponse = MutableStateFlow<ApiState<DraftOrderResponse>>(ApiState.Loading)
    val updateDraftResponse : StateFlow<ApiState<DraftOrderResponse>> = _updateDraftResponse

    private val _isCartDraft = MutableStateFlow<ApiState<Boolean>>(ApiState.Loading)
    val isCartDraft : StateFlow<ApiState<Boolean>> = _isCartDraft

    fun getDraftOrder(id : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDraftOrder(id)
                .catch {
                    _cartDraft.value = ApiState.Failure(it)
                }.collect {
                    val newLineItem = it.draft_order.line_items.filterNot { item->
                        item.title == "dummy"
                    }
                    val newDraftOrder = it.draft_order.copy(
                        line_items = newLineItem
                    )

                    _cartDraft.value = ApiState.Success(DraftOrderResponse(newDraftOrder))
                }
        }
    }

    fun addLineItemToDraft(id: String, lineItem: LineItem)
    {
        Log.i("TAG", "addLineItemToDraft: $lineItem")
        getDraftOrder(id)
        viewModelScope.launch(Dispatchers.IO) {
            cartDraft.collect{
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
                        val newLineItemList = oldLineItemList.filterNot {
                            it.title.equals("dummy",ignoreCase = true)
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
                        }
                    }
                }
            }
        }
    }

    fun removeLineItemFromDraft(id: String, lineItem: LineItem)
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
                    if (oldLineItemList.count() <= 1)
                    {
                        newLineItemList[0].apply {
                            title = "dummy"
                            variant_id = null
                            product_id = null
                            price = "0"
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
                        getDraftOrder(id)
                    }
                }
            }

        }
    }

    fun isFavoriteLineItem(id: String, lineItem: LineItem){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDraftOrder(id)
                .catch {
                    _isCartDraft.value = ApiState.Failure(it)
                }.collect {
                    val lineItems = it.draft_order.line_items.toMutableList()
                    val containsItem = lineItems.any { item -> item.variant_id == lineItem.variant_id }
                    if (containsItem){
                        _isCartDraft.value = ApiState.Success(true)
                    }else{
                        _isCartDraft.value = ApiState.Success(false)
                    }
                }
        }
    }
}