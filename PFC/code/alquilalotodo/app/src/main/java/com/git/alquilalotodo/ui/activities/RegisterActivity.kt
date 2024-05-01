package com.git.alquilalotodo.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.git.alquilalotodo.controler.MainFragmentControlerActivity
import com.git.alquilalotodo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.content.ContentValues.TAG
import com.git.alquilalotodo.databinding.ActivityRegisterBinding
import com.git.alquilalotodo.objects.Message
import com.git.alquilalotodo.objects.Producto
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    /********** OBJETOS DE CLASE **********/

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var dbRealTime : FirebaseDatabase
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var registerEmail: EditText
    private lateinit var registerUsername: EditText
    private lateinit var registerPassword: EditText
    private lateinit var registerConfirmPassword: EditText


    /******************************/
    /********** onCreate **********/
    /******************************/
    //Nota: Cambiar para user ViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Oculta la ActionBar
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        //Localizacion de objetos
        registerEmail = binding.registerEmail
        registerPassword = binding.registerPassword
        registerConfirmPassword = binding.registerConfirmPassword
        registerUsername = binding.registerUsername

        //Volver a la actividad de comienzo
        binding.registerGoback.setOnClickListener {
            startActivity(Intent(this, GetStartedActivity::class.java))
            finish()
        }

        //Registrar al nuevo usuario
        binding.registerBtn.setOnClickListener {
            val email: String = registerEmail.text.toString()
            val username: String = registerUsername.text.toString()
            val password: String = registerPassword.text.toString()
            val passwordConfirm: String = registerConfirmPassword.text.toString()

            //Comprobar campos vacios
            if (checkEmpty(email, password, passwordConfirm,username)) {
                //Comprobar que las contraseñas son iguales
                if(password != passwordConfirm){
                    Toast.makeText(applicationContext, getString(R.string.passwordNoMatchToast), Toast.LENGTH_LONG).show()
                }else{
                    //Crear la cuenta
                    createAccount(email, password,username)
                }

            }else{
                Toast.makeText(applicationContext, getString(R.string.genericErrorToast), Toast.LENGTH_LONG).show()
            }

        }
    }

    /**
     * @author Comprueba que los campos del registro no esten vacios
     * @param email Email de registro de usuario
     * @param password Contraseña del usuario
     * @param password2 Confirmacion de contrasela del usuario
     * @return Devuelve true si no estan vacios y false si alguno lo esta
     */
    private fun checkEmpty(email: String, password: String, password2: String,username:String): Boolean {
        return (email.isNotEmpty() && password.isNotEmpty() && password2.isNotEmpty() && username.isNotEmpty())
    }

    /**
     * @author Crea mediante el gmail y la contraseña una cuenta en Firebase Auth. Despues almacena
     * el email en un documento de la Firebase Firestore con el id del usuario, creado por el
     * propio Firebase Auth
     * @param email Email de registro de usuario
     * @param password Contraseña del usuario
     */
    private fun createAccount(email: String, password: String,username:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    addDataToFirestore(email,username)
                    // Sign in success, update UI with the signed-in user's information
                    // val user = auth.currentUser
                    startActivity(Intent(this, MainFragmentControlerActivity::class.java))
                    finish()
                } else {
                    println("Mensaje de error " + it.exception)
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

    }

    /**
     * @author Añade los datos del usuario a un documento de Firebase Firestore
     * @param email El email de registro del usuario
     */
    private fun addDataToFirestore(email : String,username:String){
        val user = auth.currentUser //Obtener el usuario actual
        val userId = user?.uid // Obtener el id del usuario actual
        if (userId != null) {
            //addDataToRealTimeDatabase(userId,username)
            val list : ArrayList<Producto> = ArrayList()
            val userMapData = hashMapOf(
                "email" to email,
                "nombre" to username,
                "productCount" to 0,
                "productId" to 0,
                "listaProductos" to list
            )
            db.collection("usuarios").document(userId).set(userMapData)
                .addOnSuccessListener {
                    Log.d(TAG, "Documento creado correctamente")
                }.addOnFailureListener{
                    Log.e(TAG, "Error al crear el documento", it)
                }
        }
        val ref = db.collection("usuarios").document(userId!!)
        ref.get().addOnSuccessListener {
            if (it != null) {
                val invent = it.data?.get("mentira")?.toString()
            }
        }
    }

    /*
    private fun addDataToRealTimeDatabase(userId:String,username:String){

        dbRealTime = FirebaseDatabase.getInstance()
        val myRef = dbRealTime.getReference("msgUsers")

        val message = Message(username,userId)

        myRef.child(userId).setValue(message).addOnSuccessListener {

        }


    }

     */

}