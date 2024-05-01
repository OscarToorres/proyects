package com.git.alquilalotodo.adapter

import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.git.alquilalotodo.R

class ImageAdapterURL(private val imageUris: ArrayList<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return imageUris.size
    }

    override fun getItem(position: Int): Any {
        return imageUris[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView: ImageView
        if (convertView == null) {
            imageView = ImageView(parent?.context)
            imageView.layoutParams = AbsListView.LayoutParams(200, 200) // Tamaño de cada elemento de la cuadrícula
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            imageView = convertView as ImageView
        }

        if (position < imageUris.size) {
            val imageUri = imageUris[position]
            Glide.with(imageView)
                .load(imageUri)
                .into(imageView)
        }

        return imageView
    }
}