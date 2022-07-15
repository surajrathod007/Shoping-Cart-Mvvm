package com.surajrathod.shopingcart.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.surajrathod.shopingcart.R
import com.surajrathod.shopingcart.adapters.CartAdapter
import com.surajrathod.shopingcart.databinding.FragmentCartBinding
import com.surajrathod.shopingcart.databinding.FragmentShopBinding
import com.surajrathod.shopingcart.model.CartItem
import com.surajrathod.shopingcart.repo.CartRepo

import com.surajrathod.shopingcart.repo.ShopRepo
import com.surajrathod.shopingcart.viewmodels.ShopViewModel
import com.surajrathod.shopingcart.viewmodels.ShopViewModelFactory


class CartFragment : Fragment() , CartAdapter.CartInterface{

    lateinit var binding : FragmentCartBinding
    lateinit var navController: NavController
    lateinit var viewModel: ShopViewModel
    val cartRepo = CartRepo()
    val shopRepo = ShopRepo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding = FragmentCartBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        viewModel = ViewModelProvider(requireActivity(),ShopViewModelFactory(shopRepo,cartRepo)).get(ShopViewModel::class.java)

        binding.cartRecyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL)
        )
        viewModel.getCart().observe(viewLifecycleOwner,{

            binding.cartRecyclerView.adapter = it?.let { it1 -> CartAdapter(it1,this) }
            binding.placeOrderButton.isEnabled = it?.size!! >0
        })


        //get Total Price
        viewModel.getTotalPrice()?.observe(viewLifecycleOwner,{

            binding.orderTotalTextView.text = "Total : $ $it"
        })

        binding.placeOrderButton.setOnClickListener {
            navController.navigate(R.id.action_cartFragment_to_orderFragment)
        }


    }

    override fun deleteItem(cartItem: CartItem) {
        viewModel.deleteFromCart(cartItem)
        //Toast.makeText(requireContext(),"$cartItem Deleted",Toast.LENGTH_SHORT).show()
    }

    override fun changeQty(cartItem: CartItem, qty: Int) {
        viewModel.cartRepo.changeQuantity(cartItem,qty)
        //Toast.makeText(requireContext(),"$qty Deleted",Toast.LENGTH_SHORT).show()
    }


}