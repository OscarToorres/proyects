package com.git.alquilalotodo.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.git.alquilalotodo.R
import com.git.alquilalotodo.adapter.ProductosAdapter
import com.git.alquilalotodo.databinding.FragmentHomeBinding
import com.git.alquilalotodo.provider.ProductosProvider
import com.git.alquilalotodo.ui.activities.ContactosActivity
import com.git.alquilalotodo.ui.activities.ProductItemHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =  inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.bind(view)

        if (auth.currentUser == null){
            binding.homeMessages.visibility = View.GONE
        }

        return view
    }

    private lateinit var recycleView : RecyclerView
    private lateinit var productosAdapter: ProductosAdapter
    private var productosProvider: ProductosProvider = ProductosProvider()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycleView = view.findViewById(R.id.generic_recycleview)
        recycleView.setHasFixedSize(true)
        recycleView.layoutManager = GridLayoutManager(view.context, 2)
        productosProvider.setAllProducts() { productList ->
            productosAdapter = ProductosAdapter(productList)
            recycleView.adapter = productosAdapter

            productosAdapter.onItemClick = {producto,position ->
                val intent = Intent(view.context, ProductItemHomeActivity::class.java)
                // val user = auth.currentUser
                intent.putExtra("productoItem",producto)
                intent.putExtra("productoPosicion", position)
                startActivity(intent)
            }
        }

        binding.homeMessages.setOnClickListener{
            startActivity(Intent(view.context,ContactosActivity::class.java))
        }

        binding.homeSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                query?.let { searchText ->
                    recycleView = view.findViewById(R.id.generic_recycleview)
                    recycleView.setHasFixedSize(true)
                    recycleView.layoutManager = GridLayoutManager(view.context, 2)
                    productosProvider.setAllProductsWithFilter(query) { productList ->
                        productosAdapter = ProductosAdapter(productList)
                        recycleView.adapter = productosAdapter

                        productosAdapter.onItemClick = {producto,position ->
                            val intent = Intent(view.context, ProductItemHomeActivity::class.java)
                            // val user = auth.currentUser
                            intent.putExtra("productoItem",producto)
                            intent.putExtra("productoPosicion", position)
                            startActivity(intent)
                        }
                    }
                }

                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {

                if (query.isNullOrBlank()) {
                    recycleView = view.findViewById(R.id.generic_recycleview)
                    recycleView.setHasFixedSize(true)
                    recycleView.layoutManager = GridLayoutManager(view.context, 2)
                    productosProvider.setAllProducts() { productList ->
                        productosAdapter = ProductosAdapter(productList)
                        recycleView.adapter = productosAdapter

                        productosAdapter.onItemClick = { producto, position ->
                            val intent = Intent(view.context, ProductItemHomeActivity::class.java)
                            // val user = auth.currentUser
                            intent.putExtra("productoItem", producto)
                            intent.putExtra("productoPosicion", position)
                            startActivity(intent)
                        }
                    }
                }

                return false
            }


        })

    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}