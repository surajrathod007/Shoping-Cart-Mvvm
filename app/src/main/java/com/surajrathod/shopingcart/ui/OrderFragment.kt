package com.surajrathod.shopingcart.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.surajrathod.shopingcart.R
import com.surajrathod.shopingcart.databinding.FragmentOrderBinding
import com.surajrathod.shopingcart.repo.CartRepo
import com.surajrathod.shopingcart.repo.ShopRepo
import com.surajrathod.shopingcart.viewmodels.ShopViewModel
import com.surajrathod.shopingcart.viewmodels.ShopViewModelFactory


class OrderFragment : Fragment() {


    lateinit var shopViewModel: ShopViewModel
    val cartRepo = CartRepo()
    val shopRepo = ShopRepo()
    lateinit var navController: NavController
    lateinit var binding : FragmentOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        shopViewModel = ViewModelProvider(requireActivity(),ShopViewModelFactory(shopRepo,cartRepo)).get(ShopViewModel::class.java)
        binding = FragmentOrderBinding.inflate(inflater,container,false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        binding.txtContinue.setOnClickListener {
            shopViewModel.resetCart()
            navController.navigate(R.id.action_orderFragment_to_shopFragment)

        }

        binding.txtNewOrder.setOnClickListener {
            navController.navigate(R.id.action_orderFragment_to_newProductDetail)
        }

    }


}