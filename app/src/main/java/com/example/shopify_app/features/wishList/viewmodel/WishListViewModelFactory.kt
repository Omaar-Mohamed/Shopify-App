package com.example.shopify_app.features.wishList.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify_app.features.wishList.data.WishListRope

class WishListViewModelFactory  (
    private val repository: WishListRope
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WishListViewModel::class.java)) {
            WishListViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}