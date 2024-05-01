package com.git.alquilalotodo.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.git.alquilalotodo.R
import com.git.alquilalotodo.controler.ProductosControler
import com.git.alquilalotodo.databinding.ActivityProductItemBinding
import com.git.alquilalotodo.objects.Producto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProductItemActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProductItemBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage : FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        val prod = intent.getParcelableExtra<Producto>("productoItem")
        val posicion = intent.getIntExtra("productoPosicion", -1)

        if (prod != null) {
            Glide.with(this).load(prod.imagenURL).into(binding.prodItemImg)
            binding.prodItemNombre.text = prod.nombre
            binding.prodItemPrecio.text = prod.precio
            binding.prodItemCaracteristicas.text = prod.caracteristicas
            binding.prodItemDesc.text = prod.descripcion
            binding.prodItemCondiciones.text = prod.condiciones
        }

        binding.prodItemGoback.setOnClickListener{
            finish()
        }

        var productosControler = ProductosControler(auth,storage,db,this,this)


        binding.prodItemDelete.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Eliminar producto")
            builder.setMessage("¿Estás seguro de que quieres eliminar este producto?")
            builder.setPositiveButton("Eliminar", null)
            builder.setNegativeButton("Cancelar", null)

            val dialog = builder.create()

            // Asigna el estilo personalizado a los botones
            dialog.setOnShowListener {
                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorCancel))
                positiveButton.setOnClickListener {
                    // Aquí puedes realizar las acciones necesarias para cerrar la sesión
                    productosControler.eliminarProductoFirestore(posicion,this)
                    dialog.dismiss()
                }

                val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                negativeButton.setTextColor(ContextCompat.getColor(this, R.color.black))
                negativeButton.setOnClickListener {
                    dialog.dismiss()
                }
            }
            dialog.show()
        }

        binding.prodItemEdit.setOnClickListener {
            val intent = Intent(this,RentProductoEditActivity::class.java)
            intent.putExtra("productoEdit",prod)
            intent.putExtra("productoPositionEdit",posicion)
            startActivity(intent)
        }
    }
}