package com.git.alquilalotodo.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.git.alquilalotodo.R
import com.git.alquilalotodo.objects.Categories
import com.git.alquilalotodo.objects.ItemCategoria

class CategoriaViewHolder(view:View) : RecyclerView.ViewHolder(view) {

    private val nombreCategoriaTextView: TextView = itemView.findViewById(R.id.item_nombre_categoria)
    private val iconoCategoriaImageView: ImageView = itemView.findViewById(R.id.item_icon_categoria )

    fun renderCatg(categoria: ItemCategoria) {
        nombreCategoriaTextView.text = categoria.nombre

        // Establecer el icono según la categoría
        val iconoResId = when (categoria.categories) {
            Categories.ACCESORIOS -> R.drawable.ic_baseline_vpn_key_24
            Categories.HERRAMIENTAS -> R.drawable.ic_baseline_vpn_key_24
            Categories.INFORMATICA -> R.drawable.ic_baseline_vpn_key_24
            Categories.INSTRUMENTOS -> R.drawable.ic_baseline_vpn_key_24
            Categories.JARDINERIA -> R.drawable.ic_baseline_vpn_key_24
            Categories.PRENDAS -> R.drawable.ic_baseline_vpn_key_24
            Categories.VEHICULOS -> R.drawable.ic_baseline_vpn_key_24
            Categories.VIDEOJUEGOS -> R.drawable.ic_baseline_vpn_key_24
        }
        iconoCategoriaImageView.setImageResource(iconoResId)
    }

}