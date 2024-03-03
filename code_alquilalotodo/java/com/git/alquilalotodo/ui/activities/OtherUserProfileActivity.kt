package com.git.alquilalotodo.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.git.alquilalotodo.R
import com.git.alquilalotodo.adapter.ViewPagerAdapter
import com.git.alquilalotodo.databinding.ActivityOtherUserProfileBinding
import com.git.alquilalotodo.objects.Producto
import com.git.alquilalotodo.objects.User
import com.git.alquilalotodo.ui.fragment.perfil.ProfileProductsFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OtherUserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtherUserProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var viewPager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val userProfId = intent.getStringExtra("userId").toString()
        var listProductos = ArrayList<Producto>()
        //Cargar datos del usuario en la vista
        //1º Cargar los datos de la bd a la vista (algunos no seran modificables)
        var user = User()
        val userDoc = db.collection("usuarios").document(userProfId)
        userDoc.get().addOnSuccessListener {
            user.nombre = it.getString("nombre") ?: ""
            user.apellidos = it.getString("apellidos") ?: ""
            user.localizacion = it.getString("localizacion") ?: ""
            user.descripcion = it.getString("descripcion") ?: ""
            user.tipoCuenta = it.getString("tipoCuenta") ?: ""
            user.correoContacto = it.getString("correoContacto") ?: ""
            user.horasActivo = it.getString("horasActivo") ?: ""
            listProductos = it.get("listaProductos") as ArrayList<Producto>
            val imageProfile = it.getString("imagenPerfil")
            if (!imageProfile.isNullOrBlank()){
                Glide.with(this).load(imageProfile.toString()).into(binding.otherProfBody.imgUsuario)
            }

            binding.otherProfBody.nombre.text = user.nombre + " " + user.apellidos
            binding.otherProfBody.descripcion.text = user.descripcion
            binding.otherProfBody.localizacion.text =user.localizacion
            binding.otherProfBody.correoContacto.text = user.correoContacto
            binding.otherProfBody.horasActivo.text = user.horasActivo

        }

        binding.otherProfGoback.setOnClickListener {
            finish()
        }

        setupTabLayout()

        val fragmentManager = supportFragmentManager
        viewPager = binding.profViewPager
        val adapter = ViewPagerAdapter(fragmentManager,binding.profTabLayout.tabCount,userProfId) // Pasar childFragmentManager al adaptador
        viewPager.adapter = adapter





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
}