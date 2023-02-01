/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.carga_excel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.mycompany.carga_excel.clases.Conexion_bd;
import com.mycompany.carga_excel.clases.Alg_enc;

/**
 * FXML Controller class
 *
 * @author ELIZABETH
 */
public class Form_bdController implements Initializable {

    //instancia de la clase conexion_bd
    private Conexion_bd obj_bd;
    //leemos los datos de conexión del archivo env
    private String[] datos_url;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Nueva instancia para conexión con bd
        obj_bd = new Conexion_bd();
        //Leemos los datos de conexión del archivo env y los guardamos en el arreglo
        datos_url = obj_bd.leer_datos_conexion();
        //actualiza los campos del formulario con los datos del archivo
        inicializar_form();

    }

    @FXML
    Button aceptar, probar_conexion, actualizar, cancelar;
    @FXML
    TextField host, instance, database, user, url;
    @FXML
    PasswordField password;

    //Actualiza los campos del formulario con los datos extraidos del archivo env
    @FXML
    void inicializar_form() {
        host.setText(datos_url[0]);
        instance.setText(datos_url[1]);
        database.setText(datos_url[3]);
        user.setText(datos_url[4]);
        password.setText(datos_url[5]);
        url.setText("jdbc:sqlserver://" + datos_url[0]
                + "\\" + datos_url[1]
                + ";databaseName=" + datos_url[3]
                + ";user=" + datos_url[4]
                + ";password=*"
                + ";encrypt=true"
                + ";trustServerCertificate=true"
                + ";loginTimeout=30;");
    }

    @FXML
    void cerrar_ventana() throws IOException {
        Stage stage = (Stage) aceptar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void actualizar_url() {
        
    }
    
    @FXML
    void probar_conexion() throws Exception{
        Conexion_bd obd = new Conexion_bd();
        obd.actualizar_datos_conexion("prueba de cifrado");
        
    }

}
