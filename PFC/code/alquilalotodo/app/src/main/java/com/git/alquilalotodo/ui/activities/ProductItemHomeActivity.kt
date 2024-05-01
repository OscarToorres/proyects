package com.git.alquilalotodo.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.git.alquilalotodo.R
import com.git.alquilalotodo.databinding.ActivityProductItemBinding
import com.git.alquilalotodo.databinding.ActivityProductItemHomeBinding
import com.git.alquilalotodo.objects.Producto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProductItemHomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProductItemHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductItemHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()
        val prod = intent.getParcelableExtra<Producto>("productoItem")
        val posicion = intent.getIntExtra("productoPosicion", -1)

        if (prod != null) {
            Glide.with(this).load(prod.imagenURL).into(binding.prodItemImg)
            binding.prodItemNombre.text = prod.nombre
            binding.prodItemPrecio.text = prod.precio
            binding.prodItemCaracteristicas.text = prod.caracteristicas
            binding.prodItemDesc.text = prod.descripcion
            binding.prodItemCondiciones.text = prod.condiciones

            db.collection("usuarios").document(prod.userId).get().addOnSuccessListener {
                val nombre = it.getString("nombre")?: "Usuario"
                val apellidos = it.getString("apellidos")?: "Sin Apellidos"

                if ("Usuario".equals(nombre) && "Sin Apellidos".equals(apellidos)){
                    binding.prodItemNombreUsuario.text = nombre + " $apellidos"
                } else {
                    if ("Usuario".equals(nombre) && !"Sin Apellidos".equals(apellidos)){
                        binding.prodItemNombreUsuario.text = apellidos
                    }
                    if (!"Usuario".equals(nombre) && "Sin Apellidos".equals(apellidos)){
                        binding.prodItemNombreUsuario.text = nombre
                    } else {
                        binding.prodItemNombreUsuario.text = nombre + " $apellidos"
                    }
                }

                val userImgProfile = it.getString("imagenPerfil")
                if (!userImgProfile.isNullOrBlank()){
                    Glide.with(this).load(userImgProfile.toString()).into(binding.prodImgUsuario)
                }
            }
        }

        binding.prodItemGoback.setOnClickListener{
            finish()
        }

        binding.profUsuario.setOnClickListener {
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(150)
                .withEndAction {
                    // Restaurar el tamaño original después de la animación
                    it.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(150)
                        .start()
                }.start()
            val profile = Intent(this,OtherUserProfileActivity::class.java)
            profile.putExtra("userId",prod?.userId)
            startActivity(profile)
        }
    }
}