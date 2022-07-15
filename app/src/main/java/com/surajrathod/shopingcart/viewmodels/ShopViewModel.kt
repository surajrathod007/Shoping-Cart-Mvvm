package com.surajrathod.shopingcart.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.surajrathod.shopingcart.model.CartItem
import com.surajrathod.shopingcart.model.Product
import com.surajrathod.shopingcart.repo.CartRepo
import com.surajrathod.shopingcart.repo.ShopRepo

class ShopViewModel(val repo : ShopRepo, val cartRepo: CartRepo) : ViewModel() {

    var _mutableProduct = MutableLiveData<Product>()
    val product : LiveData<Product>
    get() = _mutableProduct

    init {
        repo.loadQuotes()
    }
    fun getProducts() : LiveData<List<Product>>{
        return repo.product_list
    }

    fun setProduct(product: Product){
        _mutableProduct.postValue(product)
    }

    fun getCart() : LiveData<List<CartItem>?> {
        return cartRepo.getCart()
    }

    fun addToCart(product: Product) : Boolean{
        return cartRepo.addItemToCart(product)
    }

    fun deleteFromCart(cartItem: CartItem){
        cartRepo.removeItemFromCart(cartItem)
    }

    fun changeQty(cartItem: CartItem,qty : Int){
        cartRepo.changeQuantity(cartItem,qty)
    }

    fun getTotalPrice() : LiveData<Double?>? {
        return cartRepo.getTotalPrice()
    }

    fun resetCart(){
        cartRepo.initCart()
        cartRepo.mutableTotalPrice.value = 0.0
    }
}