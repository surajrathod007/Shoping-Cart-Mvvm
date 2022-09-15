package com.surajrathod.shopingcart.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.itextpdf.commons.utils.DateTimeUtil
import com.itextpdf.kernel.colors.Color
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Text
import com.surajrathod.shopingcart.R
import com.surajrathod.shopingcart.adapters.CartAdapter
import com.surajrathod.shopingcart.databinding.FragmentCartBinding
import com.surajrathod.shopingcart.databinding.FragmentShopBinding
import com.surajrathod.shopingcart.model.CartItem
import com.surajrathod.shopingcart.repo.CartRepo

import com.surajrathod.shopingcart.repo.ShopRepo
import com.surajrathod.shopingcart.viewmodels.ShopViewModel
import com.surajrathod.shopingcart.viewmodels.ShopViewModelFactory
import java.io.File
import java.io.FileOutputStream


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

        binding.generateBill.setOnClickListener {
            generatePdf()
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

    fun generatePdf(){

        try{

            var lst = viewModel.getCart().value

            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
            val file = File(path,"mypdf.pdf")
            val output = FileOutputStream(file)

            val writer = PdfWriter(file)
            val pdfDocument = PdfDocument(writer)
            val document = Document(pdfDocument)


            val paragraph = Paragraph("Hey there ! Mann Sign\n\nInvoice")
            //paragraph.add(DateTimeUtil.getCurrentTimeDate().toString())
            document.add(paragraph)


            val table = Table(3)
            table.addCell("Product")
            table.addCell("Qty")
            table.addCell("Price ")

            lst!!.forEach {
                table.addCell(it.product.name)
                table.addCell(it.qty.toString())
                table.addCell((it.product.price*it.qty).toString())
            }

            table.addCell("")
            val total = viewModel.getTotalPrice()?.value ?: 0.0
            table.addCell("Sub Total : ")
            table.addCell("$total")

            table.addCell("")
            table.addCell("Gst : 18% ")
            table.addCell("-${(total*18)/100}")
            table.addCell("")
            table.addCell("Grand Total : ")
            table.addCell("$${total-(total*18)/100}")



            document.add(table)
            document.add(Paragraph("\n\nBill Genereted : "+ DateTimeUtil.getCurrentTimeDate().toString()))

            document.close()
            Toast.makeText(this.requireContext(),"Pdf Created",Toast.LENGTH_SHORT).show()
            openFile(file,path)

        }catch ( e : Exception){
            Toast.makeText(this.requireContext(),e.message,Toast.LENGTH_SHORT).show()
        }

    }

    fun openFile(file : File,path : String){
        val intent = Intent(Intent.ACTION_VIEW)

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
            val uri = FileProvider.getUriForFile(this.requireActivity(),this.requireActivity().packageName+".provider",file)
            intent.setData(uri)
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        }else{
            intent.setDataAndType(Uri.parse(path),"application/pdf")
            val i = Intent.createChooser(intent,"Open File With")
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }
    }


}