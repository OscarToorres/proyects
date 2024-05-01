package com.git.alquilalotodo.objects

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.PropertyName

data class Producto(
        @PropertyName("uid") val userId: String,
        @PropertyName("productoId") val productoId: Long,
        @PropertyName("nombre") val nombre: String,
        @PropertyName("descripcion") val descripcion: String,
        @PropertyName("condiciones") val condiciones : String?,
        @PropertyName ("caracteristicas") val caracteristicas : String?,
        @PropertyName ("imagenURL") val imagenURL: String?,
        @PropertyName ("categoria") val categoria: String?,
        @PropertyName("listImagenes") var listImagenes: ArrayList<String>?,
        @PropertyName("precio") val precio:String
        ) : Parcelable {
        //No tocar porque sino peta ;)
        constructor(parcel: Parcel) : this(
                parcel.readString()?: "",
                parcel.readLong()?: 0,
                parcel.readString()?: "",
                parcel.readString()?:"",
                parcel.readString()?:"",
                parcel.readString()?:"",
                parcel.readString()?:"",
                parcel.readString()?:"",
                parcel.readArrayList(String::class.java.classLoader) as ArrayList<String>?,
                parcel.readString()?:""

        ) {
        }

        constructor() : this("",0,"", "","", "","","", null, "")

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(userId)
                parcel.writeLong(productoId)
                parcel.writeString(nombre)
                parcel.writeString(descripcion)
                parcel.writeString(condiciones)
                parcel.writeString(caracteristicas)
                parcel.writeString(imagenURL)
                parcel.writeString(categoria)
                parcel.writeList(listImagenes)
                parcel.writeString(precio)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Producto> {
                override fun createFromParcel(parcel: Parcel): Producto {
                        return Producto(parcel)
                }

                override fun newArray(size: Int): Array<Producto?> {
                        return arrayOfNulls(size)
                }
        }
}