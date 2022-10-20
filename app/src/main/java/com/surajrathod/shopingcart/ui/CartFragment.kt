package com.surajrathod.shopingcart.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.itextpdf.commons.utils.DateTimeUtil
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.Color
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.Style
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.TextAlignment
import com.surajrathod.shopingcart.R
import com.surajrathod.shopingcart.adapters.CartAdapter
import com.surajrathod.shopingcart.databinding.FragmentCartBinding
import com.surajrathod.shopingcart.databinding.FragmentShopBinding
import com.surajrathod.shopingcart.model.CartItem
import com.surajrathod.shopingcart.repo.CartRepo

import com.surajrathod.shopingcart.repo.ShopRepo
import com.surajrathod.shopingcart.viewmodels.ShopViewModel
import com.surajrathod.shopingcart.viewmodels.ShopViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.util.*


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

    @RequiresApi(Build.VERSION_CODES.O)
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
            makeInvoice()
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
            val file = File(path,"mann_invoice${System.currentTimeMillis()}.pdf")
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


    @RequiresApi(Build.VERSION_CODES.O)
    fun makeInvoice(){

        try{

            var lst = viewModel.getCart().value

            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
            val file = File(path,"mann_invoice${System.currentTimeMillis()}.pdf")
            val output = FileOutputStream(file)

            val writer = PdfWriter(file)
            val pdfDocument = PdfDocument(writer)
            val document = Document(pdfDocument)


            //header
            val headerImg = requireContext().getDrawable(R.drawable.invoice_header)
            val bitmap = (headerImg as BitmapDrawable).bitmap
            val opstream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG,100,opstream)
            val bitmapdata = opstream.toByteArray()

            val img = ImageDataFactory.create(bitmapdata)
            val myheader = Image(img)
            document.add(myheader)


            //first table
            val c : FloatArray = floatArrayOf(220F, 220F, 200F, 180F)
            val table1 = Table(c)


            // row 1
            table1.addCell(Cell().add(Paragraph("Bill to Party ").setFontSize(10.0f)).setBold())
            table1.addCell(Cell().add(Paragraph("Ship to Party ").setFontSize(10.0f)).setBold())
            table1.addCell(Cell().add(Paragraph("Date : ").setFontSize(10.0f)).setBold())
            table1.addCell(Cell().add(Paragraph(LocalDate.now().toString()).setFontSize(10.0f).setBold()))

            //row 2
            table1.addCell(Cell(4,0).add(Paragraph("857,indiranagar -2 ").setFontSize(8.0f)))
            table1.addCell(Cell(4,0).add(Paragraph("11, My Queen").setFontSize(8.0f)))
            table1.addCell(Cell().add(Paragraph("Invoice No : ").setFontSize(10.0f)))
            table1.addCell(Cell().add(Paragraph("inv1000").setFontSize(10.0f)))

            //row 3
            //table1.addCell(Cell().add(Paragraph("")))
            //table1.addCell(Cell().add(Paragraph("")))
            table1.addCell(Cell().add(Paragraph("Buyer's Order No. :").setFontSize(10.0f)))
            table1.addCell(Cell().add(Paragraph("order97777").setFontSize(10.0f)))

            //row 4
            //table1.addCell(Cell().add(Paragraph("")))
            //table1.addCell(Cell().add(Paragraph("")))
            table1.addCell(Cell().add(Paragraph("Order Date : ").setFontSize(10.0f)))
            table1.addCell(Cell().add(Paragraph("${LocalDate.now().plusDays(7)}").setFontSize(10.0f)))

            //row 5
            //table1.addCell(Cell().add(Paragraph("")))
            //table1.addCell(Cell().add(Paragraph("")))
            table1.addCell(Cell().add(Paragraph("Status Code : ").setFontSize(10.0f)))
            table1.addCell(Cell().add(Paragraph("24").setFontSize(10.0f)))

            //row 6
            table1.addCell(Cell().add(Paragraph("State Code : ").setFontSize(8.0f).setBold()))
            table1.addCell(Cell().add(Paragraph("State Code : ").setFontSize(8.0f).setBold()))
            table1.addCell(Cell(0,2).add(Paragraph("COMPANY GSTIN NO : ").setFontSize(8.0f).setBold()).setTextAlignment(TextAlignment.CENTER))
            //table1.addCell(Cell().add(Paragraph("")))

            //row 7
            table1.addCell(Cell().add(Paragraph("GSTIN NO : <add here>").setFontSize(8.0f).setBold()))
            table1.addCell(Cell().add(Paragraph("GSTIN NO : <add here>").setFontSize(8.0f).setBold()))
            table1.addCell(Cell(0,2).add(Paragraph("24BENPP0006B1Z4").setFontSize(8.0f).setBold()).setTextAlignment(TextAlignment.CENTER))
            //table1.addCell(Cell().add(Paragraph("")))







            document.add(table1)
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