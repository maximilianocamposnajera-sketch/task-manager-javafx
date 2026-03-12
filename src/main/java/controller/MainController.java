package controller;

import database.Database;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Tarea;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class MainController {

    @FXML private Label lblUsuario;
    @FXML private TableView<Tarea> tablaTareas;
    @FXML private TableColumn<Tarea, String> colTarea;
    @FXML private TableColumn<Tarea, Boolean> colCompletada;
    @FXML private TableColumn<Tarea, String> colFecha;
    @FXML private TextField txtDescripcion;
    @FXML private DatePicker datePickerFecha;

    private String usuarioActivo;

    public void setUsuario(String usuario) {
        this.usuarioActivo = usuario;
        lblUsuario.setText(usuario);
        cargarTareas();
    }

    @FXML
    public void initialize() {
        colTarea.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());
        colCompletada.setCellValueFactory(cellData -> cellData.getValue().completadaProperty().asObject());
        colFecha.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());

        tablaTareas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, nuevaTarea) -> {
            if (nuevaTarea != null) {
                txtDescripcion.setText(nuevaTarea.getDescripcion());
                datePickerFecha.setValue(LocalDate.parse(nuevaTarea.getFecha()));
            }
        });
    }

    private void cargarTareas() {
        List<Tarea> tareas = Database.obtenerTareasPorUsuario(usuarioActivo);
        tareas.sort(Comparator.comparing(Tarea::getFecha));
        tablaTareas.setItems(FXCollections.observableArrayList(tareas));
    }

    @FXML
    private void agregarTarea() {
        String descripcion = txtDescripcion.getText().trim();
        LocalDate fecha = datePickerFecha.getValue();

        if (descripcion.isEmpty() || fecha == null) {
            mostrarAlerta("Campos incompletos", "Debes ingresar descripción y fecha.");
            return;
        }

        Tarea nueva = new Tarea(descripcion, false, fecha.toString(), usuarioActivo);
        Database.insertarTarea(nueva);
        cargarTareas();
        limpiarCampos();
    }

    @FXML
    private void editarTarea() {
        Tarea seleccionada = tablaTareas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Error", "Selecciona una tarea para editar.");
            return;
        }

        String nuevaDescripcion = txtDescripcion.getText().trim();
        LocalDate nuevaFecha = datePickerFecha.getValue();

        if (nuevaDescripcion.isEmpty() || nuevaFecha == null) {
            mostrarAlerta("Campos incompletos", "Debes ingresar descripción y fecha.");
            return;
        }

        Database.actualizarTarea(seleccionada.getId(), nuevaDescripcion, nuevaFecha.toString());
        cargarTareas();
        limpiarCampos();
    }

    @FXML
    private void eliminarTarea() {
        Tarea seleccionada = tablaTareas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Error", "Selecciona una tarea para eliminar.");
            return;
        }

        Database.eliminarTarea(seleccionada.getId());
        cargarTareas();
    }

    @FXML
    private void marcarCompletada() {
        Tarea seleccionada = tablaTareas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Error", "Selecciona una tarea para marcar como completada.");
            return;
        }

        List<Tarea> tareas = tablaTareas.getItems();
        tareas.sort(Comparator.comparing(Tarea::getFecha));

        for (Tarea t : tareas) {
            if (t.equals(seleccionada)) break;
            if (!t.isCompletada()) {
                mostrarAlerta("Orden incorrecto", "Debes completar las tareas anteriores primero.");
                return;
            }
        }

        seleccionada.setCompletada(true);
        Database.marcarComoCompletada(seleccionada.getId());
        cargarTareas();
    }

    @FXML
    private void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) tablaTareas.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtDescripcion.clear();
        datePickerFecha.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
