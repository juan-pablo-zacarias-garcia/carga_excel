package com.mycompany.carga_excel;

import com.mycompany.carga_excel.clases.Conexion_bd;
import com.mycompany.carga_excel.clases.Consultas;
import com.mycompany.carga_excel.clases.Excel;
import javafx.fxml.FXML;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PrimaryController implements Initializable {

    //globales
    String ruta_archivo = null;
    File archivo = null;
    int no_sheet = 0, row_headers = 0, no_colums = 0;
    Excel doc_excel = new Excel();

    //FXML
    @FXML
    Label nom_archivo;
    @FXML
    VBox vb1;
    @FXML
    Button btn_seleccionar_datos, btn_exportar;

    //Método par abrir el archivo con los datos de conexion a la bd
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
            btn_seleccionar_datos.setDisable(false);
            try {
                nom_archivo.setText(file.getPath());
                archivo = file;
                doc_excel.abrir(file);
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

        GridPane grid = new GridPane();
        for (int j = 0; j < doc_excel.no_columns; j++) {
            ColumnConstraints column = new ColumnConstraints(100);
            grid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < doc_excel.no_rows; i++) {
            for (int j = 0; j < doc_excel.no_columns; j++) {
                Label nodo = new Label(doc_excel.datos.get(index_datos).toString());
                grid.add(nodo, j, i);
                index_datos++;
            }
        }

        //carga el grid con los datos al ScrollPane y luego al Pane
        ListView lv = new ListView();
        lv.getItems().addAll(grid);
        //Limpia los datos mostrados y los vuelve a cargar
        vb1.getChildren().clear();
        vb1.getChildren().add(lv);

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
        combo_sheets.getSelectionModel().select(0);
        
        TextField tf_row_headers = new TextField();
        TextField tf_no_columns = new TextField();
        TextField tf_nom_tabla = new TextField();

        grid.add(new Label("Hoja: "), 0, 0);
        grid.add(new Label("Fila de encabezados: "), 0, 1);
        grid.add(new Label("Número de columnas: "), 0, 2);
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
                doc_excel.leer(no_sheet, no_colums, row_headers);
                mostrar_datos();
                btn_exportar.setDisable(false);
            }
        });

        VBox vbox = new VBox(grid, btn);
        root.getChildren().add(vbox);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    //Selecciona la tabla de la base de datos a la que se insertará la información
    @FXML
    void seleccionar_tabla_bd() {

        //se abre una nueva ventana
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Seleccionar tabla");

        //Se contrulle la interfaz de manera manual
        //contenedores
        AnchorPane root = new AnchorPane();
        GridPane grid = new GridPane();

        //control
        ComboBox combo_tables = new ComboBox();
        grid.add(new Label("Exportar a tabla: "), 0, 0);
        grid.add(combo_tables, 1, 0);
        Button btn = new Button();
        btn.setText("Exportar datos");
        //recuperar datos de la bd
        try {
            Conexion_bd obj_bd = new Conexion_bd();
            Consultas queries = new Consultas();
            Connection  conn = obj_bd.conectar("");
            
            ResultSet tablas = obj_bd.query(queries.tablas_bd, conn);
            
            while (tablas.next()) {
                String item = tablas.getString("name");
                combo_tables.getItems().add(item);
            }
            //Cerramos la conexión
            conn.close();
            //seleccionamos el item 1 por default
            combo_tables.getSelectionModel().select(0);
        } catch (Exception e) {
            System.out.println("PrimaryController/Seleccionar_tabla_bd/ " + e);
        }
        //Cuando presiona el boton exporta los datos a la tabla seleccionada
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String tabla = combo_tables.getValue().toString();
                //Si se seleccionó una tabla se exportan los datos, si no se manda una alerta
                    //Exporta los datos
                    exportar(tabla);
                    //Cierra la ventana
                    Stage stage = (Stage)btn.getScene().getWindow();
                    stage.close();
            }
        });

        VBox vbox = new VBox(grid, btn);
        root.getChildren().add(vbox);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();

    }

    //Lee el archivo de excel cargado y lo exporta a la base de datos
    void exportar(String tabla_bd) {
        if (archivo != null) {
            //exporta los datos, los datos se generan al leer el archivo
            doc_excel.exportar_datos(doc_excel.datos, doc_excel.no_columns, doc_excel.columns, tabla_bd);
            //Se reinician las variables necesarias
            nom_archivo.setText("");
            archivo = null;
            vb1.getChildren().clear();
            btn_seleccionar_datos.setDisable(true);
            btn_exportar.setDisable(true);
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_seleccionar_datos.setDisable(true);
        btn_exportar.setDisable(true);
    }
}
