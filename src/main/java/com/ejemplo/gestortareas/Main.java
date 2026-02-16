package com.ejemplo.gestortareas;

import com.ejemplo.gestortareas.database.DatabaseConnection;
import com.ejemplo.gestortareas.modelo.Tarea;
import com.ejemplo.gestortareas.servicio.TareaService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static TareaService tareaService;
    private static Scanner scanner;

    public static void main(String[] args) {
        tareaService = new TareaService();
        scanner = new Scanner(System.in);
        
        DatabaseConnection.inicializarTablas();
        
        boolean salir = false;
        
        while (!salir) {
            mostrarMenu();
            String opcion = scanner.nextLine();
            
            switch (opcion) {
                case "1":
                    agregarTarea();
                    break;
                case "2":
                    listarTodasTareas();
                    break;
                case "3":
                    listarTareasPendientes();
                    break;
                case "4":
                    verDetalleTarea();
                    break;
                case "5":
                    completarTarea();
                    break;
                case "6":
                    actualizarTarea();
                    break;
                case "7":
                    eliminarTarea();
                    break;
                case "0":
                    salir = true;
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
        
        scanner.close();
        DatabaseConnection.cerrarConexion();
    }

    private static void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("      GESTOR DE TAREAS - MENÚ          ");
        System.out.println("========================================");
        System.out.println("1. Agregar nueva tarea");
        System.out.println("2. Listar todas las tareas");
        System.out.println("3. Listar tareas pendientes");
        System.out.println("4. Ver detalle de una tarea");
        System.out.println("5. Marcar tarea como completada");
        System.out.println("6. Actualizar tarea");
        System.out.println("7. Eliminar tarea");
        System.out.println("0. Salir");
        System.out.println("========================================");
        System.out.print("Seleccione una opción: ");
    }

    private static void agregarTarea() {
        System.out.println("\n--- AGREGAR NUEVA TAREA ---");
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        
        System.out.print("Descripción (opcional): ");
        String descripcion = scanner.nextLine();
        
        try {
            Tarea tarea = tareaService.crearTarea(titulo, descripcion);
            if (tarea != null) {
                System.out.println("✓ Tarea creada exitosamente con ID: " + tarea.getId());
            } else {
                System.out.println("✗ Error al crear la tarea");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private static void listarTodasTareas() {
        System.out.println("\n--- LISTA DE TODAS LAS TAREAS ---");
        List<Tarea> tareas = tareaService.listarTodas();
        
        if (tareas.isEmpty()) {
            System.out.println("No hay tareas registradas.");
        } else {
            for (Tarea tarea : tareas) {
                System.out.println(tarea);
            }
            System.out.println("\nTotal: " + tareas.size() + " tarea(s)");
        }
    }

    private static void listarTareasPendientes() {
        System.out.println("\n--- TAREAS PENDIENTES ---");
        List<Tarea> tareas = tareaService.listarPendientes();
        
        if (tareas.isEmpty()) {
            System.out.println("No hay tareas pendientes.");
        } else {
            for (Tarea tarea : tareas) {
                System.out.println(tarea);
            }
            System.out.println("\nTotal pendientes: " + tareas.size() + " tarea(s)");
        }
    }

    private static void verDetalleTarea() {
        System.out.println("\n--- VER DETALLE DE TAREA ---");
        System.out.print("Ingrese el ID de la tarea: ");
        
        try {
            Long id = Long.parseLong(scanner.nextLine());
            var tareaOpt = tareaService.obtenerTarea(id);
            
            if (tareaOpt.isPresent()) {
                Tarea tarea = tareaOpt.get();
                System.out.println("\n----------------------------------------");
                System.out.println("ID: " + tarea.getId());
                System.out.println("Título: " + tarea.getTitulo());
                System.out.println("Descripción: " + (tarea.getDescripcion().isEmpty() ? "(Sin descripción)" : tarea.getDescripcion()));
                System.out.println("Estado: " + (tarea.isCompletada() ? "Completada" : "Pendiente"));
                System.out.println("Fecha de creación: " + tarea.getFechaCreacion());
                if (tarea.getFechaCompletada() != null) {
                    System.out.println("Fecha de completado: " + tarea.getFechaCompletada());
                }
                System.out.println("----------------------------------------");
            } else {
                System.out.println("✗ No se encontró la tarea con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: El ID debe ser un número válido");
        }
    }

    private static void completarTarea() {
        System.out.println("\n--- MARCAR TAREA COMO COMPLETADA ---");
        System.out.print("Ingrese el ID de la tarea: ");
        
        try {
            Long id = Long.parseLong(scanner.nextLine());
            if (tareaService.completarTarea(id)) {
                System.out.println("✓ Tarea marcada como completada");
            } else {
                System.out.println("✗ No se encontró la tarea con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: El ID debe ser un número válido");
        }
    }

    private static void actualizarTarea() {
        System.out.println("\n--- ACTUALIZAR TAREA ---");
        System.out.print("Ingrese el ID de la tarea a actualizar: ");
        
        try {
            Long id = Long.parseLong(scanner.nextLine());
            var tareaOpt = tareaService.obtenerTarea(id);
            
            if (tareaOpt.isPresent()) {
                Tarea tarea = tareaOpt.get();
                System.out.println("Tarea actual: " + tarea.getTitulo());
                
                System.out.print("Nuevo título (Enter para mantener '" + tarea.getTitulo() + "'): ");
                String nuevoTitulo = scanner.nextLine();
                
                System.out.print("Nueva descripción (Enter para mantener actual): ");
                String nuevaDescripcion = scanner.nextLine();
                
                String tituloFinal = nuevoTitulo.isEmpty() ? tarea.getTitulo() : nuevoTitulo;
                String descripcionFinal = nuevaDescripcion.isEmpty() ? tarea.getDescripcion() : nuevaDescripcion;
                
                if (tareaService.actualizarTarea(id, tituloFinal, descripcionFinal)) {
                    System.out.println("✓ Tarea actualizada exitosamente");
                } else {
                    System.out.println("✗ Error al actualizar la tarea");
                }
            } else {
                System.out.println("✗ No se encontró la tarea con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: El ID debe ser un número válido");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private static void eliminarTarea() {
        System.out.println("\n--- ELIMINAR TAREA ---");
        System.out.print("Ingrese el ID de la tarea a eliminar: ");
        
        try {
            Long id = Long.parseLong(scanner.nextLine());
            var tareaOpt = tareaService.obtenerTarea(id);
            
            if (tareaOpt.isPresent()) {
                System.out.print("¿Está seguro de eliminar la tarea '" + tareaOpt.get().getTitulo() + "'? (s/n): ");
                String confirmacion = scanner.nextLine();
                
                if (confirmacion.equalsIgnoreCase("s")) {
                    if (tareaService.eliminarTarea(id)) {
                        System.out.println("✓ Tarea eliminada exitosamente");
                    } else {
                        System.out.println("✗ Error al eliminar la tarea");
                    }
                } else {
                    System.out.println("Operación cancelada");
                }
            } else {
                System.out.println("✗ No se encontró la tarea con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: El ID debe ser un número válido");
        }
    }
}
