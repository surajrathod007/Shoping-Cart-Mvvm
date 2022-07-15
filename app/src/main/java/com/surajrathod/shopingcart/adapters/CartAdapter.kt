package com.surajrathod.shopingcart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.surajrathod.shopingcart.databinding.CartRowBinding
import com.surajrathod.shopingcart.model.CartItem

class CartAdapter(val data : List<CartItem>,val cartInterface: CartInterface) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

     inner class ViewHolder(private val binding : CartRowBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : CartItem){
            binding.cartItem = item

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CartRowBinding.inflate(inflater,parent,false)

        binding.deleteProductButton.setOnClickListener {
            cartInterface.deleteItem(binding.cartItem!!)
        }

        binding.quantitySpinner.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val quantity = position + 1
                if(quantity == binding.cartItem!!.qty)
                    return
                cartInterface.changeQty(binding.cartItem!!,quantity)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])


    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface CartInterface{
        fun deleteItem(cartItem: CartItem)
        fun changeQty(cartItem: CartItem,qty : Int)
    }
}