# Fase de implementación

Para este proyecto se han tenido que aprender diversas tecnologías y formas de desarrollo. Aquí mostraré algunas que me han parecido más interesantes o complicadas de aplicar. Si quieres ver todos los documentos o alguno en concreo, entra [aquí](https://gitlab.com/iesleliadoura/DAM2/oscar-torres/-/tree/main/src/alquilalotodo/app/src/main/java/com/git/alquilalotodo)


## View Binding

El view binding sirve para enlazar directamente las vistas a la Actividad sin tener que usar el "findViewById()". Esto nos permite ahorrar código y tener más limpieza en nuestro proyecto.

Para cada clase, el binding tendrá un nombre diferente. En Kotlin lo definiremos como "lateinit" que nos dejará inicializarlo más tarde.

~~~~
     private lateinit var binding:ActivityConfiguracionUsuarioBinding
~~~~

En el metodo onCreate de la activity lo iniciaremos de la siguiente forma. Despues para llamar a los atributos del layout será con el el binding y el id del atrubuto.

~~~~
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracionUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.confNombre.setText(nombre)

        binding.confUserSave.setOnClickListener {}

}
~~~~

## Métodos asíncronos

Al usar la base de datos de Firebase Firestore, nos encontramos con que usa metodos asíncronos, es decir, la ejecución del código continúa y la operación asíncrona se ejecuta por detrás. En este caso estamos recuperando una lista de objetos para despues mostrarla en un "recycleView", por lo tanto tenemos que esperar a que acabe de ejecutarse para poder mandar la lista completa y mostrarla.

~~~~
fun setUserProducts(userId: String, callback: (ArrayList<Producto>) -> Unit) {
        var productList: ArrayList<Producto> = ArrayList()
        db = FirebaseFirestore.getInstance()
        val userDoc = db.collection("usuarios").document(userId)
        userDoc.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val usuario = documentSnapshot.toObject(User::class.java)
                if (usuario != null) {
                    val productos = usuario.listaProductos
                    if (productos != null) {
                        for (prod in productos) {
                            val producto = Producto(
                                prod.userId,
                                prod.productoId,
                                prod.nombre,
                                prod.descripcion,
                                prod.condiciones,
                                prod.caracteristicas,
                                prod!!.listImagenes!![0],
                                prod.categoria,
                                prod.listImagenes,
                                prod.precio


                            )
                            productList.add(producto)
                        }
                    }
                }
            }
            // Llama al callback y pasa el resultado
            callback(productList)
        }
    }
~~~~

## Configurar el recycleView con un metodo asíncrono y un onClick

En este caso estamos configurando el recycleView en un fragment, por eso usamos view. Para cargar la lista y asignarsela al recycleView necesitamos hacerlo dentro de la llamada al metodo asíncrono. Una vez ahí podemos hacer todo lo que queramos a nuestro recycleView, como añadirle un metodo onClick, para al pulsar en cualquier item de nuestro adaptador ir a la actividad que llamamos y pasarle los datos que queramos. Esto ahorra muchos rompecabezas y facilita bastante la comprensión del código. 
~~~~
        recycleView = view.findViewById(R.id.generic_recycleview)
        recycleView.setHasFixedSize(true)
        recycleView.layoutManager = GridLayoutManager(view.context, 2)
        productosProvider.setAllProducts() { productList ->
            productosAdapter = ProductosAdapter(productList)
            recycleView.adapter = productosAdapter

            productosAdapter.onItemClick = {producto,position ->
                val intent = Intent(view.context, ProductItemHomeActivity::class.java)
                // val user = auth.currentUser
                intent.putExtra("productoItem",producto)
                intent.putExtra("productoPosicion", position)
                startActivity(intent)
            }
        }
~~~~

## Enviar data para reutilizar fragments y vistas

En el desarrollo de la aplicación me ha sido de bastante ayuda aprender a reutilizar lo que ya he creado. De esta forma puedo darle al usuario la sensación de que está en otro sistio completamente diferente, usando la misma lógica. Se trata de pasar argumentos a los fragments o activities y hacer comprobaciones. De esta forma sabemos desde donde está llamando a esa vista el usuario y le podremos mostrar datos diferentes.

En este caso comprobamos si se le pasa argumentos y asignamos a una variable global el data correspondiente, y de esta forma cargamos, en este caso, productos diferentes para mostrar.

¿Cómo se envían argumentos?

~~~~
                val fragment = ProfileProductsFragment()
                val bundle = Bundle()
                bundle.putString("userId", userId)
                fragment.arguments = bundle
                fragment //Llamamos al fragment
~~~~
¿Cómo los recibo?
~~~~
        uid = arguments?.getString("userId")

        if (uid.isNullOrBlank()){
            uid = auth.currentUser!!.uid
        }
~~~~
