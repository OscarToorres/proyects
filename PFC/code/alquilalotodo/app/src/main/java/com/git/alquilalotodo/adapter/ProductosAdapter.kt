package com.git.alquilalotodo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.git.alquilalotodo.objects.Producto
import com.git.alquilalotodo.R

class ProductosAdapter(private val listaProductos : ArrayList<Producto>) : RecyclerView.Adapter<ProductosViewHolder>() {

    var onItemClick : ((Producto,Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductosViewHolder(layoutInflater.inflate(R.layout.item_producto,parent,false))

    }

    //Obtiene el tamaño del listado que vamos a tener
    override fun getItemCount(): Int = listaProductos.size

    override fun onBindViewHolder(holder: ProductosViewHolder, position: Int) {

        val item = listaProductos[position]
        holder.renderProd(item)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item,position)
        }

    }
}