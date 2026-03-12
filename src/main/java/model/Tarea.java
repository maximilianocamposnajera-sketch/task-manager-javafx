package model;

import javafx.beans.property.*;

public class Tarea {
    private IntegerProperty id;
    private StringProperty descripcion;
    private BooleanProperty completada;
    private StringProperty fecha;
    private StringProperty usuario;

    public Tarea(int id, String descripcion, boolean completada, String fecha, String usuario) {
        this.id = new SimpleIntegerProperty(id);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.completada = new SimpleBooleanProperty(completada);
        this.fecha = new SimpleStringProperty(fecha);
        this.usuario = new SimpleStringProperty(usuario);
    }

    public Tarea(String descripcion, boolean completada, String fecha, String usuario) {
        this(0, descripcion, completada, fecha, usuario);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public boolean isCompletada() {
        return completada.get();
    }

    public void setCompletada(boolean completada) {
        this.completada.set(completada);
    }

    public BooleanProperty completadaProperty() {
        return completada;
    }

    public String getFecha() {
        return fecha.get();
    }

    public void setFecha(String fecha) {
        this.fecha.set(fecha);
    }

    public StringProperty fechaProperty() {
        return fecha;
    }

    public String getUsuario() {
        return usuario.get();
    }

    public void setUsuario(String usuario) {
        this.usuario.set(usuario);
    }

    public StringProperty usuarioProperty() {
        return usuario;
    }
}
