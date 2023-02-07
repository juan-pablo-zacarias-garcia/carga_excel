package com.mycompany.carga_excel;

import com.mycompany.carga_excel.clases.Excel;
import javafx.fxml.FXML;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PrimaryController {

    //globales
    String ruta_archivo = null;
    File archivo = null;
    int no_sheet = 12, row_headers=1, no_colums=69; 
    Excel doc_excel = new Excel();

    //FXML
    @FXML
    Label nom_archivo;
    @FXML
    GridPane grid;

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
                doc_excel.leer(archivo, no_sheet, no_colums, row_headers);
                mostrar_datos();
            } catch (Exception e) {
                System.out.println("PrimaryController/abrir_archivo/ " + e);
            }

        } else {
            //Muestra una alerta de archivo no cargado
            alert("Archivo no cargado", "No se ha seleccionado ningun archivo", AlertType.WARNING);
        }
    }

    //Método para mostrar los datos en pantalla
    void mostrar_datos() {
        int index_datos = 0;
        for (int j = 0; j < doc_excel.no_columns; j++) {
            ColumnConstraints column = new ColumnConstraints(100);
            grid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < doc_excel.no_rows; i++) {
            for (int j = 0; j < doc_excel.no_columns; j++) {
                Label nodo = new Label(doc_excel.datos.get(index_datos));
                grid.add(nodo, j, i);
                index_datos++;
            }
        }
    }

    //Selecciona la hoja de excel, la fila conde inician los encabezados de la tabla y el número de columnas de la tabla
    @FXML
    void seleccionar_datos() {
        //se abre una nueva ventana
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Seleccionar datos de Excel");
        
        //Se contrulle la interfaz de manera manual
        //contenedores
        AnchorPane root = new AnchorPane();
        GridPane grid = new GridPane();
        
        //control
        ComboBox combo_sheets = new ComboBox();
        combo_sheets.getItems().addAll(doc_excel.getSheets());
        TextField tf_row_headers = new TextField();
        TextField tf_no_columns = new TextField();
        grid.add(new Label("Hoja: "),0,0);
        grid.add(new Label("Fila de encabezados: "),0,1);
        grid.add(new Label("Número de columnas: "),0,2);
        grid.add(combo_sheets, 1, 0);
        grid.add(tf_row_headers, 1, 1);
        grid.add(tf_no_columns, 1, 2);
        Button btn = new Button();
        btn.setText("Aceptar");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                //Se actualizan las variables de la clase PrimaryController
               no_sheet = combo_sheets.getSelectionModel().getSelectedIndex();
               row_headers = Integer.parseInt(tf_row_headers.getText());
               no_colums = Integer.parseInt(tf_no_columns.getText());
               
                
            }
        });
        
        
        VBox vbox = new VBox(grid, btn);
        root.getChildren().add(vbox);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    //Lee el archivo de excel cargado y lo exporta a la base de datos
    @FXML
    void exportar() {
        if (archivo != null) {
            //exporta los datos, los datos se generan al leer el archivo
            doc_excel.exportar_datos(doc_excel.datos, doc_excel.no_columns, doc_excel.columns, "tablas");
            nom_archivo.setText("");
            archivo = null;
        } else {
            alert("Archivo no cargado", "No se ha seleccionado ningun archivo", AlertType.WARNING);
        }
    }

    //Método para abrir el formulario para la configuración de conexión con la BD
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

