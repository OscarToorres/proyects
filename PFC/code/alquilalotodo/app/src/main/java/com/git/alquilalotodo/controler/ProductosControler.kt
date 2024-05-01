package com.git.alquilalotodo.controler

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.git.alquilalotodo.objects.Producto
import com.git.alquilalotodo.objects.User
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProductosControler (val auth: FirebaseAuth,val storage : FirebaseStorage,val db: FirebaseFirestore,val context: Context, val activityContext : AppCompatActivity?){


    /**
     * @author Actualiza la bd de Firestore con el nuevo producto del usuario
     * @param name Nombre del producto
     * @param description Descripcion del producto
     */
    fun addProductosFirestore(name:String, description:String, caract:String, precio:String, condic:String,categoria:String,imagenesSelected : ArrayList<Uri>){

        val currentUser = auth.currentUser
        var listProductos:ArrayList<Producto>
        var listUrl: ArrayList<String> = uploadImageToFirebaseStorage(imagenesSelected)

        val idUser = currentUser!!.uid
        val userDocument = db.collection("usuarios").document(idUser)
        userDocument.get().addOnSuccessListener {
            if (it.exists()){
                val productId = it.getLong("productId") ?: 0
                val listProductosMap = it.get("listaProductos")
                if (listProductosMap != null) {
                    listProductos = listProductosMap as ArrayList<Producto>
                } else {
                    listProductos = ArrayList()
                }
                listProductos.add(Producto(idUser,productId,name,description,condic,caract,"",categoria,listUrl,precio))

                val listaActualizada = hashMapOf<String,Any>(
                    "listaProductos" to listProductos
                )

                userDocument.update(listaActualizada)
                    .addOnSuccessListener {
                        //Log.d(TAG, "Objeto añadido correctamente al ArrayList en Firestore")
                    }.addOnFailureListener { it ->
                        //Log.e(TAG, "Error al actualizar el ArrayList en Firestore", it)
                    }
            }
        }.addOnFailureListener{
            //Log.e(TAG, "Error al obtener el documento de Firestore", it)
        }
    }
    fun actualizarProductosFirestore(name:String, description:String, caract:String, precio:String, condic:String,categoria:String,imagenesSelected : ArrayList<Uri>,posicion:Int,context: AppCompatActivity){
        val currentUser = auth.currentUser
        var listProductos:ArrayList<Producto>
        var listUrl: ArrayList<String> = uploadImageToFirebaseStorage(imagenesSelected)
        val idUser = currentUser!!.uid
        val userDocument = db.collection("usuarios").document(idUser)
        userDocument.get().addOnSuccessListener {
            if (it.exists()){
                listProductos = it.get("listaProductos") as ArrayList<Producto>
                val producto = listProductos.get(posicion) as HashMap<String,Any>
                val productoId = producto.get("productoId") as Long
                if (imagenesSelected.isEmpty()){
                    val productoRecuperado = listProductos.get(posicion) as HashMap<String,Any>
                    val listaImagenesRecuperadas = productoRecuperado.get("listImagenes") as ArrayList<String>
                    val proctoActualizado = Producto(idUser,productoId,name,description,condic,caract,"",categoria,listaImagenesRecuperadas,precio)
                    listProductos[posicion] = proctoActualizado
                }else {
                    listProductos[posicion] = Producto(idUser,productoId,name,description,condic,caract,"",categoria,listUrl,precio)

                }


                val listaActualizada = hashMapOf<String,Any>(
                    "listaProductos" to listProductos
                )

                userDocument.update(listaActualizada)
                    .addOnSuccessListener {
                        if (listUrl.isEmpty()){
                            context.finish()
                        }
                        //Log.d(TAG, "Objeto añadido correctamente al ArrayList en Firestore")
                    }.addOnFailureListener { it ->
                        //Log.e(TAG, "Error al actualizar el ArrayList en Firestore", it)
                    }
            }
        }.addOnFailureListener{
            //Log.e(TAG, "Error al obtener el documento de Firestore", it)
        }
    }

    fun eliminarProductoFirestore(posicion:Int,context: AppCompatActivity) {

        val currentUser = auth.currentUser

        val idUser = currentUser!!.uid
        val userDocument = db.collection("usuarios").document(idUser)
        userDocument.get().addOnSuccessListener {
            if (it.exists()){
                val listProductos = it.get("listaProductos") as ArrayList<Producto>
                val producto = listProductos.get(posicion) as HashMap<String,Any>
                val productoId = producto.get("productoId") as Long

                listProductos.removeAt(posicion)
                var productCount = it.getLong("productCount") ?: 0
                productCount -= 1

                val listaActualizada = hashMapOf<String,Any>(
                    "listaProductos" to listProductos,
                    "productCount" to productCount
                )

                userDocument.update(listaActualizada)
                    .addOnSuccessListener {

                        val storageRef = storage.reference
                        val carpetaAEliminar = storageRef.child(idUser).child("$productoId")
                        carpetaAEliminar.listAll().addOnSuccessListener {
                            val deleteImages = mutableListOf<Task<Void>>()
                            for (item in it.items) {
                                val deletePromise = item.delete()
                                deleteImages.add(deletePromise)
                            }

                            Tasks.whenAll(deleteImages).addOnSuccessListener {
                                context.finish()

                            }
                        }
                        //Log.d(TAG, "Objeto añadido correctamente al ArrayList en Firestore")
                    }.addOnFailureListener { it ->
                        //Log.e(TAG, "Error al actualizar el ArrayList en Firestore", it)
                    }
            }
        }.addOnFailureListener{
            //Log.e(TAG, "Error al obtener el documento de Firestore", it)
        }
    }


    /**
     * @author Guarda las imagenes proporcionadas por el usurario en Firebase Storage y aumenta los
     * contadores almacenados en su documento de Firestore. Crea una ruta de carpetas con la
     * siguiente estructura: idUsuario/numero de producto a subir/imagen + numero de imagen + .jpg
     * @return Una lista con las URL de las imagenes subidas a storage
     * @param imageList Lista de imagenes URI
     */
    private fun uploadImageToFirebaseStorage(imageList : ArrayList<Uri>) : ArrayList<String> {
        var listImgUrl: ArrayList<String> = ArrayList()
        if (imageList.isNotEmpty()) {
            var storageRef = storage.reference
            val userId = auth.currentUser!!.uid
            val userRef = db.collection("usuarios").document(userId)
            userRef.get().addOnSuccessListener { documentSnapshot ->
                val productCount = documentSnapshot.getLong("productCount") ?: 0
                val productId = documentSnapshot.getLong("productId") ?: 0

                val productIdString = productId.toString()

                var counter = 0
                var completedTasks = 0
                for (image in imageList) {
                    val imageName = "image$counter.jpg"
                    val fileName = "$userId/$productIdString/$imageName" // Nombre del archivo
                    val imageRef = storageRef.child(fileName)

                    val uploadTask = imageRef.putFile(image)
                    uploadTask.addOnCompleteListener {
                        if (uploadTask.isSuccessful) {

                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                val imageUrl = uri.toString()
                                listImgUrl.add(imageUrl)
                                completedTasks++

                                if (completedTasks == imageList.size) {
                                    saveImageUrlToDatabase(listImgUrl, userId)
                                }
                            }


                        } else {
                            println("Error al subir la imagen")
                        }
                    }
                    counter += 1
                }
                val newProductCount = productCount + 1
                val newProductId = productId + 1

                val camposActualizados = hashMapOf<String, Any>(
                    "productCount" to newProductCount,
                    "productId" to newProductId
                )

                userRef.update(camposActualizados)
                    .addOnSuccessListener {
                        println("Campos correctamente actualizados")

                        // El contador se actualizó exitosamente
                        // Puedes realizar cualquier acción adicional aquí
                    }.addOnFailureListener {
                        println("Error al actualizar los campos")

                    }

            }
            return listImgUrl
        }else {
            return listImgUrl
        }
    }


    private fun saveImageUrlToDatabase(listUrl: ArrayList<String>, userId: String) {
        val userDocument = db.collection("usuarios").document(userId)
        userDocument.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val usuario = documentSnapshot.toObject(User::class.java)
                if (usuario != null) {
                    val productos = usuario.listaProductos
                    val lastIndex = productos?.size!! - 1

                    val producto = productos[lastIndex]
                    producto.listImagenes = listUrl
                    productos[lastIndex] = producto

                    userDocument.update("listaProductos", productos)
                        .addOnSuccessListener {
                            val intent = Intent(context,MainFragmentControlerActivity::class.java)
                            startActivity(context,intent,null)
                            activityContext?.finish()

                        }.addOnFailureListener { exception ->
                            // Error al actualizar la lista de productos en Firestore
                        }
                }
            }
        }.addOnFailureListener { exception ->
            // Error al obtener el documento de Firestore
        }
    }


}