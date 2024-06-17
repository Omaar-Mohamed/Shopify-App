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
import kotlinx.coroutines.launch
import kotlin.math.log

class DraftViewModel(
    val repo : ProductsDetailsRepo
) : ViewModel() {
    private val _cartDraft = MutableStateFlow<ApiState<DraftOrderResponse>>(ApiState.Loading)
    val cartDraft : StateFlow<ApiState<DraftOrderResponse>> = _cartDraft

    private val _updateDraftResponse = MutableStateFlow<ApiState<DraftOrderResponse>>(ApiState.Loading)
    val updateDraftResponse : StateFlow<ApiState<DraftOrderResponse>> = _updateDraftResponse

    fun getDraftOrder(id : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDraftOrder(id)
                .catch {
                    _cartDraft.value = ApiState.Failure(it)
                }.collect {
                    _cartDraft.value = ApiState.Success(it)
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
}