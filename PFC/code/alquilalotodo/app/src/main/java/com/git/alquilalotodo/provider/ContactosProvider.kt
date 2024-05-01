package com.git.alquilalotodo.provider

import com.git.alquilalotodo.objects.Contacto
import com.google.firebase.firestore.FirebaseFirestore

class ContactosProvider {

    private lateinit var db: FirebaseFirestore

    fun setUserContacts(userId: String, callback: (MutableList<Contacto>) -> Unit) {
        var contactosRecuperados: MutableList<Contacto> = mutableListOf()
        db = FirebaseFirestore.getInstance()
        val userDoc = db.collection("usuarios").document(userId)
        userDoc.get().addOnSuccessListener {
            if (it.exists()) {
                if (it.get("listaChatUsuarios") != null) {
                    val listaContactos = it.get("listaChatUsuarios") as ArrayList<String>

                    for (i in 0 until listaContactos.size) {

                        //Recorrer la lista de contactos para obtener el uid
                        val contactoMap = listaContactos[i] as HashMap<String,Any>
                        val contactoUid = contactoMap["contactoUid"] as String
                        val lastMsg = contactoMap["lastMsg"] as String

                        val contactoDoc = db.collection("usuarios").document(contactoUid)
                        contactoDoc.get().addOnSuccessListener {


                            val userProfileImg = it.getString("imagenPerfil") ?: ""
                            val userName = it.getString("nombre") ?: "No tiene"
                            val unseenMessage = 0

                            val contacto = Contacto(
                                userName,
                                contactoUid,
                                lastMsg,
                                userProfileImg
                            )
                            contactosRecuperados.add(contacto)

                            if (i == listaContactos.size -1){
                                callback(contactosRecuperados)
                            }

                        }
                    }
                    // Llama al callback y pasa el resultado
                }
            }
        }
    }
}