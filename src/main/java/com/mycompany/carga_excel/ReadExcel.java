/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carga_excel;

/**
 *
 * @author ELIZABETH
 */
import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.Cell;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.sql.Connection;
import java.util.ArrayList;

public class ReadExcel {

    void leer() {
        try {
            //Obtiene el archivo
            FileInputStream fis = new FileInputStream(new File("C:\\Users\\ELIZABETH\\Documents\\clientes.xlsx"));  
            //instancia del objeto workbook para archivos xlsx
            XSSFWorkbook wb = new XSSFWorkbook(fis);  
            //instancia del objeto sheet para archivos xlsx
            XSSFSheet sheet = wb.getSheetAt(0);
            //arraylist para guardar los datos de las celdas de excel
            ArrayList<String> items = new ArrayList<String>(); 
            
            //recorre las filas de la hoja de excel
            for (Row row : sheet) //iteration over row using for each loop  
            {
                //recorre las celdas de cada fila;
                for (Cell cell : row)  
                {
                    //los valores de las celdas pueden ser leidos como numéricos o string
                    //los valores se ponen entre comillas simples y divididos por comas para generar la consulta sql
                    if(cell.getCellType().equals(NUMERIC)){
                        //se guarda el valor de la celda
                        items.add(String.valueOf(cell.getNumericCellValue()));
                    }
                    if(cell.getCellType().equals(STRING)){
                        //se guarda el valor de la celda
                        items.add(cell.getStringCellValue());
                    }
                    
                }
                
            }
            
            //iniico la conexion para cargar los datos;
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
            int index_item = 9;
            //almacena los valores de cada fila
            String values;
            //cada fila está conformada por 9 columnas, entonces dividimos el arraylist entre 9
            for(int i=0; i<items.size()/9; i++){
                values = "";
                //se estraen los elementos del arraylist de 9 en 9 y se les da formato
                for(int j=0; j<9; j++){
                    values = values+"\'"+items.get(index_item)+"\',";
                    //index del array list
                    index_item++;
                }
                //se quita la última coma a values
                values = values.substring(0, values.length()-1);
                //se realiza la consulta sql
                con.insert(query+"("+values+")",conn);
            }
            //se reinicia el index del array list
            index_item = 9;
            con.cerrar_conexion(conn);
        } catch (Exception e) {
            System.out.println("Error al leer archivo " + e);
        }

    }

}
