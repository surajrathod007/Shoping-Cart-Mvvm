package com.surajrathod.shopingcart.model

data class Product(
    val id : String,
    val name : String,
    val price : Int,
    val isAvailable : Boolean,
    val image : String
)