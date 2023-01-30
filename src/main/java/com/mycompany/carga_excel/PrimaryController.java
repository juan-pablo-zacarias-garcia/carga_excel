package com.mycompany.carga_excel;

import javafx.fxml.FXML;
import com.mycompany.carga_excel.ReadExcel;
import java.io.File;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PrimaryController {

    //globales
    String ruta_archivo = null;
    File archivo = null;

    //FXML
    @FXML
    Label nom_archivo;

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
            nom_archivo.setText(file.getPath());
            archivo = file;

        } else {
            //Muestra una alerta de archivo no cargado
            alert("Archivo no cargado", "No se ha seleccionado ningun archivo", AlertType.WARNING);
        }
    }
    
    //Lee el archivo de excel cargado y lo exporta a la base de datos
    @FXML
    void exportar() {
        ReadExcel re = new ReadExcel();
        if (archivo != null) {
            //Lee el archivo en la hoja 0, la tabla de esa hoja contiene 9 columnas
            re.leer(archivo, 0, 9);
            //exporta los datos, los datos se generan al leer el archivo
            re.exportar_datos(re.datos, 9);
            nom_archivo.setText("");
            archivo = null;
        } else {
            alert("Archivo no cargado", "No se ha seleccionado ningun archivo", AlertType.WARNING);
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
