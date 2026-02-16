package com.ejemplo.gestortareas.servicio;

import com.ejemplo.gestortareas.dao.TareaDAO;
import com.ejemplo.gestortareas.modelo.Tarea;

import java.util.List;
import java.util.Optional;

public class TareaService {
    private final TareaDAO tareaDAO;

    public TareaService() {
        this.tareaDAO = new TareaDAO();
    }

    public Tarea crearTarea(String titulo, String descripcion) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        
        Tarea tarea = new Tarea(titulo.trim(), descripcion != null ? descripcion.trim() : "");
        return tareaDAO.guardar(tarea);
    }

    public List<Tarea> listarTodas() {
        return tareaDAO.obtenerTodas();
    }

    public List<Tarea> listarPendientes() {
        return tareaDAO.obtenerPendientes();
    }

    public Optional<Tarea> obtenerTarea(Long id) {
        return tareaDAO.obtenerPorId(id);
    }

    public boolean completarTarea(Long id) {
        Optional<Tarea> tareaOpt = tareaDAO.obtenerPorId(id);
        if (tareaOpt.isPresent()) {
            Tarea tarea = tareaOpt.get();
            tarea.setCompletada(true);
            return tareaDAO.actualizar(tarea);
        }
        return false;
    }

    public boolean actualizarTarea(Long id, String titulo, String descripcion) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        
        Optional<Tarea> tareaOpt = tareaDAO.obtenerPorId(id);
        if (tareaOpt.isPresent()) {
            Tarea tarea = tareaOpt.get();
            tarea.setTitulo(titulo.trim());
            tarea.setDescripcion(descripcion != null ? descripcion.trim() : "");
            return tareaDAO.actualizar(tarea);
        }
        return false;
    }

    public boolean eliminarTarea(Long id) {
        return tareaDAO.eliminar(id);
    }
}
