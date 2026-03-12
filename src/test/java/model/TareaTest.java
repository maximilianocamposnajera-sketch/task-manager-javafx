package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TareaTest {

    @Test
    void gettersYSettersFuncionan() {
        Tarea t = new Tarea(1, "Probar JUnit", false, "2025-08-08", "usuario1");

        assertEquals(1, t.getId());
        assertEquals("Probar JUnit", t.getDescripcion());
        assertFalse(t.isCompletada());
        assertEquals("2025-08-08", t.getFecha());
        assertEquals("usuario1", t.getUsuario());

        // modificar
        t.setDescripcion("Nuevo texto");
        t.setCompletada(true);
        t.setFecha("2025-08-09");
        t.setUsuario("otro");

        assertEquals("Nuevo texto", t.getDescripcion());
        assertTrue(t.isCompletada());
        assertEquals("2025-08-09", t.getFecha());
        assertEquals("otro", t.getUsuario());
    }

    @Test
    void formatoFechaYYYYMMDDOrdenaLexicograficamente() {
        // si usas "yyyy-MM-dd", ordenar por String funciona
        Tarea a = new Tarea(1, "A", false, "2025-01-01", "u");
        Tarea b = new Tarea(2, "B", false, "2025-02-01", "u");
        assertTrue(a.getFecha().compareTo(b.getFecha()) < 0, "La fecha debe ordenar correctamente");
    }
}
