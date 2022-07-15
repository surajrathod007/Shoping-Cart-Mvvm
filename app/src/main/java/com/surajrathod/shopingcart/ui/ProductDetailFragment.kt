package com.surajrathod.shopingcart.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.surajrathod.shopingcart.R
import com.surajrathod.shopingcart.databinding.FragmentProductDetailBinding
import com.surajrathod.shopingcart.repo.CartRepo
import com.surajrathod.shopingcart.repo.ShopRepo
import com.surajrathod.shopingcart.viewmodels.ShopViewModel
import com.surajrathod.shopingcart.viewmodels.ShopViewModelFactory


class ProductDetailFragment : Fragment() {


    lateinit var viewModel: ShopViewModel
    lateinit var binding: FragmentProductDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val repo = ShopRepo()
        val cart = CartRepo()
        binding = FragmentProductDetailBinding.inflate(inflater,container,false)

        viewModel = ViewModelProvider(requireActivity(),ShopViewModelFactory(repo,cart)).get(ShopViewModel::class.java)

        binding.shopViewModel = viewModel

        //Glide.with(binding.imgProductDetail).load(viewModel.product.value?.image).fitCenter().into(binding.imgProductDetail)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}