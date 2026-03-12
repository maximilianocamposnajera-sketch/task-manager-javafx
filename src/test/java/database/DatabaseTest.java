package database;

import model.Tarea;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseTest {

    private static final String USER = "junit_user";
    private static final String PASS = "secret";

    @BeforeAll
    static void prepararTablasYUsuario() throws Exception {
        Database.crearTablaUsuarios();
        Database.crearTablaTareas();
        // Insertar usuario de prueba si no existe
        try (Connection c = Database.conectar();
             PreparedStatement st = c.prepareStatement(
                     "INSERT OR IGNORE INTO usuarios(usuario, contrasena) VALUES(?, ?)")) {
            st.setString(1, USER);
            st.setString(2, PASS);
            st.executeUpdate();
        }
    }

    @AfterAll
    static void limpiarUsuarioYtareas() throws Exception {
        try (Connection c = Database.conectar()) {
            try (PreparedStatement st1 = c.prepareStatement("DELETE FROM tareas WHERE usuario = ?")) {
                st1.setString(1, USER);
                st1.executeUpdate();
            }
            try (PreparedStatement st2 = c.prepareStatement("DELETE FROM usuarios WHERE usuario = ?")) {
                st2.setString(1, USER);
                st2.executeUpdate();
            }
        }
    }

    @Test
    @Order(1)
    void validarCredencialesFunciona() {
        assertTrue(Database.validarCredenciales(USER, PASS));
        assertFalse(Database.validarCredenciales(USER, "mal"));
    }

    @Test
    @Order(2)
    void insertarYListarTarea() {
        Tarea t = new Tarea(0, "Tarea de prueba", false, "2025-08-08", USER);
        Database.insertarTarea(t);

        List<Tarea> tareas = Database.obtenerTareasPorUsuario(USER);
        assertFalse(tareas.isEmpty());
        assertTrue(tareas.stream().anyMatch(x -> x.getDescripcion().equals("Tarea de prueba")));
    }

    @Test
    @Order(3)
    void actualizarYCompletarTarea() {
        List<Tarea> tareas = Database.obtenerTareasPorUsuario(USER);
        Tarea target = tareas.stream()
                .filter(x -> x.getDescripcion().equals("Tarea de prueba"))
                .findFirst().orElseThrow();

        Database.actualizarTarea(target.getId(), "Tarea editada", "2025-08-09");
        List<Tarea> tareas2 = Database.obtenerTareasPorUsuario(USER);
        Tarea editada = tareas2.stream().filter(x -> x.getId() == target.getId()).findFirst().orElseThrow();
        assertEquals("Tarea editada", editada.getDescripcion());
        assertEquals("2025-08-09", editada.getFecha());

        Database.marcarComoCompletada(editada.getId());
        List<Tarea> tareas3 = Database.obtenerTareasPorUsuario(USER);
        Tarea completada = tareas3.stream().filter(x -> x.getId() == editada.getId()).findFirst().orElseThrow();
        assertTrue(completada.isCompletada());
    }

    @Test
    @Order(4)
    void eliminarTarea() {
        List<Tarea> tareas = Database.obtenerTareasPorUsuario(USER);
        for (Tarea t : tareas) {
            Database.eliminarTarea(t.getId());
        }
        List<Tarea> tareasFinal = Database.obtenerTareasPorUsuario(USER);
        assertTrue(tareasFinal.isEmpty());
    }
}
