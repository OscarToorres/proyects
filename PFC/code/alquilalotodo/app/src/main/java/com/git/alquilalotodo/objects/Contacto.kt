package com.git.alquilalotodo.objects

data class Contacto (

    val nombre : String?,
    val contactoUid : String?,
    val lastMsg:String?,
    val imagenPerfil: String?

) {
    constructor(): this("","","","")
}
