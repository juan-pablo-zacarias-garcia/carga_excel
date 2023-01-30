/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carga_excel;

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
import java.util.logging.Level;
import java.util.logging.Logger;

public class conexion_bd {

    Connection conectar() {
        String connectionUrl
                = "jdbc:sqlserver://192.168.196.23\\SQLEXPRESS; "
                + "databaseName=ejemplo;"
                + "user=zagj;"
                + "password=1380JpLm;"
                + "encrypt=true;"
                + "trustServerCertificate=true;";
        ResultSet resultSet = null;

        try {
            Connection connection = DriverManager.getConnection(connectionUrl);
            System.out.println("Conexion establecida");
            return connection;

        } catch (Exception e) {
            System.out.println("Error de conexion " + e);
            return null;
        }
        
    }

    ResultSet insert(String query, Connection conn) {
        try {
            
            Statement statement = conn.createStatement();
            ResultSet resultSet = null;
            // se ejecuta la consulta de insert y devuelve los valores insertados
            PreparedStatement prepsInsertProduct = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            prepsInsertProduct.execute();
            
            resultSet = prepsInsertProduct.getGeneratedKeys();
            return resultSet;
        } catch (Exception e) {
            System.out.println("Error en consulta "+e);
            return null;
        }
    }
    
    void cerrar_conexion(Connection conn){
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error al cerrar conexión "+ex);
        }
    }
    

}
