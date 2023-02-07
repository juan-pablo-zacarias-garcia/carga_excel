/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carga_excel.clases;

/**
 *
 * @author Juan Pablo Zacarias
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javafx.scene.control.Alert;

public class Conexion_bd {
    public DatosBD datos_bd;
    public Conexion_bd(){
        //en el contructor inicializamos los valores de los datos para la conexion;
        datos_bd = new DatosBD();
    }

    public Connection conectar(String url) {
       
        String connectionUrl
                = "jdbc:sqlserver://"+datos_bd.host+"\\"+datos_bd.instance+";"
                + "databaseName="+datos_bd.database+";"
                + "user="+datos_bd.user+";"
                + "password="+datos_bd.password+";"
                + "trustServerCertificate=true;"
                + "encrypt=true;";
        ResultSet resultSet = null;

        try {
            //establece la conexión con la bd
            Connection connection = DriverManager.getConnection(connectionUrl);
            System.out.println("COnexion_bd/conectar/Conexión establecida");
            return connection;

        } catch (Exception e) {
            //Alerta sobre datos cargados
            alert("Error de conexión", "No se ha conectado con la base de datos", Alert.AlertType.WARNING);
            //System.out.println("Error de conexión "+e);
            return null;
        }

    }

    public ResultSet insert(String query, Connection conn) {
        try {

            Statement statement = conn.createStatement();
            ResultSet resultSet = null;
            // se ejecuta la consulta de insert y devuelve los valores insertados
            PreparedStatement prepsInsertProduct = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            prepsInsertProduct.execute();

            resultSet = prepsInsertProduct.getGeneratedKeys();
            return resultSet;
        } catch (Exception e) {
            System.out.println(query);
            System.out.println("Conexion_bd/insert/Error en consulta " + e);
            return null;
        }
    }

    public void cerrar_conexion(Connection conn) {
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Conexion_bd/cerrar_conexion/Error al cerrar conexión " + ex);
        }
    }



    //Método para mostrar alertas
    public void alert(String title, String msg, Alert.AlertType type) {
        Alert alerta_archivo = new Alert(type);
        alerta_archivo.setTitle(title);
        alerta_archivo.setContentText(msg);
        alerta_archivo.showAndWait();
    }

}
