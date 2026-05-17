
import com.ejemplo.gestortareas.database.DatabaseConnection;
import com.ejemplo.gestortareas.modelo.Tarea;
import com.ejemplo.gestortareas.servicio.TareaService;
import java.sql.Connection;
import java.sql.Statement;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author usuario
 */
@DisplayName("Tests de cobertura - actualizarTarea()")
public class ActualizarTareaTest {
    private TareaService tareaService;

    @BeforeAll
    static void inicializarBD() {
        DatabaseConnection.inicializarTablas();
    }

    @BeforeEach
    void setUp() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM tareas");
        }
        tareaService = new TareaService();
    }

    @AfterAll
    static void cerrarBD() {
        DatabaseConnection.cerrarConexion();
    }
    
    /* RAMA 1: Título inválido → lanza IllegalArgumentException
       Cubre: if (titulo == null || titulo.trim().isEmpty()) → true */ 
    @Test
    @DisplayName("actualizarTarea_debeLanzarExcepcion_siTituloEsNullOVacio")
    void actualizarTarea_debeLanzarExcepcion_siTituloEsNullOVacio() {
        //Caso título null
        IllegalArgumentException exNull = assertThrows(IllegalArgumentException.class, () ->
                tareaService.actualizarTarea(1L, null, "Descripción"));
        assertEquals("El título no puede estar vacío", exNull.getMessage());

        //Caso título vacío con espacios
        IllegalArgumentException exVacio = assertThrows(IllegalArgumentException.class, () ->
                tareaService.actualizarTarea(1L, "   ", "Descripción"));
        assertEquals("El título no puede estar vacío", exVacio.getMessage());
    }

    /* RAMA 2: ID no existe → retorna false
       Cubre: if (tareaOpt.isPresent()) → false → return false */
    @Test
    @DisplayName("actualizarTarea_debeRetornarFalse_siIdNoExiste")
    void actualizarTarea_debeRetornarFalse_siIdNoExiste() {
        boolean resultado = tareaService.actualizarTarea(9999L, "Título válido", "Descripción");

        assertFalse(resultado);
    }

    /* RAMA 3: Tarea existe + descripción null → actualiza correctamente
       Cubre: if (tareaOpt.isPresent()) → true
       descripcion != null ? ... : "" → rama con null
       return tareaDAO.actualizar(tarea) → true */
    @Test
    @DisplayName("actualizarTarea_debeActualizarCorrectamente_siTareaExisteYDescripcionEsNull")
    void actualizarTarea_debeActualizarCorrectamente_siTareaExisteYDescripcionEsNull() {
        Tarea tarea = tareaService.crearTarea("Título original", "Descripción original");
        assertNotNull(tarea);

        boolean resultado = tareaService.actualizarTarea(tarea.getId(), "  Título actualizado  ", null);

        assertTrue(resultado);
        Tarea tareaActualizada = tareaService.obtenerTarea(tarea.getId()).orElse(null);
        assertNotNull(tareaActualizada);
        assertEquals("Título actualizado", tareaActualizada.getTitulo());//verifica trim()
        assertEquals("", tareaActualizada.getDescripcion());//verifica null → ""
    }
    
}
