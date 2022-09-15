package com.surajrathod.shopingcart.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.surajrathod.shopingcart.R
import com.surajrathod.shopingcart.repo.CartRepo
import com.surajrathod.shopingcart.repo.ShopRepo
import com.surajrathod.shopingcart.viewmodels.ShopViewModel
import com.surajrathod.shopingcart.viewmodels.ShopViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    lateinit var shopViewModel: ShopViewModel
    val shopRepo = ShopRepo()
    val cartRepo = CartRepo()
    var cartQty = 0

    var isRead = false
    var isWrite = false

    lateinit var permissionLauncher : ActivityResultLauncher<Array<String>>


    val TAG = "Suraj"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            isRead = it[Manifest.permission.READ_EXTERNAL_STORAGE] ?: isRead
            isWrite = it[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: isWrite
        }

        requestPermission()
        navController = Navigation.findNavController(this,R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this,navController)

        shopViewModel = ViewModelProvider(this,ShopViewModelFactory(shopRepo,cartRepo)).get(ShopViewModel::class.java)

//        shopViewModel.getTotalPrice()?.observe(this,{
//            Toast.makeText(this@MainActivity,"Total is $it",Toast.LENGTH_SHORT).show()
//        })



        //just press alt+enter on above code , so lamda will be out of parenthis
        shopViewModel.getCart().observe(this) {

            var q = 0
            it?.forEach {
                q += it.qty
            }

            cartQty = q
            invalidateOptionsMenu()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //To this thing work , the cart fragment id must be same as cart menu item id !!
        NavigationUI.onNavDestinationSelected(item, navController)
        return super.onOptionsItemSelected(item)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        //TODO : If some error occur try to comment below code and return true
        val menuItem = menu?.findItem(R.id.cartFragment)
        val actionView = menuItem?.actionView       //if we used android:actionView then , it will give it null
        val cartBadgeTextView = actionView?.findViewById<TextView>(R.id.txtBadgeCount)
        cartBadgeTextView?.setText(cartQty.toString())
        cartBadgeTextView?.visibility = if(cartQty==0) {
            View.GONE
        }else{
            View.VISIBLE
        }

        actionView?.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }



        return super.onCreateOptionsMenu(menu)
    }



    fun requestPermission(){

        //check permission already granted or not
        isRead = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        isWrite = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        var permissionRequest : MutableList<String> = ArrayList()

        if(!isRead){
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if(!isWrite){
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if(permissionRequest.isNotEmpty()){
            //request permission
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }


    }
}