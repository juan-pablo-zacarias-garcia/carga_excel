package com.mycompany.carga_excel;

import com.mycompany.carga_excel.clases.Excel;
import javafx.fxml.FXML;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PrimaryController {

    //globales
    String ruta_archivo = null;
    File archivo = null;
    Excel re = new Excel();

    //FXML
    @FXML
    Label nom_archivo;
    @FXML
    TableView tabla;

    @FXML
    void abrir_archivo() {

        //Nuevo escenario para abrir el FIleChooser
        Stage stage = new Stage();
        //FileChooser para abrir archivo 
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Abrir documento de Excel");
        chooser.getExtensionFilters().addAll(
                new ExtensionFilter("Excel Files (xlsx)", "*.xlsx"));
        File file = chooser.showOpenDialog(stage);

        if (file != null) {
            try {
                nom_archivo.setText(file.getPath());
                archivo = file;
                //Lee el archivo en la hoja 0, la tabla de esa hoja contiene 9 columnas
                //re.leer(archivo, 12, 69, 0);
                re.leer(archivo, 12, 69, 1);
            } catch (Exception e) {
                System.out.println("PrimaryController/abrir_archivo/ " + e);
            }

        } else {
            //Muestra una alerta de archivo no cargado
            alert("Archivo no cargado", "No se ha seleccionado ningun archivo", AlertType.WARNING);
        }
    }

    @FXML
    void mostrar_datos() {
        TableColumn ejemplo = new TableColumn("ejemplo");
        tabla.getColumns().addAll(ejemplo);
    }

    //Lee el archivo de excel cargado y lo exporta a la base de datos
    @FXML
    void exportar() {
        if (archivo != null) {
            //exporta los datos, los datos se generan al leer el archivo
            re.exportar_datos(re.datos, 69, re.columns, "empleados");
            nom_archivo.setText("");
            archivo = null;
        } else {
            alert("Archivo no cargado", "No se ha seleccionado ningun archivo", AlertType.WARNING);
        }
    }

    @FXML
    void config_bd() {
        //se abre una nueva ventana
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("com/mycompany/carga_excel/form_bd.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Configuración de base de datos");
            stage.setScene(new Scene(root));
            stage.resizableProperty().setValue(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void salir() {
        System.exit(0);
    }

    //metodos
    void alert(String title, String msg, AlertType type) {
        Alert alerta_archivo = new Alert(type);
        alerta_archivo.setTitle(title);
        alerta_archivo.setContentText(msg);
        alerta_archivo.initStyle(StageStyle.UTILITY);
        alerta_archivo.showAndWait();
    }
}
