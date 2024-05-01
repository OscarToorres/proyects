package com.git.alquilalotodo.objects

class Message {
    var mensaje: String? = null
    var userId: String? = null //Sera el uid del usuario

    constructor() {}

    constructor(message: String?, userId: String?) {
        this.mensaje = message
        this.userId = userId

    }
}