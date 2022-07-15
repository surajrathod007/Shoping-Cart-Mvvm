package com.surajrathod.shopingcart.ui

import android.icu.lang.UCharacter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.surajrathod.shopingcart.R
import com.surajrathod.shopingcart.adapters.ShopListAdapter
import com.surajrathod.shopingcart.databinding.FragmentShopBinding
import com.surajrathod.shopingcart.model.Product
import com.surajrathod.shopingcart.repo.CartRepo
import com.surajrathod.shopingcart.repo.ShopRepo
import com.surajrathod.shopingcart.viewmodels.ShopViewModel
import com.surajrathod.shopingcart.viewmodels.ShopViewModelFactory


class ShopFragment : Fragment() , ShopListAdapter.ShopInterface{

    lateinit var binding : FragmentShopBinding

    lateinit var navController: NavController

    lateinit var viewModel: ShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopBinding.inflate(inflater,container,false)





        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        val repo = ShopRepo()
        val cartRepo = CartRepo()
        viewModel = ViewModelProvider(requireActivity(),ShopViewModelFactory(repo,cartRepo)).get(ShopViewModel::class.java)



        binding.rvProduct.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
        binding.rvProduct.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.HORIZONTAL))
        viewModel.getProducts().observe(viewLifecycleOwner,{

            binding.rvProduct.adapter = ShopListAdapter(it,this)


        })
    }


    @BindingAdapter("imageUrl")
    fun ImageView.setImage(url: String){
        Glide.with(this).load(url).fitCenter().into(this)
    }

    companion object{
        @BindingAdapter("android:productImage")
        fun loadImage(img : ImageView, url : String){

            Glide.with(img).load(url).fitCenter().into(img)
        }
    }

    override fun addItem(product: Product) {
        val isAdded = viewModel.addToCart(product)
        if(isAdded){
            Snackbar.make(requireView(),"${product.name} Added to cart.",Snackbar.LENGTH_LONG).setAction("CheckOut",{
                navController.navigate(R.id.action_shopFragment_to_cartFragment)
            }).show()
        }else{
            Snackbar.make(requireView(),"Maximum quantity of product is only 5",Snackbar.LENGTH_LONG).show()
        }

    }

    override fun onItemClick(product: Product) {
        //Toast.makeText(requireContext(),"Im clicked ${product.name}", Toast.LENGTH_SHORT).show()
        viewModel.setProduct(product)
        navController.navigate(R.id.action_shopFragment_to_productDetailFragment)
    }


}