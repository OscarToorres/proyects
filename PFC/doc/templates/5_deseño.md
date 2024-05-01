# DISEÑO


### Diagrama de clases.

![DiagramaDeClase](doc/img/Uml-2da-version.PNG)

### Casos de uso.

[Casos de uso](doc/documentos/casos_de_uso.pdf)

### Deseño de interfaces de usuarios.

[Mockups](doc/documentos/Mockups.pdf)

### Base de datos utilizada

Se ha utilizado Firestore, debido a que la complejidad de los datos no es muy elevada y nos ofrece todas las consultas necesarias para gestionar todos los datos de los usuarios.

### Estructura de la BD

A continuación se mostrarán unas imagenes que representan la estructura de la base de datos de Auntentificación, Firestore y Storage. 

**NOTA:** Ninguno de los datos mostrados son reales, son todo datos de prueba.

* Estructura de **Auntentificación**: Esta estructura viene proporcionada por Google.

![Auntentificación](doc/img/Auntentificacion.PNG)

* Estructura de **Firestore**: Cada usuario tiene su propio documento con el nombre de su identificador.


![Firestore](doc/img/EstructuraFirestore.PNG)

* Estructura de **Storage**: Primero se muestra la carpeta inicial, que corresponde al código de identificación de cada usuario. A continuación tendremos una carpeta por producto con las imagenes guardadas y una carpeta donde se guarda la foto de perfil del usuario.

![Firestore](doc/img/UsuariosStorage.PNG)

![Firestore](doc/img/EstructuraUsuarioData.PNG)

![Firestore](doc/img/StorageNombreImg.PNG)

* Estructura de **Realtime Database**: En esta bd guardamos los mensajes de los usuarios

![Realtime](doc/img/BDRealtime.PNG)

