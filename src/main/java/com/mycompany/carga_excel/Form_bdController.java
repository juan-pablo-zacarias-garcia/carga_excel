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
import java.sql.Connection;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author ELIZABETH
 */
public class Form_bdController implements Initializable {

    //instancia de la clase conexion_bd
    private Conexion_bd obj_bd;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       obj_bd = new Conexion_bd();
       inicializar_form();
    }

    @FXML
    Button aceptar, probar_conexion, actualizar, cancelar;
    @FXML
    TextField host, instance, database, user, url;
    @FXML
    PasswordField password;

    @FXML
    void actualizar_datos(){
        boolean act = obj_bd.datos_bd.actualizar_datos_conexion(host.getText(), instance.getText(), database.getText(), user.getText(), password.getText());
        if(act){
            obj_bd.alert("Datos actualizados", "Los datos se han actualizado satisfactoriamente", Alert.AlertType.INFORMATION);
        }
    }
    //Actualiza los campos del formulario con los datos extraidos del archivo env
    @FXML
    void inicializar_form() {
        host.setText(obj_bd.datos_bd.host);
        instance.setText(obj_bd.datos_bd.instance);
        database.setText(obj_bd.datos_bd.database);
        user.setText(obj_bd.datos_bd.user);
        password.setText(obj_bd.datos_bd.password);
        url.setText("jdbc:sqlserver://" + obj_bd.datos_bd.host
                + "\\" + obj_bd.datos_bd.instance
                + ";databaseName=" +obj_bd.datos_bd.database
                + ";user=" + obj_bd.datos_bd.user
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
        url.setText("jdbc:sqlserver://" + obj_bd.datos_bd.host
                + "\\" + obj_bd.datos_bd.instance
                + ";databaseName=" +obj_bd.datos_bd.database
                + ";user=" + obj_bd.datos_bd.user
                + ";password=*"
                + ";encrypt=true"
                + ";trustServerCertificate=true"
                + ";loginTimeout=30;");
    }
    
    @FXML
    void probar_conexion() throws Exception{
        Connection con = obj_bd.conectar("");
        if(con!=null){
            obj_bd.alert("Estatus de conexión", "Conexión exitosa", Alert.AlertType.INFORMATION);
            con.close();
        }
        else{
            obj_bd.alert("Estatus conexión", "Error de conexión, revise que los datos sean correctos", Alert.AlertType.ERROR);
        }
    }

}
