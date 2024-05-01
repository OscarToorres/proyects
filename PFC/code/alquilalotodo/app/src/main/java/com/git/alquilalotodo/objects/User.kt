package com.git.alquilalotodo.objects

import com.google.firebase.database.PropertyName

data class User(
    @PropertyName("email") var email:String,
    @PropertyName("nombre") var nombre:String?,
    @PropertyName("apellidos") var apellidos:String?,
    @PropertyName("localizacion") var localizacion:String?,
    @PropertyName("descripcion") var descripcion:String?,
    @PropertyName("tipoCuenta") var tipoCuenta:String?,
    @PropertyName("genero") var genero:String?,
    @PropertyName("birthday") var birthday:String?,
    @PropertyName("correoContacto") var correoContacto:String?,
    @PropertyName("horasActivo") var horasActivo:String?,
    @PropertyName("listaContactosUsuario") var listaContactosUsuario :ArrayList<String>?,
    @PropertyName("listaProductos") var listaProductos : ArrayList<Producto>?

           ) {
    // Constructor sin argumentos necesario para la deserialización de Firestore
    constructor() : this("", "","","","","","","","","",null,null)
}
