package com.git.alquilalotodo.ui.fragment.perfil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.git.alquilalotodo.R
import com.git.alquilalotodo.adapter.ProductosAdapter
import com.git.alquilalotodo.provider.ProductosProvider
import com.git.alquilalotodo.ui.activities.ProductItemActivity
import com.git.alquilalotodo.ui.activities.ProductItemHomeActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.NonDisposableHandle.parent

class ProfileProductsFragment () : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var uid : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        arguments?.let {
        }
    }

    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_products, container, false)

        return view
    }

    private lateinit var recycleView: RecyclerView
    private lateinit var productosAdapter: ProductosAdapter
    private val productosProvider : ProductosProvider = ProductosProvider()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uid = arguments?.getString("userId")

        if (uid.isNullOrBlank()){
            uid = auth.currentUser!!.uid
        }

        //Cargar los productos del usuario en su perfil
        loadProductsData()
    }

    fun setUserId(userId: String) {
        this.uid = userId
    }

    override fun onResume() {
        super.onResume()
        loadProductsData()
    }
    private fun loadProductsData(){
        //Cargar los productos del usuario en su perfil
        recycleView = view.findViewById(R.id.generic_recycleview)
        recycleView.setHasFixedSize(true)
        recycleView.layoutManager = GridLayoutManager(view.context, 2)
        productosProvider.setUserProducts(uid!!) { productList ->
            println("Longitude de la lista de productos " + productList.size)
            productosAdapter = ProductosAdapter(productList)
            recycleView.adapter = productosAdapter

            productosAdapter.onItemClick = {producto,position ->
                if (arguments?.getString("userId").isNullOrBlank()) {
                    val intent = Intent(view.context, ProductItemActivity::class.java)
                    // val user = auth.currentUser
                    intent.putExtra("productoItem",producto)
                    intent.putExtra("productoPosicion", position)
                    startActivity(intent)
                } else {
                    val intent = Intent(view.context, ProductItemHomeActivity::class.java)
                    // val user = auth.currentUser
                    intent.putExtra("productoItem", producto)
                    intent.putExtra("productoPosicion", position)
                    startActivity(intent)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ProfileProductsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}