package com.git.alquilalotodo.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.git.alquilalotodo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.git.alquilalotodo.adapter.CategoriaAdapter
import com.git.alquilalotodo.databinding.FragmentCategoriesBinding
import com.git.alquilalotodo.objects.Categories
import com.git.alquilalotodo.objects.ItemCategoria
import com.git.alquilalotodo.ui.activities.RentProductoActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class CategoriesFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CategoriesFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    /******************************/
    /********** onCreate **********/
    /******************************/

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage : FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        arguments?.let {
        }
    }
    /**********************************/
    /********** onCreateView **********/
    /**********************************/

    private lateinit var view : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Cargar el layout para este fragment
        view = inflater.inflate(R.layout.fragment_categories, container, false)
        _binding = FragmentCategoriesBinding.bind(view)

        return view;

    }

    /***********************************/
    /********** onViewCreated **********/
    /***********************************/

    private lateinit var recycleView: RecyclerView
    private lateinit var categoriaAdapter: CategoriaAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Cargar los productos del usuario en su perfil
        recycleView = view.findViewById(R.id.recycleview_categories)
        recycleView.setHasFixedSize(true)
        recycleView.layoutManager = LinearLayoutManager(view.context)

        val listaCategorias = listOf(
            ItemCategoria("Videojuegos", Categories.VIDEOJUEGOS),
            ItemCategoria("Vehiculos", Categories.VEHICULOS),
            ItemCategoria("Prendas", Categories.PRENDAS),
            ItemCategoria("Jardineria", Categories.JARDINERIA),
            ItemCategoria("Instrumentos", Categories.INSTRUMENTOS),
            ItemCategoria("Informatica", Categories.INFORMATICA),
            ItemCategoria("Accesorios", Categories.ACCESORIOS),
            ItemCategoria("Herramientas", Categories.HERRAMIENTAS)
        )

        categoriaAdapter = CategoriaAdapter(listaCategorias)
        recycleView.adapter = categoriaAdapter
        categoriaAdapter.onItemClick = {
            val intent = Intent(view.context, RentProductoActivity::class.java)
            // val user = auth.currentUser
            intent.putExtra("nombreCategoria",it)
            startActivity(intent)
        }

    }

    /***********************************/
    /********** onDestroyView **********/
    /***********************************/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}