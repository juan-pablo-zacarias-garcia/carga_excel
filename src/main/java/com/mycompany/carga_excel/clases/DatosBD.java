/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carga_excel.clases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author ELIZABETH
 */
public class DatosBD {
    //Esta clase siempre leera los datos de la bd desde el archivo

    public String host;
    public String instance;
    public String database;
    public String user;
    public String password;
    //ruta del archivo
    private String urlf = "src/main/java/env.txt";
    //palabra
    String llave = "Estaesunallaveen64";

    DatosBD() {
        //inicializa las variables, después si hay algo en el archivo actualiza las variables
        this.host = "host";
        this.instance = "instance";
        this.database = "databaseName";
        this.user = "user";
        this.password = "password";
        File env_file = new File(this.urlf);
        try {
            // Si el archivo no existe es creado
            if (!env_file.exists()) {
                env_file.createNewFile();
                //actualiza los datos con datos por default
                actualizar_datos_conexion(this.host, this.instance, this.database, this.user, this.password);
                //Lee los datos por default
                leer_datos_conexion();
            } else {
                //si el archivo existe lee los datos del archivo
                leer_datos_conexion();
            }
        } catch (Exception e) {

        }
    }

    private void leer_datos_conexion() {
        //Este método devuelve el arreglo con los datos para la conexión con la bd
        //El orden de los datos es: host,instance,port,database, user, password
        File env_file = new File(this.urlf);
        try {

            // Leer del archivo
            FileInputStream fl = new FileInputStream(env_file);
            // creamos un arreglo de bytes de la longitud del archivo que son los del archivo
            byte[] arr = new byte[(int) env_file.length()];
            // Leemos el contenido del archivo al array
            fl.read(arr);
            // cerramos la instacia del fileinputstream
            fl.close();
            Alg_enc oenc = new Alg_enc();
            //Desencriptamos el arreglo de bytes y guardamos el string en la variable linea
            String linea = oenc.decript(arr, llave);
            //los datos se separan por ; y se almacenan en el arreglo
            String[] datos_conexion = linea.split(";");
            //actualiza los datos de la clase principal
            this.host = datos_conexion[0];
            this.instance = datos_conexion[1];
            this.database = datos_conexion[2];
            this.user = datos_conexion[3];
            this.password = datos_conexion[4];
        } catch (Exception ex) {
            System.out.println("DatosBD/leer_datos_conexion/Mensaje: " + ex.getMessage());
            //Si no hay información en el archivo entonces lo actualiza con los datos por default
            if (ex.getMessage().equalsIgnoreCase("No line found")) {
                actualizar_datos_conexion(this.host, this.instance, this.database, this.user, this.password);

            }
        }
    }
    //El siguiente método recibe como parámetro una cadena que son los datos de conexion de la bd
    //encripta la información y la guarda en el archivo env.txt

    public boolean actualizar_datos_conexion(String host, String instance, String database, String user, String password) {
        String datos = host + ";" + instance + ";" + database + ";" + user + ";" + password;
        byte[] datos_enc;
//abrimos el archivo
        File env_file = new File(this.urlf);
        try {
            //encriptamos la cadena
            Alg_enc oenc = new Alg_enc();
            //cuando encriptamos nos devuelve un arreglo de bytes que es el texto cifrado, este hay que guardarlo en el archivo
            datos_enc = oenc.encript(datos, llave);
            // Si el archivo no existe es creado
            if (!env_file.exists()) {
                env_file.createNewFile();
            }
            //Escribimos en el archivo
            // Initialize a pointer in file
            // using OutputStream
            OutputStream os = new FileOutputStream(env_file);
            os.write(datos_enc);
            //y vuelve a leer los datos del archivo
            leer_datos_conexion();
            return true;
        } catch (Exception e) {
            System.out.println("DatosBD/actualizar_datos_conexion/ Exception: " + e);
            return false;
        }
    }

    private void crear_dir(File file) {
        try {
            file.createNewFile();
        } catch (IOException ex) {
            System.out.println("DatosBD/crear_dir: " + ex);
        }
    }
}
