# REQUERIMIENTOS DEL SISTEMA
Para el uso de la aplicación **AlquilaloTodo** será necesario un dispositivo móvil Android (mientras no se desarrolla las versiones pera IOS y no esté disponible como Web). También será necesario conexión a internet.

## Descripción General

Será un proyecto orientado al alquiler de objetos (revisar el documento [Idea](doc/templates/1_idea.md)). 

## Funcionalidades

1. Creación de una BD para la gestión de los usuarios y de los alquileres
   - El programa estará enlazado a Firebase y tendrá una base de datos propia que gestionará los usuarios y los productos.

2. Gestión de los usuarios de la aplicación en la base de datos.
    
   - Dar de alta a un usuario
   - Dar de baja a un usuario
   - Modificar los datos del usuario
   - Escribir valoraciones
   - Recibir valoraciones   

3. Gestión de las valoraciones

   - Escribir valoraciones
   - Recibir valoraciones 

4. Gestión de productos na BD:

   - Añadir productos
   - Eliminar productos
   - Alteración del estado de los productos (Disponibles / No disponibles)
   - Búsqueda de productos (Genérica, por categoría, por estado, localización, etc)
   - Ver fechas de disponibilidad del producto

5. Gestión de roles en la BD:

   - Gestionar los distintos roles de usuario para saber que operaciones pueden realizar ya que existirán tres tipos de usuario:
      - Administrador
      - Empresa
      - Usuario común

6. Gestión del perfil del usuario desde la vista de otro

   - Ver estado de un producto
   - Ver perfil
   - Ver valoraciones 
   - Información variada sobre la actividad del mismo en la aplicación
   - Añadir a favoritos (Para guardar el producto en cuestión)
   - Marcar como destacado (Para guardar el usuario de interés)
   - Contactar (Comunicación entre usuarios mediante chat de texto)

7. Gestión de la Ubicación

   - Búsqueda por áreas concretas
   - Usuario y productos asociados a una ubicación  

## Requerimientos no funcionales

- Confiabilidad: Los datos de los usuarios estarán protegidos conforme a lo que establece la ley de protección de datos.
- Disponibilidad: El servicio estará disponible 24/7, exceptuando los períodos de mantenibilidad.
- Seguridad: Sistema de encriptación de contraseñas y seguridad de doble factor.
- Mantenibilidad: Actualizaciones periodicas para la mejora de la aplicación y mantenimiento de la base de datos.
- Portabilidad: Funcionabilidad en Android, (IOS y Web en futuras versiones).
- Actuación: El sistema consta del servicio gratuito de Firebase por lo tanto si en algún momento se necesita más capacidad para los usuarios se aumentará su capacidad.


## Tipos de usuarios

- Administrador: Tendrá acceso a todas las funcionalidades y no tendrá que pasar determinadas barreras (en caso de que sea necesario) que tendrán otros usuarios para la prueba de la aplicación y comprobación de errores.
- Empresa: será un usuario de pago que contará con determinados privilegios (mayor visibilidad de sus productos, menos porcentaje de retención de los beneficios, ...)
- Usuario común: cualquier usuario que se registre en la aplicación, contará con acceso a todos los servicios tanto para alquilar productos como para ponerlos en alquiler.

## Evaluación de la viabilidad técnica del proyecto

### Hardware y Software requerido
Como el proyecto de momento solo será una aplicación android, se necesitará un dispositivo android (minimo Android 8.0)

### Interfaces de usuario

Una interfaz sencilla cuyo objetivo es facilitar el uso de la aplición a los usuarios y que toda la información mostrada sea de utilidad.

## Análisis de riesgos

Dado que la aplicación está conectada a Firebase, contamos con los sistemas de seguridad de Google para las bases de datos y un sistema de encriptación de para las contraseñas. 

Para evitar cualquier tipo de problema usamos lo máximo posible la documentación y guías que google proporciona para crear una base de datos segura. Por otro lado en caso de haber algun problema en la BD, los administradores podrán modificar los datos manualmente y gestionar las cuentas.

## Actividades

1. Sistema de Registro
2. Sistema de Login
3. Perfil de Usuario
4. Cerrar sesión
5. Creación de un producto
6. Modificación de un producto
7. Eliminación de un producto
8. Pantalla principal donde se muestran los productos
9. Acceder a un producto en concreto
10. Acceder al perfil de un usuario en concreto
11. Sistema de búsqueda
12. Modificación del perfil de usuario
13. Agregar a contactos
14. Chatear en tiempo real

## Mejoras futuras

1. Introducción de anuncios
2. Sistema de valoraciones
3. Añadir a favoritos
4. Sistema de localización de los productos
5. Filtrado más complejo
6. Categorías personalizadas
7. Eliminar la cuenta