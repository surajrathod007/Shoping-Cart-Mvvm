package com.surajrathod.shopingcart.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.surajrathod.shopingcart.repo.CartRepo
import com.surajrathod.shopingcart.repo.ShopRepo

class ShopViewModelFactory(val repo: ShopRepo, val cartRepo: CartRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShopViewModel(repo,cartRepo) as T
    }
}