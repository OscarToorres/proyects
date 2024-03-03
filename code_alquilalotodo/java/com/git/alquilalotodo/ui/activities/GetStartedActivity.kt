package com.git.alquilalotodo.ui.activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.git.alquilalotodo.controler.MainFragmentControlerActivity
import com.git.alquilalotodo.R
import com.git.alquilalotodo.databinding.ActivityGetStartedBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GetStartedActivity : AppCompatActivity() {

    /********** Objetos de Clase **********/

    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var binding: ActivityGetStartedBinding

    private lateinit var edt_username: EditText
    private lateinit var edt_password: EditText

    val RC_SING_IN = 10001

    /******************************/
    /********** onCreate **********/
    /******************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Oculta la ActionBar
        binding = ActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Splash de carga
        screenSplash.setKeepOnScreenCondition { false }
        auth = Firebase.auth

        //Establecer el registro con una cuenta de google
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this,options)

        /******************* LOCALIZACION DE OBJETOS *******************/
        edt_username = binding.getstartedUsername
        edt_password = binding.getstartedPassword

        /******************* LISTENERS *******************/
        //Login
        binding.getstartedBtnStart.setOnClickListener {
            if (checkEmpty(edt_username.text.toString(), edt_password.text.toString())) {
                signIn(edt_username.text.toString(), edt_password.text.toString())
            } else {
                Toast.makeText(applicationContext, getString(R.string.loginEmptyFields), Toast.LENGTH_LONG).show()
            }
        }
        //Registrarse
        binding.getstartedRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        //Comenzar sin un usuario (Limita las funcionalidades)
        binding.getstartedClose.setOnClickListener{
            startActivity(Intent(this, MainFragmentControlerActivity::class.java))
            finish()
        }
        //Comenzar con una cuenta de google
        binding.getstartedGoogle.setOnClickListener{
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent,RC_SING_IN)
        }
    }

    /*****************************/
    /********** onStart **********/
    /*****************************/

    public override fun onStart() {
        super.onStart()
        // Comprobar si el usuario ya tiene la sesión iniciada.
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(this, MainFragmentControlerActivity::class.java))
        }
    }

    /*******************************/
    /********** FUNCIONES **********/
    /*******************************/

    /******************* METODOS DE COMPROBACION DE CAMPOS *******************/

    /**
     * Comprueba que los campos no estan vacios
     */
    private fun checkEmpty(email: String, password: String): Boolean {
        return (email.isNotEmpty() && password.isNotEmpty())
    }

    /**
     * Inicia sesion con los datos que el usurario introduce
     * @param email email del usuario
     * @param password contraseña del usuario
     */
    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this, MainFragmentControlerActivity::class.java)
                    intent.putExtra("email", email)
                    // val user = auth.currentUser
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "Usuario o contraseña invalidos",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    /****** INICIO DE SESION CON GOOGLE ******/

    /**
     * Inicia sesion con una la cuenta de google del usuario
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SING_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success")
                            val user = auth.currentUser
                            startActivity(Intent(this, MainFragmentControlerActivity::class.java))
                            finish()
                            //updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.exception)
                            Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()
                        }
                    }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
}