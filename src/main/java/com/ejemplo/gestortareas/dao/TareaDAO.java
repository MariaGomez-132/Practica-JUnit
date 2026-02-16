package com.ejemplo.gestortareas.dao;

import com.ejemplo.gestortareas.database.DatabaseConnection;
import com.ejemplo.gestortareas.modelo.Tarea;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TareaDAO {

    public Tarea guardar(Tarea tarea) {
        String sql = "INSERT INTO tareas (titulo, descripcion, completada, fecha_creacion) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, tarea.getTitulo());
            pstmt.setString(2, tarea.getDescripcion());
            pstmt.setInt(3, tarea.isCompletada() ? 1 : 0);
            pstmt.setTimestamp(4, Timestamp.valueOf(tarea.getFechaCreacion()));
            
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                tarea.setId(rs.getLong(1));
            }
            rs.close();
            
            return tarea;
        } catch (SQLException e) {
            System.err.println("Error al guardar la tarea: " + e.getMessage());
            return null;
        }
    }

    public List<Tarea> obtenerTodas() {
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT * FROM tareas ORDER BY fecha_creacion DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                tareas.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tareas: " + e.getMessage());
        }
        
        return tareas;
    }

    public Optional<Tarea> obtenerPorId(Long id) {
        String sql = "SELECT * FROM tareas WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapearResultSet(rs));
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error al obtener tarea: " + e.getMessage());
        }
        
        return Optional.empty();
    }

    public boolean actualizar(Tarea tarea) {
        String sql = "UPDATE tareas SET titulo = ?, descripcion = ?, completada = ?, fecha_completada = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tarea.getTitulo());
            pstmt.setString(2, tarea.getDescripcion());
            pstmt.setInt(3, tarea.isCompletada() ? 1 : 0);
            if (tarea.getFechaCompletada() != null) {
                pstmt.setTimestamp(4, Timestamp.valueOf(tarea.getFechaCompletada()));
            } else {
                pstmt.setNull(4, Types.TIMESTAMP);
            }
            pstmt.setLong(5, tarea.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar tarea: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(Long id) {
        String sql = "DELETE FROM tareas WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar tarea: " + e.getMessage());
            return false;
        }
    }

    public List<Tarea> obtenerPendientes() {
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT * FROM tareas WHERE completada = 0 ORDER BY fecha_creacion DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                tareas.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tareas pendientes: " + e.getMessage());
        }
        
        return tareas;
    }

    private Tarea mapearResultSet(ResultSet rs) throws SQLException {
        Tarea tarea = new Tarea();
        tarea.setId(rs.getLong("id"));
        tarea.setTitulo(rs.getString("titulo"));
        tarea.setDescripcion(rs.getString("descripcion"));
        tarea.setCompletada(rs.getInt("completada") == 1);
        tarea.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        Timestamp fechaCompletada = rs.getTimestamp("fecha_completada");
        if (fechaCompletada != null) {
            tarea.setFechaCompletada(fechaCompletada.toLocalDateTime());
        }
        return tarea;
    }
}
