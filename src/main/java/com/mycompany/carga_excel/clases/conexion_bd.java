/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carga_excel.clases;

/**
 *
 * @author Juan Pablo Zacarias
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;
import javafx.scene.control.Alert;

public class Conexion_bd {

    public Connection conectar() {
        //Leemos los datos para la conexión del archivo env
        String[] datos_con = leer_datos_conexion();

        String connectionUrl
                = "jdbc:sqlserver://" + datos_con[0] + "\\SQLEXPRESS;"
                + "databaseName=ejemplo;"
                + "user=zagj;"
                + "password=1380JpLm;"
                + "trustServerCertificate=true;"
                + "encrypt=true;";
        ResultSet resultSet = null;

        try {
            //establece la conexión con la bd
            Connection connection = DriverManager.getConnection(connectionUrl);
            System.out.println("Conexión establecida");
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

    public String[] leer_datos_conexion() {
        //Este método devuelve el arreglo con los datos para la conexión con la bd
        //El orden de los datos es: host,instance,port,database, user, password
        File env_file = new File("src/main/java/env.txt");
        Scanner s = null;
        try {
            // Si el archivo no existe es creado
            if (!env_file.exists()) {
                env_file.createNewFile();
            }
            // Leemos el contenido del fichero
            s = new Scanner(env_file);
            //el archivo tiene solo una línea con los datos divididos por ;
            String linea = s.nextLine();
            //los datos se separan por ; y se almacenan en el arreglo
            String[] datos_conexion = linea.split(";");
            //retornamos los datos
            return datos_conexion;
        } catch (Exception ex) {
            System.out.println("Conexion_bd/leer_datos_conexion/Mensaje: " + ex.getMessage());
            //Si no hay información en el archivo entonces devuelve un arreglo con los datos siguientes
            if (String.valueOf(ex.getMessage()).equalsIgnoreCase("No line found")) {
                String[] datos = new String[6];
                datos[0] = "-";
                datos[1] = "-";
                datos[2] = "-";
                datos[3] = "-";
                datos[4] = "-";
                datos[5] = "-";
                return datos;
            }//de lo contrario devuelve null
            else {
                return null;
            }
        } finally {
            // Cerramos el fichero tanto si la lectura ha sido correcta o no
            try {
                if (s != null) {
                    s.close();
                }
            } catch (Exception ex2) {
                System.out.println("Conexion_bd/leer_datos_conexion/Mensaje 2: " + ex2.getMessage());
            }
        }
    }

    //El siguiente método recibe como parámetro una cadena que son los datos de conexion de la bd
    //encripta la información y la guarda en el archivo env.txt
    public boolean actualizar_datos_conexion(String datos) {
        //abrimos el archivo
        File env_file = new File("src/main/java/env.txt");
        try {
            //encriptamos la cadena
            Alg_enc obj_enc = new Alg_enc();
            datos = obj_enc.encript(datos, "estaeslallavedecifrado").toString();
            // Si el archivo no existe es creado
            if (!env_file.exists()) {
                env_file.createNewFile();
            }
            //Escribimos en el archivo
            FileWriter fw = new FileWriter(env_file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(datos);
            bw.close();
            return true;
        } catch (Exception e) {
            System.out.println("Conexion_bd/actualizar_datos_conexion/ Exception: "+e);
            return false;
        }
    }

    //Método para mostrar alertas
    void alert(String title, String msg, Alert.AlertType type) {
        Alert alerta_archivo = new Alert(type);
        alerta_archivo.setTitle(title);
        alerta_archivo.setContentText(msg);
        alerta_archivo.showAndWait();
    }

}
