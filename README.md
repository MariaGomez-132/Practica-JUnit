# Gestor de Tareas - Java + SQLite

Aplicación de consola para gestionar tareas con persistencia en base de datos SQLite.

## Características

- Agregar nuevas tareas con título y descripción
- Listar todas las tareas
- Listar solo tareas pendientes
- Ver detalle completo de una tarea
- Marcar tareas como completadas
- Actualizar tareas existentes
- Eliminar tareas
- Persistencia automática en SQLite

## Estructura del Proyecto

```
├── pom.xml
└── src/main/java/com/ejemplo/gestortareas/
    ├── Main.java
    ├── modelo/
    │   └── Tarea.java
    ├── dao/
    │   └── TareaDAO.java
    ├── servicio/
    │   └── TareaService.java
    └── database/
        └── DatabaseConnection.java
```

## Requisitos

- Java 11 o superior
- Maven

## Compilar y Ejecutar

### Compilar el proyecto:
```bash
mvn clean compile
```

### Ejecutar la aplicación:
```bash
mvn exec:java -Dexec.mainClass="com.ejemplo.gestortareas.Main"
```

### O compilar y empaquetar como JAR ejecutable:
```bash
mvn clean package
java -jar target/gestor-tareas-1.0-SNAPSHOT.jar
```

## Uso

Al iniciar la aplicación se mostrará un menú interactivo:

```
========================================
      GESTOR DE TAREAS - MENÚ          
========================================
1. Agregar nueva tarea
2. Listar todas las tareas
3. Listar tareas pendientes
4. Ver detalle de una tarea
5. Marcar tarea como completada
6. Actualizar tarea
7. Eliminar tarea
0. Salir
========================================
Seleccione una opción: 
```

## Base de Datos

La aplicación crea automáticamente un archivo `tareas.db` en el directorio de ejecución con la siguiente estructura:

```sql
CREATE TABLE tareas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo TEXT NOT NULL,
    descripcion TEXT,
    completada INTEGER DEFAULT 0,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_completada TIMESTAMP
);
```

## Notas

- La base de datos SQLite se crea automáticamente la primera vez que se ejecuta la aplicación
- Las tareas completadas muestran [✓] y las pendientes [ ]
- El ID de cada tarea se asigna automáticamente
- Al eliminar una tarea se solicita confirmación
