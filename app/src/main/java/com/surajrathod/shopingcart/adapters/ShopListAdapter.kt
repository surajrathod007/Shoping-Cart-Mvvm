package com.surajrathod.shopingcart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.surajrathod.shopingcart.R
import com.surajrathod.shopingcart.databinding.ItemProductBinding
import com.surajrathod.shopingcart.model.Product

class ShopListAdapter(val data : List<Product>,val shopInterface: ShopInterface) : RecyclerView.Adapter<ShopListAdapter.ViewHolder>(){



    inner class ViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root){

        val image : ImageView = binding.imageView


        fun bind(item : Product){
            binding.product = item
        }
    }



    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val listItemBinding = ItemProductBinding.inflate(inflater,parent,false)

        listItemBinding.shopInterface = shopInterface
        return ViewHolder(listItemBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //Glide.with(holder.image).load(data[position].image).fitCenter().into(holder.image)

//        val phone = data[position].name
//        holder.binding.root.setOnClickListener {
//            Toast.makeText(it.context,"Im clicked $phone",Toast.LENGTH_SHORT).show()
//        }
        holder.bind(data[position])
    }

    interface ShopInterface{
        fun addItem(product : Product)
        fun onItemClick(product : Product)
    }





}