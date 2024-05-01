package com.git.alquilalotodo.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.git.alquilalotodo.R
import com.git.alquilalotodo.adapter.ImageAdapter
import com.git.alquilalotodo.adapter.ImageAdapterURL
import com.git.alquilalotodo.controler.ProductosControler
import com.git.alquilalotodo.databinding.ActivityRentProductoBinding
import com.git.alquilalotodo.objects.Producto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class RentProductoEditActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRentProductoBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage : FirebaseStorage

    private lateinit var gridView: GridView
    private lateinit var imageAdapterUrl: ImageAdapterURL
    private lateinit var imageAdapter: ImageAdapter
    private var imageUris: ArrayList<Uri> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRentProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        val prod = intent.getParcelableExtra<Producto>("productoEdit")
        val posicion = intent.getIntExtra("productoPositionEdit", -1)

        binding.rentProdTitle.setText("Editar producto")

        //Cargar en la vista
        binding.rentProdName.setText(prod?.nombre)
        binding.rentProdDesc.setText(prod?.descripcion)
        binding.rentProdCaracteristicas.setText(prod?.caracteristicas)
        binding.rentProdCondiciones.setText(prod?.condiciones)
        binding.rentProdPrecio.setText(prod?.precio)

        binding.rentProgressBar.visibility = View.INVISIBLE
        // Se crea el estilo por defecto del selector de imagenes
        gridView = binding.rentGridview
        imageAdapterUrl = ImageAdapterURL(prod!!.listImagenes!!)
        gridView.adapter = imageAdapterUrl

        gridView.setOnItemClickListener { parent, view, position, id ->
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            openImagePicker()
        }

        binding.rentProdGoback.setOnClickListener {
            finish()
        }

        val options = arrayOf("Hora","Dia","Semana","Mes")
        var selectedItem = ""
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.rentSpinner.adapter = adapter

        binding.rentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedItem = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //Si no selecciona nada
            }
        }

        /*
        Se cargan los datos de la vista, comprueban los campos obligatorios y guarda los datos
        en la bd
        */
        binding.rentProdConfirm.setOnClickListener {

            val productName: String = binding.rentProdName.text.toString()
            val productDesc: String = binding.rentProdDesc.text.toString()
            val productCond: String = binding.rentProdCondiciones.text.toString()
            val productCaract: String = binding.rentProdCaracteristicas.text.toString()
            val productCategoria: String = binding.rentProdCateg.text.toString()
            val productPrecio: String = binding.rentProdPrecio.text.toString() + " €/" + selectedItem

            val prodContr = ProductosControler(auth, storage, db,this,this)

            if (checkEmpty(productName, productDesc, productPrecio)) {
                    binding.rentProdConfirm.visibility = View.GONE
                    binding.rentProgressBar.visibility = View.VISIBLE
                    prodContr.actualizarProductosFirestore(productName, productDesc,productCaract,productPrecio, productCond,productCategoria,imagenesSelected,posicion,this)

            } else {
                Toast.makeText(this,"Tienes campos obligatorios vacios", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private var imagenesSelected : ArrayList<Uri> = ArrayList()
    /**
     * Se verifica si se han seleccionado varios archivos, si se seleccionaron, se suben a la bd
     * y se actualizan en la vista. Cuando se seleccionan las imagenes se guardan las URIs que son
     * la ruta del dispositivo hacia esa imagen. Lo que almacenammos en la bd no es la imagen en si,
     * sino la ruta de la imagen.
     */
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intentData: Intent? = result.data
            val selectedUris: ArrayList<Uri>? = intentData?.clipData?.let { clipData ->
                (0 until clipData.itemCount).map { clipData.getItemAt(it).uri }.toCollection(ArrayList())
            } ?: arrayListOf(intentData?.data).filterNotNull().toCollection(ArrayList())

            if (selectedUris?.isNotEmpty() == true) {

                var selectedUrisView: ArrayList<Uri> = selectedUris

                /*
                if (selectedUris.size < 8) {
                    val defImg = createDefaultGridView(view.context).get(0)
                    while (selectedUrisView.size < 8) {
                        selectedUrisView.add(defImg)
                    }
                }
                 */
                imagenesSelected = selectedUris
                imageAdapter = ImageAdapter(selectedUris)
                gridView.adapter = imageAdapter

            }
        }
    }

    /**
     * Se inicia la actividad para la seleccion de imagenes. Se le indica el tipo de archivos y
     * que se pueden seleccionar varios antes de lanzarla
     */
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*" // Tipos de archivo permitidos: imágenes
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // Permitir selección múltiple
        }
        imagePickerLauncher.launch(intent)
    }

    /**
     * @author Comprueba que los campos obligatorios para añadir el producto no esten vacios
     * @param email Email de registro de usuario
     * @param password Contraseña del usuario
     * @param password2 Confirmacion de contrasela del usuario
     * @return Devuelve true si no estan vacios y false si alguno lo esta
     */
    private fun checkEmpty(nombre:String, description:String,precio: String): Boolean {
        return (nombre.isNotEmpty() && description.isNotEmpty())
    }

}
