package com.git.alquilalotodo.viewmodel

import androidx.lifecycle.*
import com.google.android.gms.common.api.internal.ApiKey
import kotlinx.coroutines.launch

class MainViewModel(): ViewModel() {
    /*
    private val _productos = MutableLiveData<List<ProductosModel>>(emptyList())
    //val newlist: LiveData<List<ProductosModel>>get() = _productos
    val currentList: MutableList<ProductosModel>? = _productos.value?.toMutableList()



    fun setDataListExample() {
        currentList?.add(ProductosModel("Esto es un coche", R.drawable.coche))
        currentList?.add(ProductosModel("Esto es una chaqueta", R.drawable.chaqueta_invierno))
        currentList?.add(ProductosModel("Esto es un cortacesped", R.drawable.cortacesped))
        currentList?.add(ProductosModel("Esto son herramientas", R.drawable.caja_herramienta))

        _productos.value = currentList
    }

    //Metodo para añadir objetos a la lista
    fun add (producto: ProductosModel) {
        currentList?.add(producto)
        _productos.value = currentList
    }

    //Metodo para elminar objetos de la lista
    fun remove(producto: ProductosModel){
        currentList?.remove(producto)
        _productos.value=currentList
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel() as T
        }
        throw IllegalArgumentException("UnknowViewModel")
    }

     */
}