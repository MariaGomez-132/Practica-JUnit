
import com.ejemplo.gestortareas.database.DatabaseConnection;
import com.ejemplo.gestortareas.modelo.Tarea;
import com.ejemplo.gestortareas.servicio.TareaService;
import java.sql.Connection;
import java.sql.Statement;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
@DisplayName("Tests de regresión - TareaService")
class TareaServiceTest {

    private TareaService tareaService;

    @BeforeAll
    static void inicializarBD() {//Crea la tabla si no existe antes de ejecutar ningún test
        DatabaseConnection.inicializarTablas();
    }

    @BeforeEach
    void setUp() throws Exception {//Limpia la tabla antes de cada test para partir de un estado limpio
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

    /* TESTS PARA crearTarea() */
    //Verifica que se crea correctamente una tarea con título y descripción válidos.
    @Test
    @DisplayName("testCrearTarea_Exitoso")
    void testCrearTarea_Exitoso() {
        Tarea resultado = tareaService.crearTarea("Título de prueba", "Descripción de prueba");

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Título de prueba", resultado.getTitulo());
        assertEquals("Descripción de prueba", resultado.getDescripcion());
    }

    //Verifica que se lanza IllegalArgumentException cuando el título es una cadena vacía.
    @Test
    @DisplayName("testCrearTarea_TituloVacio")
    void testCrearTarea_TituloVacio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                tareaService.crearTarea("", "Descripción"));

        assertEquals("El título no puede estar vacío", ex.getMessage());
    }

    //Verifica que se lanza IllegalArgumentException cuando el título es null.
    @Test
    @DisplayName("testCrearTarea_TituloNull")
    void testCrearTarea_TituloNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                tareaService.crearTarea(null, "Descripción"));

        assertEquals("El título no puede estar vacío", ex.getMessage());
    }

    //Verifica que un título formado solo por espacios en blanco es rechazado.
    @Test
    @DisplayName("testCrearTarea_TituloSoloEspacios")
    void testCrearTarea_TituloSoloEspacios() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                tareaService.crearTarea("   ", "Descripción"));

        assertEquals("El título no puede estar vacío", ex.getMessage());
    }

    //Verifica que el título se guarda sin espacios al inicio ni al final (trim).
    @Test
    @DisplayName("testCrearTarea_TituloConEspaciosAlrededor")
    void testCrearTarea_TituloConEspaciosAlrededor() {
        Tarea resultado = tareaService.crearTarea("  Título limpio  ", "");

        assertNotNull(resultado);
        assertEquals("Título limpio", resultado.getTitulo());
    }

    //Verifica que cuando la descripción es null se almacena como cadena vacía.
    @Test
    @DisplayName("testCrearTarea_DescripcionNull")
    void testCrearTarea_DescripcionNull() {
        Tarea resultado = tareaService.crearTarea("Título", null);

        assertNotNull(resultado);
        assertEquals("", resultado.getDescripcion());
    }

    //Verifica que una tarea recién creada tiene el estado completada = false.
    @Test
    @DisplayName("testCrearTarea_CompletadaEsFalsaPorDefecto")
    void testCrearTarea_CompletadaEsFalsaPorDefecto() {
        Tarea resultado = tareaService.crearTarea("Nueva tarea", "Descripción");

        assertNotNull(resultado);
        assertFalse(resultado.isCompletada());
        assertNull(resultado.getFechaCompletada());
    }

    //Verifica que la tarea creada tiene una fecha de creación asignada automáticamente.
    @Test
    @DisplayName("testCrearTarea_FechaCreacionNoEsNull")
    void testCrearTarea_FechaCreacionNoEsNull() {
        Tarea resultado = tareaService.crearTarea("Tarea con fecha", "Descripción");

        assertNotNull(resultado);
        assertNotNull(resultado.getFechaCreacion());
    }

    //Verifica que la tarea creada recibe un ID generado por la base de datos.
    @Test
    @DisplayName("testCrearTarea_IDGeneradoAutomaticamente")
    void testCrearTarea_IDGeneradoAutomaticamente() {
        Tarea resultado = tareaService.crearTarea("Tarea con ID", "Descripción");

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertTrue(resultado.getId() > 0);
    }

    /* TESTS PARA completarTarea() */
    //Verifica que una tarea existente se marca como completada correctamente.
    @Test
    @DisplayName("testCompletarTarea_Exitoso")
    void testCompletarTarea_Exitoso() {
        Tarea tarea = tareaService.crearTarea("Tarea a completar", "Descripción");
        assertNotNull(tarea);

        boolean resultado = tareaService.completarTarea(tarea.getId());

        assertTrue(resultado);
    }

    //Verifica que se devuelve false cuando el ID no corresponde a ninguna tarea.
    @Test
    @DisplayName("testCompletarTarea_TareaNoExiste")
    void testCompletarTarea_TareaNoExiste() {
        boolean resultado = tareaService.completarTarea(9999L);

        assertFalse(resultado);
    }

    //Verifica que tras completar la tarea, su campo completada es true al recuperarla de la BD.
    @Test
    @DisplayName("testCompletarTarea_EstadoCompletadaEsTrue")
    void testCompletarTarea_EstadoCompletadaEsTrue() {
        Tarea tarea = tareaService.crearTarea("Tarea para verificar estado", "Descripción");
        assertNotNull(tarea);

        tareaService.completarTarea(tarea.getId());

        Tarea tareaActualizada = tareaService.obtenerTarea(tarea.getId()).orElse(null);
        assertNotNull(tareaActualizada);
        assertTrue(tareaActualizada.isCompletada());
    }

    //Verifica que al completar una tarea se le asigna una fecha de completado.
    @Test
    @DisplayName("testCompletarTarea_FechaCompletadaAsignada")
    void testCompletarTarea_FechaCompletadaAsignada() {
        Tarea tarea = tareaService.crearTarea("Tarea con fecha completada", "Descripción");
        assertNotNull(tarea);

        tareaService.completarTarea(tarea.getId());

        Tarea tareaActualizada = tareaService.obtenerTarea(tarea.getId()).orElse(null);
        assertNotNull(tareaActualizada);
        assertNotNull(tareaActualizada.getFechaCompletada());
    }

    //Verifica que pasar un ID null provoca una excepción ya que el driver no lo soporta.
    @Test
    @DisplayName("testCompletarTarea_IdNull")
    void testCompletarTarea_IdNull() {
        // El driver SQLite lanza NullPointerException al recibir un ID null
        assertThrows(Exception.class, () -> tareaService.completarTarea(null));
    }
}