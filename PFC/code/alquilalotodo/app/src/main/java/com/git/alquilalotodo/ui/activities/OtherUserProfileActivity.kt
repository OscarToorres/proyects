package com.git.alquilalotodo.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.git.alquilalotodo.R
import com.git.alquilalotodo.adapter.ViewPagerAdapter
import com.git.alquilalotodo.databinding.ActivityOtherUserProfileBinding
import com.git.alquilalotodo.objects.ContactoAgregado
import com.git.alquilalotodo.objects.Producto
import com.git.alquilalotodo.objects.User
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
        val userDoc = db.collection("usuarios").document(userProfId)
        userDoc.get().addOnSuccessListener {

            val nombre = it.getString("nombre")?:"Usuario"
            val apellidos = it.getString("apellidos")?:"Sin Apellidos"
            val localizacion = it.getString("localizacion")?:"Sin localizacion"
            val descripcion = it.getString("descripcion")?:"Sin descripcion"
            val correoContacto = it.getString("correoContacto")?:"Sin correo de contacto"
            val horasActivo = it.getString("horasActivo")?:"Sin especificar"
            val imageProfile = it.getString("imagenPerfil")
            val numeroProductos = it.get("productCount")?: "0"

            if (!imageProfile.isNullOrBlank()){
                Glide.with(this).load(imageProfile.toString()).into(binding.otherProfBody.imgUsuario)
            }

            if ("Usuario".equals(nombre) && "Sin Apellidos".equals(apellidos)){
                binding.otherProfBody.nombre.text = nombre + " $apellidos"
            } else {
                if ("Usuario".equals(nombre) && !"Sin Apellidos".equals(apellidos)){
                    binding.otherProfBody.nombre.text = apellidos
                }
                if (!"Usuario".equals(nombre) && "Sin Apellidos".equals(apellidos)){
                    binding.otherProfBody.nombre.text = nombre
                } else {
                    binding.otherProfBody.nombre.text = nombre + " $apellidos"
                }
            }

            binding.otherProfBody.descripcion.text = descripcion
            binding.otherProfBody.localizacion.text =localizacion
            binding.otherProfBody.correoContacto.text = correoContacto
            binding.otherProfBody.horasActivo.text = horasActivo
            binding.otherProfBody.enAlquiler.text = numeroProductos.toString()

        }

        binding.otherProfGoback.setOnClickListener {
            finish()
        }

        if (auth.currentUser == null){
            binding.otherProfMsg.visibility = View.GONE
        }

        binding.otherProfMsg.setOnClickListener {
            val currentUserDoc = db.collection("usuarios").document(auth.currentUser!!.uid)
            currentUserDoc.get().addOnSuccessListener {
                var listContactos: ArrayList<ContactoAgregado>
                val listContactosMap = it.get("listaChatUsuarios")
                if (listContactosMap != null) {
                    listContactos = listContactosMap as ArrayList<ContactoAgregado>
                } else {
                    listContactos = ArrayList()
                }

                var validator = false
                for (i in 0 until listContactos.size) {

                    //Recorrer la lista de contactos para obtener el uid
                    val contactoMap = listContactos[i] as HashMap<String, Any>
                    val contactoUid = contactoMap["contactoUid"] as String

                    if (contactoUid.equals(userProfId) || userProfId.equals(auth.currentUser!!.uid)) {
                        validator = true
                    }
                }
                if (validator == false) {
                    listContactos.add(ContactoAgregado(userProfId, ""))

                    val listOfMessagesMap = hashMapOf<String, Any>(
                        "listaChatUsuarios" to listContactos
                    )

                    currentUserDoc.update(listOfMessagesMap).addOnSuccessListener {

                        startActivity(Intent(this, ContactosActivity::class.java))
                        finish()
                        val otherUserDoc = db.collection("usuarios").document(userProfId)
                        otherUserDoc.get().addOnSuccessListener {
                            var otherlistContactos: ArrayList<ContactoAgregado>
                            val otherlistContactosMap = it.get("listaChatUsuarios")
                            if (otherlistContactosMap != null) {
                                otherlistContactos =
                                    otherlistContactosMap as ArrayList<ContactoAgregado>
                            } else {
                                otherlistContactos = ArrayList()
                            }

                            otherlistContactos.add(ContactoAgregado(auth.currentUser!!.uid, ""))

                            val otherListOfMessagesMap = hashMapOf<String, Any>(
                                "listaChatUsuarios" to otherlistContactos
                            )

                            otherUserDoc.update(otherListOfMessagesMap)
                        }
                    }
                } else {
                    startActivity(Intent(this, ContactosActivity::class.java))
                    finish()
                }
            }
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