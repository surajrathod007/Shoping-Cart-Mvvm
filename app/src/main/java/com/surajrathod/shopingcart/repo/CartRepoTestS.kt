package com.surajrathod.shopingcart.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.surajrathod.shopingcart.model.CartItem
import com.surajrathod.shopingcart.model.Product

class CartRepoTestS {
    val TAG = "Suraj"
    //var mutableCart = MutableLiveData<List<CartItem>>()
    var _mutableCart = MutableLiveData<ArrayList<CartItem>>()     //if i dont pass this function , it will give error
    val cart : LiveData<ArrayList<CartItem>>
        get() = _mutableCart

    var _mutableTotal = MutableLiveData<Double>(0.0)


    fun initCart() {
        _mutableCart.setValue(ArrayList<CartItem>())

    }
    fun addItemToCart(product: Product) : Boolean{

        if (_mutableCart.getValue() == null) {
            initCart()

        }
        val itemList : ArrayList<CartItem> = ArrayList(_mutableCart.value)
        // var cartItemList : ArrayList<CartItem> = _mutableCart.value as ArrayList
        //List<CartItem> cartItemList = new ArrayList<>(_mutableCart.value);

        itemList.forEach {

            if(it.product.id.equals(product.id)){

                if(it.qty == 5)
                    return false

                val index = itemList.indexOf(it)        //getting index of current item
                it.qty += 1                             //if items are same then increas qty by 1 , rather than adding same items at diffrent index

                itemList.set(index,it)
                _mutableCart.postValue(itemList)
                calculateTotal()

                return true
            }
        }
        val cartItem = CartItem(product,1)

        itemList.add(cartItem)
//        _mutableCart.postValue(cartItemList)

        _mutableCart.postValue(itemList)
        calculateTotal()

        return true

    }

    fun deleteFromCart(cartItem: CartItem){
        if(_mutableCart.value == null){
            return
        }

        val itemList : ArrayList<CartItem> = ArrayList(_mutableCart.value)

        itemList.remove(cartItem)

        _mutableCart.postValue(itemList)
        calculateTotal()
    }

    fun changeQty(cartItem: CartItem, qty : Int){
        if(_mutableCart.value == null){
            return
        }
        val itemList : ArrayList<CartItem> = ArrayList(_mutableCart.value)

        val updatedCartItem = CartItem(cartItem.product,qty)

        itemList.set(itemList.indexOf(cartItem),updatedCartItem)

        _mutableCart.postValue(itemList)
        calculateTotal()
    }

    fun getTotal() : LiveData<Double> {

        if(_mutableTotal.value == null){
            //calculateTotal()
        }

        calculateTotal()
        return _mutableTotal
    }

    fun calculateTotal(){

        var total = 0.0
        //val cartItemList: List<CartItem> = _mutableCart.value!!
        //val cartItemList: MutableList<CartItem> = ArrayList(_mutableCart.value)


        if(_mutableCart.value.isNullOrEmpty()){
            return
        }

        _mutableTotal.value = _mutableCart?.value?.size?.toDouble()



    }
}