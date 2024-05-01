package com.git.alquilalotodo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.git.alquilalotodo.R
import com.git.alquilalotodo.objects.Contacto
import com.git.alquilalotodo.objects.Message

class ContactosAdapter(private val listaContactos : List<Contacto>, private val activityContext : AppCompatActivity): RecyclerView.Adapter<ContactosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ContactosViewHolder(layoutInflater.inflate(R.layout.message_adapter_layout,parent,false))
    }

    override fun getItemCount(): Int = listaContactos.size

    override fun onBindViewHolder(holder: ContactosViewHolder, position: Int) {
        val contacto = listaContactos[position]
        holder.renderMessage(contacto,position,activityContext)

    }
}