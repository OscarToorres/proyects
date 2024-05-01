package com.git.alquilalotodo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.git.alquilalotodo.R
import com.git.alquilalotodo.objects.ItemCategoria

class CategoriaAdapter(private val listaCategories:List<ItemCategoria>): RecyclerView.Adapter<CategoriaViewHolder>() {

        var onItemClick : ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CategoriaViewHolder(layoutInflater.inflate(R.layout.item_categoria,parent,false))
    }

    override fun getItemCount(): Int = listaCategories.size

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val item = listaCategories[position]
        holder.renderCatg(item)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item.nombre)
        }
    }
}