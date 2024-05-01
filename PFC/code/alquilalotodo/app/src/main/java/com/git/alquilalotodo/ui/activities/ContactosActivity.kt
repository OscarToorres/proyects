package com.git.alquilalotodo.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.git.alquilalotodo.adapter.ContactosAdapter
import com.git.alquilalotodo.databinding.ActivityContactosBinding
import com.git.alquilalotodo.provider.ContactosProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ContactosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactosBinding
    private lateinit var dbRealTime : DatabaseReference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var contactosAdappter : ContactosAdapter

    private val contactosProvider = ContactosProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        dbRealTime = FirebaseDatabase.getInstance().getReference()

        db.collection("usuarios").document(auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val imagenPerfil = it.getString("imagenPerfil")?: ""

                if (!imagenPerfil.isNullOrBlank()){
                    Picasso.get().load(imagenPerfil).into(binding.userProfilePicture)
                }

            }

        binding.contactoGoBack.setOnClickListener {
            finish()
        }

        //Cargar el recycleView con los contactos del usuario
        println("Llega al metodo ")
        loadRecycleView()

    }

    private fun loadRecycleView (){
        println("Llega el recycleView")
        val recycleView = binding.messagesRecycleView
        recycleView.setHasFixedSize(true)
        recycleView.layoutManager = LinearLayoutManager(this)

        contactosProvider.setUserContacts(auth.currentUser!!.uid) { contacList ->

            println("Lingitud de la lista " + contacList.size)
            contactosAdappter = ContactosAdapter(contacList,this)
            recycleView.adapter = contactosAdappter
        }
    }

    override fun onResume() {
        super.onResume()

        loadRecycleView()
    }
}