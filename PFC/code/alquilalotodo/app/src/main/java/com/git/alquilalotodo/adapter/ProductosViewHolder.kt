package com.git.alquilalotodo.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.git.alquilalotodo.objects.Producto
import com.git.alquilalotodo.R

class ProductosViewHolder(view:View) : RecyclerView.ViewHolder(view) {

    val nombreProd = view.findViewById<TextView>(R.id.item_nombre_categoria)
    val descpProd = view.findViewById<TextView>(R.id.item_desc_prod)


    val imgProd = view.findViewById<ImageView>(R.id.item_img_prod)

    fun renderProd(producto: Producto) {
        nombreProd.text = producto.nombre
        descpProd.text = producto.descripcion
        Glide.with(imgProd.context).load(producto.imagenURL).into(imgProd)

    }
}