package com.surajrathod.shopingcart.adapters

import android.widget.ImageView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


//it is working adapter
@BindingAdapter("android:imageUrls")
fun ImageView.setImage(url: String){
    Glide.with(this.context).load(url).fitCenter().into(this)
}

@BindingAdapter("android:SetVal")
fun getSelectedSpinnerValue(spinner : Spinner,qty : Int){
    spinner.setSelection(qty - 1,true)
}