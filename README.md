# Task Manager (JavaFX)

Aplicación de gestión de tareas desarrollada en **Java** utilizando **JavaFX** y **SQLite**.

## Características

- Registro de usuarios
- Inicio de sesión
- Crear tareas
- Editar tareas
- Eliminar tareas
- Marcar tareas como completadas
- Persistencia de datos con SQLite

## Tecnologías

- Java
- JavaFX
- Maven
- SQLite
- JUnit

## Estructura del proyecto

controller → controladores de la interfaz  
model → modelos de datos  
database → conexión a SQLite  
resources → vistas FXML  

 ## Cómo ejecutar la aplicación

### Opción 1: Desde IntelliJ IDEA (recomendado)

1. Abrir el proyecto en IntelliJ IDEA.
2. Ir a la pestaña **Maven**.
3. Navegar a:


Plugins → javafx → javafx:run


4. Ejecutar **javafx:run** para iniciar la aplicación.

### Opción 2: Desde la terminal con Maven

En la raíz del proyecto ejecutar:


mvn javafx:run


Esto iniciará la aplicación JavaFX.

## Autor

Maximiliano Campos
