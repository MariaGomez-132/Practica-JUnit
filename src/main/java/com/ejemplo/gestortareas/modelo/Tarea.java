package com.ejemplo.gestortareas.modelo;

import java.time.LocalDateTime;

public class Tarea {
    private Long id;
    private String titulo;
    private String descripcion;
    private boolean completada;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaCompletada;

    public Tarea() {
        this.fechaCreacion = LocalDateTime.now();
        this.completada = false;
    }

    public Tarea(String titulo, String descripcion) {
        this();
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
        if (completada && this.fechaCompletada == null) {
            this.fechaCompletada = LocalDateTime.now();
        }
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaCompletada() {
        return fechaCompletada;
    }

    public void setFechaCompletada(LocalDateTime fechaCompletada) {
        this.fechaCompletada = fechaCompletada;
    }

    @Override
    public String toString() {
        String estado = completada ? "[✓]" : "[ ]";
        return String.format("%s ID: %d | %s | %s", 
            estado, id, titulo, 
            completada ? "Completada" : "Pendiente");
    }
}
