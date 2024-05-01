package com.git.alquilalotodo.provider

import com.git.alquilalotodo.R
import com.git.alquilalotodo.objects.Producto
import com.git.alquilalotodo.objects.User
import com.git.alquilalotodo.ui.fragment.perfil.ProfileFragment
import com.google.firebase.firestore.FirebaseFirestore

class ProductosProvider {

    private lateinit var db: FirebaseFirestore

    /**
     * @author Recoge todos los productos que tiene el usuario publicados.
     * @param userId Id del usuario
     * @return Devuelve un ArrayList con los productos o una lista vacia si no tiene ninguno
     */
    fun setUserProducts(userId: String, callback: (ArrayList<Producto>) -> Unit) {
        var productList: ArrayList<Producto> = ArrayList()
        db = FirebaseFirestore.getInstance()
        val userDoc = db.collection("usuarios").document(userId)
        userDoc.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val usuario = documentSnapshot.toObject(User::class.java)
                if (usuario != null) {
                    val productos = usuario.listaProductos
                    if (productos != null) {
                        for (prod in productos) {
                            val producto = Producto(
                                prod.userId,
                                prod.productoId,
                                prod.nombre,
                                prod.descripcion,
                                prod.condiciones,
                                prod.caracteristicas,
                                prod!!.listImagenes!![0],
                                prod.categoria,
                                prod.listImagenes,
                                prod.precio


                            )
                            productList.add(producto)
                        }
                    }
                }
            }
            // Llama al callback y pasa el resultado
            callback(productList)
        }
    }


    /**
     * @author Recoge todos los productos que tienen todos los usuarios publicados.
     * @param callback Un callback para cargar los productos en la lista
     */
    fun setAllProducts(callback: (ArrayList<Producto>) -> Unit) {
        var productList: ArrayList<Producto> = ArrayList()
        db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("usuarios")
        collectionRef.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                for (document in querySnapshot.documents) {
                    // Accede a los datos de cada documento utilizando document.data
                    val usuario = document.toObject(User::class.java)
                    val productos = usuario?.listaProductos
                    if (productos != null){
                        for (prod in productos) {
                            val producto = Producto(
                                prod.userId,
                                prod.productoId,
                                prod.nombre,
                                prod.descripcion,
                                prod.condiciones,
                                prod.caracteristicas,
                                prod!!.listImagenes!![0],
                                prod.categoria,
                                prod.listImagenes,
                                prod.precio

                                )
                            productList.add(producto)
                        }
                    }

                }
            } else {
                // La colección está vacía
            }
            callback(productList)

        }.addOnFailureListener { exception ->
            // Ocurrió un error al obtener los documentos de la colección
        }

    }

    fun setAllProductsWithFilter(filtro:String,callback: (ArrayList<Producto>) -> Unit) {
        var productList: ArrayList<Producto> = ArrayList()
        db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("usuarios")
        collectionRef.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                for (document in querySnapshot.documents) {
                    // Accede a los datos de cada documento utilizando document.data
                    val usuario = document.toObject(User::class.java)
                    val productos = usuario?.listaProductos
                    if (productos != null){
                        for (prod in productos) {
                            if (prod.categoria!!.contains(filtro)) {
                                val producto = Producto(
                                    prod.userId,
                                    prod.productoId,
                                    prod.nombre,
                                    prod.descripcion,
                                    prod.condiciones,
                                    prod.caracteristicas,
                                    prod!!.listImagenes!![0],
                                    prod.categoria,
                                    prod.listImagenes,
                                    prod.precio

                                )
                                productList.add(producto)
                            }
                        }
                    }

                }
            } else {
                // La colección está vacía
            }
            callback(productList)

        }.addOnFailureListener { exception ->
            // Ocurrió un error al obtener los documentos de la colección
        }

    }

}



