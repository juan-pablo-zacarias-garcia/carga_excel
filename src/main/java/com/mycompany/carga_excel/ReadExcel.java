/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carga_excel;

/**
 *
 * @author Juan Pablo Zacarias
 */
import java.io.File;
import org.apache.poi.ss.usermodel.Cell;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.sql.Connection;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

public class ReadExcel {
    
    //La variable items almacenará los datos de las celdas de excel
    ArrayList datos;
    //Guarda el número de columnas de la tabla
    int no_columns;

    void leer(File file, int no_sheet, int no_columns) {

        try {
            //instancia del objeto workbook para archivos xlsx
            XSSFWorkbook wb = new XSSFWorkbook(file);
            //instancia del objeto sheet para archivos xlsx
            XSSFSheet sheet = wb.getSheetAt(no_sheet);
            //arraylist para guardar los datos de las celdas de excel
            ArrayList<String> items = new ArrayList<String>();

            //recorre las filas de la hoja de excel
            for (Row row : sheet) //iteration over row using for each loop  
            {
                //recorre las celdas de cada fila;
                for (Cell cell : row) {
                    //los valores de las celdas pueden ser leidos como numéricos o string
                    //los valores se ponen entre comillas simples y divididos por comas para generar la consulta sql
                    if (cell.getCellType().equals(NUMERIC)) {
                        //se guarda el valor de la celda
                        items.add(String.valueOf(cell.getNumericCellValue()));
                    }
                    if (cell.getCellType().equals(STRING)) {
                        //se guarda el valor de la celda
                        items.add(cell.getStringCellValue());
                    }

                }

            }
            //Los datos de las celdas de la hoja de excel están en el arraylist items, incluyendo los encabezados
            this.datos = items;
            this.no_columns = no_columns;

        } catch (Exception e) {
            System.out.println("Error al leer archivo " + e);
        }
    }

    void exportar_datos(ArrayList items, int no_columns) {
        //inicio la conexion para cargar los datos;
        conexion_bd con = new conexion_bd();
        //completo la consulta
        String query = "INSERT INTO empleados "
                + "(id,"
                + " nombre_completo,"
                + " fecha_nacimiento,"
                + " direccion,"
                + " localidad,"
                + " telefono,"
                + " correo,"
                + " fecha_de_alta,"
                + " grupo_de_clientes)"
                + "VALUES";
        //se crea una conexion
        Connection conn = con.conectar();
        //index para extraer elementos del arraylist con las celdas de excel
        //incia en 9 para omitir la fila de los títulos
        int index_item = no_columns;
        //almacena los valores de cada fila
        String values;
        //número de filas del documento menos la fila de encabezados
        int no_filas = (items.size() / no_columns) - 1;

        
        for (int i = 0; i < no_filas; i++) {
            values = "";
            //se extraen los elementos del arraylist dependiendo el número de columnas y se les da formato
            for (int j = 0; j < no_columns; j++) {
                if (index_item < items.size()) {
                    values = values + "\'" + items.get(index_item) + "\',";
                    //index del array list
                    index_item++;
                }
            }
            //se quita la última coma a values
            values = values.substring(0, values.length() - 1);
            //se realiza la consulta sql
            con.insert(query + "(" + values + ")", conn);
        }

        //Alerta sobre datos cargados
        alert("Datos exportados", "Se han cargado " + no_filas + ""
                + " registros en la base de datos", Alert.AlertType.INFORMATION);
        //se reinicia el index del array list
        index_item = no_columns;
        //Cierra a conexión
        con.cerrar_conexion(conn);
    }

    //Método para mostrar alertas
    void alert(String title, String msg, Alert.AlertType type) {
        Alert alerta_archivo = new Alert(type);
        alerta_archivo.setTitle(title);
        alerta_archivo.setContentText(msg);
        alerta_archivo.initStyle(StageStyle.UTILITY);
        alerta_archivo.showAndWait();
    }

}
