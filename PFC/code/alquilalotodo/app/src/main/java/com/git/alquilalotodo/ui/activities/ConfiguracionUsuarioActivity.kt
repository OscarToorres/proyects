package com.git.alquilalotodo.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.git.alquilalotodo.R
import com.git.alquilalotodo.controler.MainFragmentControlerActivity
import com.git.alquilalotodo.databinding.ActivityConfiguracionUsuarioBinding
import com.git.alquilalotodo.objects.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage

class ConfiguracionUsuarioActivity : AppCompatActivity() {

    private lateinit var binding:ActivityConfiguracionUsuarioBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage : FirebaseStorage
    private lateinit var userDoc: DocumentReference

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracionUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        //1º Cargar los datos de la bd a la vista (algunos no seran modificables)
        val userId = auth.currentUser!!.uid
        userDoc = db.collection("usuarios").document(userId)
        userDoc.get().addOnSuccessListener {
            val email = it.getString("email")
            val nombre = it.getString("nombre")
            val apellidos = it.getString("apellidos")
            val localizacion = it.getString("localizacion")
            val descripcion = it.getString("descripcion")
            val tipoCuenta = it.getString("tipoCuenta")
            val genero = it.getString("genero")
            val birthday = it.getString("birthday")
            val correoContacto = it.getString("correoContacto")
            val horasActivo = it.getString("horasActivo")
            val imageProfile = it.getString("imagenPerfil")

            if (!nombre.equals("Usuario")) {
                binding.confNombre.setText(nombre)
            }
            if (!apellidos.equals("Sin Apellidos")) {
                binding.confApellidos.setText(apellidos)
            }
            if (!localizacion.equals("Sin localizacion")) {
                binding.confLocalizacion.setText(localizacion)
            }
            if (!descripcion.equals("Sin descripcion")) {
                binding.confDescripcion.setText(descripcion)
            }
            if (!correoContacto.equals("Sin correo de contacto")) {
                binding.confCorreoContacto.setText(correoContacto)
            }
            if (!horasActivo.equals("Sin especificar")) {
                binding.confHorasActivo.setText(horasActivo)
            }

            binding.confTipoCuenta.setText(tipoCuenta)
            binding.confGenero.setText(genero)
            binding.confBirthday.setText(birthday)
            binding.confEmail.setText(email)

            if (!imageProfile.isNullOrBlank()){
                Glide.with(this).load(imageProfile.toString()).into(binding.confProfImg)
            }
        }

        binding.confEmail.isEnabled = false
        binding.confTipoCuenta.isEnabled = false


        //2º Coger todos los datos de la vista y hacer el volcado en la bd

        binding.confUserSave.setOnClickListener {
            userDoc = db.collection("usuarios").document(userId)
            userDoc.get().addOnSuccessListener {
                var userUpdated = HashMap<String, Any>()

                userUpdated = recogerCampos(userUpdated)

                if (selectedImage != null) {
                    uploadImageToFirebaseStorage(selectedImage!!) {imageURl ->
                        userUpdated.put("imagenPerfil",imageURl)
                        updateUserFields(userUpdated)
                    }
                } else {
                    updateUserFields(userUpdated)
                }
            }
        }

        binding.confGoback.setOnClickListener {
            finish()
        }

        binding.confProfImg.setOnClickListener{
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    var selectedImage : Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImage = data.data
            binding.confProfImg.setImageURI(selectedImage)
        }

    }

    private fun recogerCampos(userUpdated:HashMap<String,Any>) : HashMap<String,Any> {

        if (binding.confNombre.text.isBlank()) {
            userUpdated.put("nombre", "Usuario")
        } else {
            userUpdated.put("nombre",binding.confNombre.text.toString())
        }
        if (binding.confApellidos.text.isBlank()) {
            userUpdated.put("apellidos", "Sin Apellidos")
        } else {
            userUpdated.put("apellidos",binding.confApellidos.text.toString())
        }
        if (binding.confLocalizacion.text.isBlank()) {
            userUpdated.put("localizacion", "Sin localizacion")
        }else {
            userUpdated.put("localizacion", binding.confLocalizacion.text.toString())
        }
        if (binding.confDescripcion.text.isBlank()) {
            userUpdated.put("descripcion", "Sin descripcion")
        }else {
            userUpdated.put("descripcion", binding.confDescripcion.text.toString())
        }
        if (binding.confCorreoContacto.text.isBlank()) {
            userUpdated.put("correoContacto", "Sin correo de contacto")
        }else {
            userUpdated.put("correoContacto", binding.confCorreoContacto.text.toString())
        }
            userUpdated.put("genero", binding.confGenero.text.toString())
            userUpdated.put("birthday", binding.confBirthday.text.toString())
        if (binding.confHorasActivo.text.isBlank()) {
            userUpdated.put("horasActivo", "Sin especificar")
        }else {
            userUpdated.put("horasActivo", binding.confHorasActivo.text.toString())
        }

        return userUpdated
    }

    private fun uploadImageToFirebaseStorage(image : Uri,onComplete : (String) -> Unit) {
        var imageUrl = ""
        var storageRef = storage.reference
        val userId = auth.currentUser!!.uid

                val imageName = "profileimg.jpg"
                val fileName = "$userId/profile/$imageName" // Nombre del archivo
                val imageRef = storageRef.child(fileName)

                val uploadTask = imageRef.putFile(image)
                uploadTask.addOnCompleteListener{
                    if (uploadTask.isSuccessful){

                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            imageUrl = uri.toString()
                            onComplete(imageUrl)
                        }
                    } else {
                        println("Error al subir la imagen")
                    }
                }
    }

    private fun updateUserFields(userUpdated: HashMap<String, Any>) {
        if (userUpdated.isNotEmpty()) {
            userDoc.update(userUpdated).addOnSuccessListener {
                Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Usuario no actualizado", Toast.LENGTH_SHORT).show()
            }
        } else {
            finish()
        }
    }

    /*
    private fun saveImageUrlToDatabase(imagenURL: String, userId: String) {
        val userDocument = db.collection("usuarios").document(userId)
        userDocument.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                    val userImageProf = hashMapOf(
                        "imagenPerfil" to imagenURL
                    )
                    userDocument.set(userImageProf)
                        .addOnSuccessListener {
                            finish()
                        }.addOnFailureListener { exception ->
                            // Error al actualizar la lista de productos en Firestore
                        }
                }
            }
    }

     */
}