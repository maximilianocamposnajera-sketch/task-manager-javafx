package database;

import model.Tarea;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:gestor_tareas.db";

    public static Connection conectar() {
        try {
            System.out.println("Conexión a SQLite exitosa.");
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("Error al conectar con SQLite.");
            e.printStackTrace();
            return null;
        }
    }

    public static void crearTablaUsuarios() {
        String sql = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario TEXT NOT NULL UNIQUE,
                contrasena TEXT NOT NULL
            );
        """;

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void crearTablaTareas() {
        String sql = """
            CREATE TABLE IF NOT EXISTS tareas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                completada INTEGER NOT NULL,
                fecha TEXT NOT NULL,
                FOREIGN KEY(usuario) REFERENCES usuarios(usuario)
            );
        """;

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Tarea> obtenerTareasPorUsuario(String usuario) {
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT * FROM tareas WHERE usuario = ? ORDER BY fecha ASC";

        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String descripcion = rs.getString("descripcion");
                boolean completada = rs.getBoolean("completada");
                String fecha = rs.getString("fecha");
                tareas.add(new Tarea(id, descripcion, completada, fecha, usuario));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tareas;
    }

    public static void insertarTarea(Tarea tarea) {
        String sql = "INSERT INTO tareas (usuario, descripcion, completada, fecha) VALUES (?, ?, ?, ?)";

        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tarea.getUsuario());
            pstmt.setString(2, tarea.getDescripcion());
            pstmt.setBoolean(3, tarea.isCompletada());
            pstmt.setString(4, tarea.getFecha());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void eliminarTarea(int id) {
        String sql = "DELETE FROM tareas WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void marcarComoCompletada(int id) {
        String sql = "UPDATE tareas SET completada = 1 WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean validarCredenciales(String usuario, String contrasena) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void actualizarTarea(int id, String nuevaDescripcion, String nuevaFecha) {
        String sql = "UPDATE tareas SET descripcion = ?, fecha = ? WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevaDescripcion);
            pstmt.setString(2, nuevaFecha);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
