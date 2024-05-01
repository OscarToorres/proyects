package com.git.alquilalotodo.ui.fragment.perfil

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.widget.NestedScrollView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.git.alquilalotodo.R
import com.git.alquilalotodo.adapter.ViewPagerAdapter
import com.git.alquilalotodo.databinding.FragmentProfileBinding
import com.git.alquilalotodo.ui.activities.ConfiguracionUsuarioActivity
import com.git.alquilalotodo.ui.activities.GetStartedActivity
import com.git.alquilalotodo.viewmodel.MainViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {

    /********** OBJETOS DE CLASE **********/
    private lateinit var  db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var recivedEmail: String? = null
    companion object {

        private const val EMAIL_BUNDLE = "email"

        fun newInstance(email: String) : ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle().apply {
                putString(EMAIL_BUNDLE, email)
            }
            fragment.arguments = args
            return fragment
        }
    }
    /*******************************/
    /********** onCreated **********/
    /*******************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        arguments?.let {args ->
            recivedEmail = args.getString(EMAIL_BUNDLE)
        }
    }
    /**********************************/
    /********** onCreateView **********/
    /**********************************/


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Cargar la vista
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = _binding?.root

        //Setear toolBar al fragment
        val toolbar: Toolbar = binding.profToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        //Crear el TabLayout
        setupTabLayout()
        setupViewPager()

        val viewPager: ViewPager = binding.profViewPager

        return view
    }

    private fun setupTabLayout() {
        binding.profTabLayout.apply {
            addTab(this.newTab().setIcon(R.drawable.ic_photo_library_24))
            addTab(this.newTab().setIcon(R.drawable.ic_star_rate_24))

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let {
                        binding.profViewPager.currentItem = it
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }
    }

    private fun setupViewPager() {
        binding.profViewPager.apply {
            adapter = ViewPagerAdapter(childFragmentManager, binding.profTabLayout.tabCount,"")
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.profTabLayout))
        }
    }


    /***********************************/
    /********** onViewCreated **********/
    /***********************************/


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Cargar datos del usuario en la vista
        val userId = auth.currentUser?.uid
        if (userId != null){
            loadData(userId)
        }

            //Cerrar sesion
            binding.profLogout.setOnClickListener {
                val builder = AlertDialog.Builder(view.context)
                builder.setTitle("Cerrar sesión")
                builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
                builder.setPositiveButton("Aceptar", null)
                builder.setNegativeButton("Cancelar", null)

                val dialog = builder.create()

                // Asigna el estilo personalizado a los botones
                dialog.setOnShowListener {
                    val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    positiveButton.setTextColor(ContextCompat.getColor(view.context, R.color.colorAccept))
                    positiveButton.setOnClickListener {
                        // Aquí puedes realizar las acciones necesarias para cerrar la sesión
                        signOut()
                        dialog.dismiss()
                    }

                    val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    negativeButton.setTextColor(ContextCompat.getColor(view.context, R.color.colorCancel))
                    negativeButton.setOnClickListener {
                        dialog.dismiss()
                    }
                }

                dialog.show()
            }

            //Entrar en editar perfil
            binding.profEdit.setOnClickListener {
                startActivity(Intent(view.context,ConfiguracionUsuarioActivity::class.java))
            }
    }

    override fun onResume() {
        super.onResume()
        val userId = auth.currentUser?.uid
        if (userId != null){
            loadData(userId)
        }
    }

    /***********************************/
    /********** FUNCIONES **********/
    /***********************************/

    /**
     * Cierra la sesion actual del usuario
     */
    private fun signOut() {
        auth.signOut()
        // Redirige al usuario a la pantalla de inicio de sesión
        val intent = Intent(activity, GetStartedActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun loadData(userId:String) {
        val userDoc = db.collection("usuarios").document(userId)
        userDoc.get().addOnSuccessListener {
            val nombre = it.getString("nombre")?:"Usuario"
            val apellidos = it.getString("apellidos")?:"Sin Apellidos"
            val localizacion = it.getString("localizacion")?:"Sin localizacion"
            val descripcion = it.getString("descripcion")?:"Sin descripcion"
            val correoContacto = it.getString("correoContacto")?:"Sin correo de contacto"
            val horasActivo = it.getString("horasActivo")?:"Sin especificar"
            val imageProfile = it.getString("imagenPerfil")
            val numeroProductos = it.get("productCount")?: "0"

            if (!imageProfile.isNullOrBlank()) {
                Glide.with(this).load(imageProfile).into(binding.profBody.imgUsuario)
            }

            if ("Usuario".equals(nombre) && "Sin Apellidos".equals(apellidos)){
                binding.profBody.nombre.text = nombre + " $apellidos"
            } else {
                if ("Usuario".equals(nombre) && !"Sin Apellidos".equals(apellidos)){
                        binding.profBody.nombre.text = apellidos
                    }
                if (!"Usuario".equals(nombre) && "Sin Apellidos".equals(apellidos)){
                    binding.profBody.nombre.text = nombre
                } else {
                    binding.profBody.nombre.text = nombre + " $apellidos"
                }
            }

            binding.profBody.descripcion.text = descripcion
            binding.profBody.localizacion.text = localizacion
            binding.profBody.correoContacto.text = correoContacto
            binding.profBody.horasActivo.text = horasActivo
            binding.profBody.enAlquiler.text = numeroProductos.toString()
        }
    }
}