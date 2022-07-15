package com.surajrathod.shopingcart.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.surajrathod.shopingcart.model.Product
import java.util.*
import kotlin.collections.ArrayList


class ShopRepo {

    var _product_list = MutableLiveData<List<Product>>()

            val product_list : LiveData<List<Product>>
            get() = _product_list


    fun loadQuotes(){

        val productList: MutableList<Product> = ArrayList()
        productList.add(
            Product(
                UUID.randomUUID().toString(),
                "iMac 21",
                10,
                true,
                "https://exploringbits.com/wp-content/uploads/2021/11/anime-girl-pfp-2.jpg"
            )
        )
        productList.add(
            Product(
                UUID.randomUUID().toString(),
                "iPad Air",
                89,
                true,
                "https://exploringbits.com/wp-content/uploads/2021/11/anime-girl-pfp-2.jpg"
            )
        )
        productList.add(
            Product(
                UUID.randomUUID().toString(),
                "iPad Pro",
                999,
                true,
                "https://exploringbits.com/wp-content/uploads/2021/11/anime-girl-pfp-2.jpg"
            )
        )
        productList.add(
            Product(
                UUID.randomUUID().toString(),
                "iPhone 11",
                699,
                false,
                "https://exploringbits.com/wp-content/uploads/2021/11/anime-girl-pfp-2.jpg"
            )
        )
        productList.add(
            Product(
                UUID.randomUUID().toString(),
                "iPhone 11 Pro",
                999,
                true,
                "https://exploringbits.com/wp-content/uploads/2021/11/anime-girl-pfp-2.jpg"
            )
        )
        productList.add(
            Product(
                UUID.randomUUID().toString(),
                "iPhone 11 Pro Max",
                1099,
                true,
                "https://exploringbits.com/wp-content/uploads/2021/11/anime-girl-pfp-2.jpg"
            )
        )
        productList.add(
            Product(
                UUID.randomUUID().toString(),
                "iPhone SE",
                399,
                true,
                "https://exploringbits.com/wp-content/uploads/2021/11/anime-girl-pfp-2.jpg"
            )
        )
        productList.add(
            Product(
                UUID.randomUUID().toString(),
                "MacBook Air",
                999,
                true,
                "https://exploringbits.com/wp-content/uploads/2021/11/anime-girl-pfp-2.jpg"
            )
        )
        productList.add(
            Product(
                UUID.randomUUID().toString(),
                "MacBook Pro 13",
                1299,
                true,
                "https://exploringbits.com/wp-content/uploads/2021/11/anime-girl-pfp-2.jpg"
            )
        )
        productList.add(
            Product(
                UUID.randomUUID().toString(),
                "MacBook Pro 16",
                2399,
                true,
                "https://exploringbits.com/wp-content/uploads/2021/11/anime-girl-pfp-2.jpg"
            )
        )


        _product_list.postValue(productList)
        //_product_list.value = list
    }

}